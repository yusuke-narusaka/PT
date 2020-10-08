package com.example.pt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {
    private SharedPreferences dataStore;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // "DataStore"という名前でインスタンスを生成
        dataStore = getSharedPreferences("DataStore", MODE_PRIVATE);

        editText = findViewById(R.id.userName);

        //ユーザー登録ボタンを押したときの処理
        Button buttonWrite = findViewById(R.id.userRegistButton);
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String text = editText.getText().toString();
                // 入力文字列を"input"に書き込む
                SharedPreferences.Editor editor = dataStore.edit();
                editor.putString("input", text);
                editor.commit();
                finish();
            }
        });

    }
}