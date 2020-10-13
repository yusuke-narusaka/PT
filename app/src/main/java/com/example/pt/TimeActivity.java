package com.example.pt;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;

public class TimeActivity extends AppCompatActivity {
    private MediaPlayer mp;
    Context context;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        mp = MediaPlayer.create(this, R.raw.bgm_5);
        mp.setLooping(true);//BGMのループ
        mp.start();//BGMの再生

        SharedPreferences data = getSharedPreferences("EmployeeNumber", Context.MODE_PRIVATE);
        final String employeeNumber = data.getString("inputNumber",null );

        // クリックイベントを取得したいボタン
        Button startButton = findViewById(R.id.start_button);
        Button closeButton = findViewById(R.id.close_button);

// ボタンに OnClickListener インターフェースを実装する
        startButton.setOnClickListener(new View.OnClickListener() {

            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                Time time = new Time("Asia/Tokyo");
                time.setToNow();
                String date = time.year + "/" + (time.month+1) + "/" + time.monthDay;
                String nowTime = time.hour + ":" + time.minute;

                //CSV書き出し処理
                String filename = employeeNumber + "_data.csv";
                String output = date + "," + employeeNumber + "," + nowTime + ",";
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(output.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //CSVアップロード処理
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                Uri file = Uri.fromFile(new File("data/data/com.example./files/" + employeeNumber + "_data.csv"));
                StorageReference riversRef = storageRef.child("CsvFiles/" + file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

            }

        });

        closeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Time time = new Time("Asia/Tokyo");
                time.setToNow();
                String date = time.year + "/" + (time.month+1) + "/" + time.monthDay;
                String nowTime = time.hour + ":" + time.minute;

                //CSV書き出し処理
                String filename = employeeNumber + ".csv";
                String output = nowTime + ",";
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_APPEND);
                    outputStream.write(output.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //CSVアップロード処理
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                Uri file = Uri.fromFile(new File("data/data/com.example.pt/files/" + employeeNumber + ".csv"));
                StorageReference riversRef = storageRef.child("CsvFiles/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);
            }

        });



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

}