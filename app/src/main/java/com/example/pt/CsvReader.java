package com.example.pt;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader extends AppCompatActivity {
    List<ListData> objects = new ArrayList<ListData>();
    String fileName = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    File cacheFile;

    public void reader(Context context, String selectedText) {

        try {

            if (selectedText.equals("福地家宏")){
                fileName = "16008.csv";
            } else if (selectedText.equals("石神国子")){
                fileName = "20009.csv";
            } else if (selectedText.equals("松井貴博")){
                fileName = "35418.csv";
            } else if (selectedText.equals("表もこみち")){
                fileName = "11247.csv";
            } else if (selectedText.equals("椎名裕美子")){
                fileName = "33984.csv";
            } else if (selectedText.equals("杉本高文")){
                fileName = "24242.csv";
            } else if (selectedText.equals("菅生大将")){
                fileName = "17426.csv";
            } else if (selectedText.equals("駿河学")){
                fileName = "43012.csv";
            } else if (selectedText.equals("竹村桐子")){
                fileName = "34715.csv";
            } else if (selectedText.equals("寺西修")){
                fileName = "22987.csv";
            } else if (selectedText.equals("ダウンロード")){
                this.fileDownloader(context);
                this.createNameList(context);
            }

            // CSVファイルの読み込み
            if (!selectedText.equals("ダウンロード")){
                String fileName = "data_fukuchi".concat("*").concat(".csv");
                cacheFile = new File(context.getCacheDir(),fileName);
                FileReader filereader = new FileReader(cacheFile);
                BufferedReader bufferReader = new BufferedReader(filereader);

                String line;
                while ((line = bufferReader.readLine()) != null) {

                    //カンマ区切りで１つづつ配列に入れる
                    ListData data = new ListData();
                    String[] RowData = line.split(",");

                    //CSVの左([0]番目)から順番にセット
                    data.setDate(RowData[0]);
                    data.setName(RowData[1]);
                    data.setStarttime(RowData[2]);
                    data.setClosingtime(RowData[3]);
                    data.setGetpoint(RowData[4]);

                    objects.add(data);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** FirebaseからCSVファイルをダウンロードする関数 */
    private void fileDownloader(Context context) {
        // Firebaseを使うための参照を作成
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
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // ダウンロード先を内部ストレージに指定
                            File localFile = new File(
                                    filePath + "/" + Constants.DOWNLOAD_PATH, item.getName());
                            // ファイルをダウンロード
                            item.getFile(localFile);
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
        File[] list = downloadDir.listFiles();

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