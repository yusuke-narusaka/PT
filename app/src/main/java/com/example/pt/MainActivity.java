package com.example.pt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //BGM再生用の変数宣言
    private MediaPlayer mp;
    //ユーザー名を保存するSharedPreferencesを使うための変数宣言
    private SharedPreferences dataStore;

    //アプリ起動時に最初に実行される処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1回だけ表示される入力画面のチェック用（コメント解除して動かすと必ず入力画面が表示される）
        //カーソルが切り替えたい行にある状態でCtrl + / （ショートカットキー）を押すと、コメント解除・設定の切り替えが簡単にできる
//        Intent intentS = new Intent(getApplication(), InputActivity.class);
//        startActivity(intentS);

        //デバイス・ファイル・エクスプローラーのdata/data/com.example.pt/shared_prefs/DataStore.xml内に保管されている名前を取得するメソッド
        dataStore = getSharedPreferences(Constants.SETTING_NAME, MODE_PRIVATE);
        //input KeyのValueを取得（値が無ければブランク（""）を返す）し、String型の変数employeeNumberStringに代入する
        String employeeNumberString = dataStore.getString(Constants.KEY_EMPLOYEE_NUMBER, "");
        String userNameString = dataStore.getString(Constants.KEY_NAME, "名無し");
        //変数ｓｔｒに名前が代入されていない（＝ブランク）の場合は、名前入力画面（InputActivity）を表示する
        if(employeeNumberString.equals("")) {
            Intent intent = new Intent(getApplication(), InputActivity.class);
            startActivity(intent);
        }

        TextView textView = findViewById(R.id.nameText);
        textView.setText(userNameString + "さん");

        //勤怠入力（猫アイコン）をクリックしたとき
        ImageButton catButton = findViewById(R.id.catButton);
        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TimeActivity.class);
                startActivity(intent);
            }
        });
        //ゲーム（熊アイコン）をクリックしたとき
        ImageButton bearButton = findViewById(R.id.bearButton);
        bearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), GameActivity.class);
                startActivity(intent);
            }
        });
        //マニュアル（うさぎアイコン）をクリックしたとき
        ImageButton rabbitButton = findViewById(R.id.rabbitButton);
        rabbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ManualActivity.class);
                startActivity(intent);
            }
        });
        //管理者用（ペンギンアイコン）をクリックしたとき
        ImageButton penguinsButton = findViewById(R.id.penguinsButton);
        penguinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PasswordActivity.class);
                startActivity(intent);
            }
        });
        //勤怠入力（文字）をクリックしたとき
        TextView timeText = findViewById(R.id.timeText);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TimeActivity.class);
                startActivity(intent);
            }
        });
        //ゲーム（文字）をクリックしたとき
        TextView gameText = findViewById(R.id.gameText);
        gameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), GameActivity.class);
                startActivity(intent);
            }
        });
        //マニュアル（文字）をクリックしたとき
        TextView manualText = findViewById(R.id.manualText);
        manualText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ManualActivity.class);
                startActivity(intent);
            }
        });
        //管理者用（文字）をクリックしたとき
        TextView managerText = findViewById(R.id.managerText);
        managerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), PasswordActivity.class);
                startActivity(intent);
            }
        });

        //リソースファイルのBGMを繰り返し自動再生
        mp = MediaPlayer.create(this, R.raw.bgm_5);
        mp.setLooping(true);//BGMのループ
        mp.start();//BGMの再生
    }

    //画面が表示されるたびに実行
    @Override
    public void onResume() {
        super.onResume();
        mp.start();//BGMの再生
        dataStore = getSharedPreferences(Constants.SETTING_NAME, MODE_PRIVATE);
        //input KeyのValueを取得（値が無ければブランク（""）を返す）し、String型の変数employeeNumberStringに代入する
        String userNameString = dataStore.getString(Constants.KEY_NAME, "名無し");
        TextView textView = findViewById(R.id.nameText);
        textView.setText(userNameString + "さん");

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

    //画面右上のAndroid純正メニューアイコン（・・・が縦に並んだアイコン）がクリックされた時に実行
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    //画面右上のAndroid純正メニューアイコンから各メニューがクリックされた時に実行
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            //勤怠入力を押したとき
            case R.id.menuManager:
                Intent managerIntent = new Intent(getApplication(), PasswordActivity.class);
                startActivity(managerIntent);
                return true;
            //ゲームを押したとき
            case R.id.menuGame:
                Intent gameIntent = new Intent(getApplication(), GameActivity.class);
                startActivity(gameIntent);
                return true;
            //マニュアルを押したとき
            case R.id.menuManual:
                Intent manualIntent = new Intent(getApplication(), ManualActivity.class);
                startActivity(manualIntent);
                return true;
            //管理者用を押したとき
            case R.id.menuTime:
                Intent timeIntent = new Intent(getApplication(), TimeActivity.class);
                startActivity(timeIntent);
                return true;
        }
        return false;
    }

}