package com.example.pt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class GameActivityResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        // 音声データを読み込む
        SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundPool.load(getApplicationContext(), R.raw.levelup, 0);

        //音楽を鳴らす
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (0 == status) {
                    soundPool.play(sampleId, 3.0F, 3.0F, 0, 0, 1.0F);
                }
            }
        });


        //ランダムな背景画像を設定
        String gamePoint = setBackGroundView();

        SharedPreferences data = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        final String storeName = data.getString("input",null );

        String filename = storeName + "_data.csv";
        String output = gamePoint + "\n";
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

        Uri file = Uri.fromFile(new File("data/data/com.example.pt/files/" + storeName + "_data.csv"));
        StorageReference riversRef = storageRef.child("CsvFiles/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        //イメージボタンに設定したid属性から、もう一度のイメージボタンの情報を取得する
        ImageButton buttonRetry = findViewById(R.id.buttonRetry);

        //イメージボタンの情報とボタンがクリックされたときの処理を関連付ける
        buttonRetry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //GameActivityに遷移するように設定
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);

                //インテントの開始
                startActivity(intent);

                //GameActivityResultの終了
                finish();

            }
        });
    }

    //おみくじの結果をランダムに出力し、背景画像と文字列として画面に表示するメソッド
    private String setBackGroundView() {

       //イメージビューに設定したid属性から、イメージビューの情報を取得する
       ImageView imageView = findViewById(R.id.gameResult);

       //おみくじ用の画像IDを格納する配列
      //{大吉：０、中吉：１、小吉：２、吉：３、末吉：４、凶：５}
       final int[] omikujiResult =
       {R.drawable.omikuji_daikichi, R.drawable.omikuji_chukichi, R.drawable.omikuji_shokichi,
               R.drawable.omikuji_kichi, R.drawable.omikuji_suekichi, R.drawable.omikuji_kyo};

       //ランダムな数字を出力する
       Random random = new Random();
       int randNum = random.nextInt(omikujiResult.length);

       //ゲームの結果によりゲットするポイントを入れる変数を宣言
       String gamePoint = null;

       //イメージビューにおみくじの画像をセットする
       imageView.setBackgroundResource(omikujiResult[randNum]);

        TextView textView = findViewById(R.id.resultText);
        //上でランダムに出力した変数の値（randNum）によって表示する文字列を変える処理
        switch (randNum) {
            //大吉（０）だった時
            case 0:
                textView.setText(R.string.result_daikichi);
                String resultDaikichi = textView.getText().toString();
                Toast.makeText(this, "10ポイントゲット！！", Toast.LENGTH_LONG).show();
                gamePoint = "10";
                return gamePoint;
            //中吉（１）だった時
            case 1:
                textView.setText(R.string.result_chukichi);
                String resultChukichi = textView.getText().toString();
                Toast.makeText(this, "8ポイントゲット！！", Toast.LENGTH_LONG).show();
                gamePoint = "08";
                return gamePoint;
            //小吉（２）だった時
            case 2:
                textView.setText(R.string.result_shokichi);
                String resultShokichi = textView.getText().toString();
                Toast.makeText(this, "6ポイントゲット！！", Toast.LENGTH_LONG).show();
                gamePoint = "06";
                return gamePoint;
            //吉（３）だった時
            case 3:
                textView.setText(R.string.result_kichi);
                String resultKichi = textView.getText().toString();
                Toast.makeText(this, "4ポイントゲット！！", Toast.LENGTH_LONG).show();
                gamePoint = "04";
                return gamePoint;
            //末吉（４）だった時
            case 4:
                textView.setText(R.string.result_suekichi);
                String resultSuekichi = textView.getText().toString();
                Toast.makeText(this, "2ポイントゲット！！", Toast.LENGTH_LONG).show();
                gamePoint = "02";
                return gamePoint;
            //凶（５）だった時
            case 5:
                textView.setText(R.string.result_kyo);
                String resultKyo = textView.getText().toString();
                Toast.makeText(this, "残念、0ポイント！！次回に期待しよう！！", Toast.LENGTH_LONG).show();
                gamePoint = "00";
                return gamePoint;
        }

        return gamePoint;
    }

}