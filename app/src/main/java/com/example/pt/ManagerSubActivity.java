package com.example.pt;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerSubActivity extends AppCompatActivity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        mp = MediaPlayer.create(this, R.raw.bgm_5);
        mp.setLooping(true);//BGMのループ
        mp.start();//BGMの再生

        Intent intent = getIntent();
        String selectedText = intent.getStringExtra("Text");

        LinearLayout mainLayout = findViewById(R.id.list_item);
        com.example.pt.CsvReader parser = new com.example.pt.CsvReader();
        parser.reader(getApplicationContext(),selectedText);
        ListViewAdapter2 listViewAdapter2 = new ListViewAdapter2(this, R.layout.list_item, parser.objects);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(listViewAdapter2);

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