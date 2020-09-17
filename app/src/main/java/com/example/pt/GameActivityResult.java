package com.example.pt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
        setBackGroundView();
        //イメージボタンに設定したid属性から、もう一度のイメージボタンの情報を取得する
        ImageButton buttonRetry = (ImageButton) findViewById(R.id.buttonRetry);

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
    private void setBackGroundView() {

       //イメージビューに設定したid属性から、イメージビューの情報を取得する
       ImageView imageView = (ImageView) findViewById(R.id.gameResult);

       //おみくじ用の画像IDを格納する配列
      //{大吉：０、中吉：１、小吉：２、吉：３、末吉：４、凶：５}
       final int[] omikujiResult =
       {R.drawable.omikuji_daikichi, R.drawable.omikuji_chukichi, R.drawable.omikuji_shokichi,
               R.drawable.omikuji_kichi, R.drawable.omikuji_suekichi, R.drawable.omikuji_kyo};

       //ランダムな数字を出力する
       Random random = new Random();
       int randNum = random.nextInt(omikujiResult.length);

       //イメージビューにおみくじの画像をセットする
       imageView.setBackgroundResource(omikujiResult[randNum]);

        TextView textView = (TextView) findViewById(R.id.resultText);
        //上でランダムに出力した変数の値（randNum）によって表示する文字列を変える処理
        switch (randNum) {
            //大吉（０）だった時
            case 0:
                textView.setText(R.string.result_daikichi);
                String resultDaikichi = textView.getText().toString();
                return;
            //中吉（１）だった時
            case 1:
                textView.setText(R.string.result_chukichi);
                String resultChukichi = textView.getText().toString();
                return;
            //小吉（２）だった時
            case 2:
                textView.setText(R.string.result_shokichi);
                String resultShokichi = textView.getText().toString();
                return;
            //吉（３）だった時
            case 3:
                textView.setText(R.string.result_kichi);
                String resultKichi = textView.getText().toString();
                return;
            //末吉（４）だった時
            case 4:
                textView.setText(R.string.result_suekichi);
                String resultSuekichi = textView.getText().toString();
                return;
            //凶（５）だった時
            case 5:
                textView.setText(R.string.result_kyo);
                String resultKyo = textView.getText().toString();
                return;
        }

    }

}