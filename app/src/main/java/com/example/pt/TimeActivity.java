package com.example.pt;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TimeActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        mp = MediaPlayer.create(this, R.raw.bgm_5);
        mp.setLooping(true);//BGMのループ
        mp.start();//BGMの再生

        SharedPreferences dataStore = getSharedPreferences(Constants.SETTING_NAME, Context.MODE_PRIVATE);
        final String employeeNumber = dataStore.getString(Constants.KEY_EMPLOYEE_NUMBER,null );

        // クリックイベントを取得したいボタン
        Button startButton = findViewById(R.id.start_button);
        Button closeButton = findViewById(R.id.close_button);
        Button changeNameButton = findViewById(R.id.changename_button);

        //「始業」ボタンが押されたとき
        startButton.setOnClickListener(new View.OnClickListener() {

            // クリック時に呼ばれるメソッド
            @Override
            public void onClick(View view) {
                //始業時間をSharedPreferencesに保存する
                CsvController csvCtrl = new CsvController();
                csvCtrl.saveStartTime(getApplicationContext());
                Context context = getApplicationContext();
                Toast.makeText(context, "今日も元気に頑張っていきましょう！！", Toast.LENGTH_LONG).show();
            }

        });

        //「終業」ボタンが押されたとき
        closeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //終業時間をSharedPreferencesに保存する
                CsvController csvCtrl = new CsvController();
                csvCtrl.saveEndTime(getApplicationContext());

                //CSVファイルに書き出し
                csvCtrl.saveFile(getApplicationContext());

                //firebaseにファイル保存
                csvCtrl.uploadFile(getApplicationContext());
                Context context = getApplicationContext();
                Toast.makeText(context, "本日もお仕事お疲れさまでした！！", Toast.LENGTH_LONG).show();
            }

        });

        changeNameButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                SharedPreferences dataStore = getSharedPreferences(Constants.SETTING_NAME, MODE_PRIVATE);
                editText = (EditText)findViewById(R.id.userName);
                String textName = editText.getText().toString();
                SharedPreferences.Editor editorName = dataStore.edit();
                editorName.putString(Constants.KEY_NAME, textName);
                editorName.commit();
                Context context = getApplicationContext();
                Toast.makeText(context, "ユーザー名が変更されました", Toast.LENGTH_LONG).show();
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