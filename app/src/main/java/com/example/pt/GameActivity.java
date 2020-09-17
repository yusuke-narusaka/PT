package com.example.pt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameActivity extends AppCompatActivity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mp = MediaPlayer.create(this, R.raw.japanese_taste);
        mp.setLooping(true);//BGMのループ
        mp.start();//BGMの再生

        //イメージボタンに設定したid属性から、おみくじを引くのイメージボタンの情報を取得する
        ImageButton buttonStart = (ImageButton) findViewById(R.id.buttonStart);

        //おみくじを引くのイメージボタンの情報とボタンがクリックされたときの処理を関連付ける
        buttonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ShakeActivityに遷移するように設定
                Intent intent = new Intent(getApplicationContext(), GameActivityShake.class);
                //インテントの開始
                startActivity(intent);
                //GameActivityの終了
                finish();
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