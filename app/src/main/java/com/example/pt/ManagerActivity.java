package com.example.pt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {
    private MediaPlayer mp;
    public ProgressDialog progressDialog = null;
    public String[] nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        mp = MediaPlayer.create(this, R.raw.bgm_5);
        mp.setLooping(true);//BGMのループ
        mp.start();//BGMの再生

        // プログレスダイアログを表示
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.downloading));
        progressDialog.show();

        // 別スレッドでファイルダウンロードを開始
        FileDownloadThread task = new FileDownloadThread(getApplicationContext());
        task.target = progressDialog;
        task.execute();
        
    }

    //画面が表示されるたびに実行
    @Override
    public void onResume() {
        super.onResume();
        mp.start();//BGMの再生
    }

    // 画面が非表示に実行
    @Override
    protected void onPause() {
        super.onPause();
        mp.pause();//BGMの一時停止
    }

    // アプリ終了時に実行
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();//メモリの解放
        mp = null; //音楽を破棄
    }

    /** ListViewに表示する名前リストをセット */
    public void setNameList(ArrayList<ArrayList<String>> arrayLists) {
        ArrayList<String> list = new ArrayList<>();
        // ArrayListから名前を取得
        for (int i = 0; i < arrayLists.size(); i++) {
            list.add(arrayLists.get(i).get(0)); //名前を取得
        }

        this.nameList = list.toArray(new String[list.size()]);
    }

    /** 別スレッドでファイルをダウンロードするクラス */
    public class FileDownloadThread extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>> {
        public ProgressDialog target;
        private Context context;
        /** ダウンロード完了したファイルをカウントする変数 */
        private int count = 0;

        public FileDownloadThread(Context context) {
            this.context = context;
        }

        /** AsyncTask.execute()呼び出し後、実行される */
        @Override
        protected ArrayList<ArrayList<String>> doInBackground(Void... voids) {
            // Firebaseからファイルをダウンロード
            fileDownloader(context);

            // ファイルダウンロードが完了するまでスレッドを停止
            waitFileDownload();

            // ダウンロードしたファイルから名前&ファイルパスリストを作成
            ArrayList<ArrayList<String>> arrayLists = createNameList(context);

            return arrayLists;
        }

        /** doInBackground()終了後、メインスレッドで実行される */
        @Override
        protected void onPostExecute(final ArrayList<ArrayList<String>> arrayLists) {
            // プログレスダイアログの表示終了
            if (target != null) {
                target.dismiss();
            }
            // 名前リストを生成
            setNameList(arrayLists);

            // ListViewのインスタンスを生成
            ListView listView = findViewById(R.id.list_view);

            // BaseAdapter を継承したadapterのインスタンスを生成
            // レイアウトファイル list.xml を activity_manager.xml に
            // inflate するためにadapterに引数として渡す
            BaseAdapter adapter = new ListViewAdapter(context, R.layout.list, nameList);

            // ListViewにadapterをセット
            listView.setAdapter(adapter);

            //リスト項目が選択された時のイベントを追加
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // clickされたpositionを元にファイルパスを取得
                    String filePath = arrayLists.get(position).get(1);

                    // インテントにセット
                    Intent intent = new Intent(context, ManagerSubActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_PATH, filePath);

                    // Activity をスイッチする
                    startActivity(intent);
                }
            });
        }

        /** FirebaseからCSVファイルをダウンロードする関数 */
        private void fileDownloader(Context context) {
            // Firebaseを使うための参照を作成
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference listRef = storage.getReference().child(Constants.FIREBASE_FILE_PATH);

            // ダウンロードしたファイルは内部ストレージに保存する
            final String filePath = context.getFilesDir().getPath();
            File downloadDir = new File(filePath, Constants.DOWNLOAD_PATH);
            if (!downloadDir.exists()) { // フォルダがなければ作成する
                downloadDir.mkdir();
            }

            // すべてのファイルのリストを取得する
            listRef.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(final ListResult listResult) {
                            for (StorageReference item : listResult.getItems()) {
                                // ダウンロード先を内部ストレージに指定
                                File localFile = new File(
                                        filePath + "/" + Constants.DOWNLOAD_PATH, item.getName());

                                // ファイルをダウンロード
                                FileDownloadTask downloadTask = item.getFile(localFile);
                                downloadTask.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                    // ファイルダウンロードが完了したら実行される
                                    @Override
                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            count++;
                                            if (count == listResult.getItems().size()) {
                                                // すべてのファイルのダウンロードが完了したら停止しているスレッドを再開
                                                synchronized (FileDownloadThread.this) {
                                                    FileDownloadThread.this.notifyAll();
                                                }
                                                // カウンターをリセット
                                                count = 0;
                                            }
                                        }
                                    }
                                });


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // エラー処理を書く
                        }
                    });
        }

        /** Firebaseからのファイルダウンロード完了まで待つ関数 */
        synchronized private void waitFileDownload() {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /** ダウンロードしたファイル一覧から名前のリストを作成する関数 */
        private ArrayList<ArrayList<String>> createNameList(Context context) {
            ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();

            // フィルタを作成する
            FilenameFilter filter = new FilenameFilter(){
                public boolean accept(File file, String str){
                    // 指定文字列でフィルタする
                    if(str.indexOf(".csv") != -1) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            // ファイル一覧を取得する
            File downloadDir = new File(context.getFilesDir().getPath(), Constants.DOWNLOAD_PATH);
            File[] list = downloadDir.listFiles(filter);

            // 1ファイルずつ読み込む
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isFile()) { // ファイルの場合
                        String userName = null;
                        try {
                            // CSVファイル読み込みの準備
                            InputStream inputStream = new FileInputStream(list[i]);
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String lineBuffer;

                            // CSVファイル読み込み
                            while ((lineBuffer = bufferedReader.readLine()) != null) {
                                // カンマ区切りで1つずつ配列に入れる
                                String[] rowData = lineBuffer.split(",");
                                // 名前を取得する
                                userName = rowData[1];
                            }
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 名前とファイル名をセットにする
                        ArrayList<String> dataList = new ArrayList<String>();
                        dataList.add(userName);
                        dataList.add(list[i].toString());

                        arrayLists.add(dataList);
                    }
                }
            }
            return arrayLists;
        }
    }

}