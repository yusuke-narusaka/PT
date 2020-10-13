package com.example.pt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {
    private SharedPreferences dataStore;
    private EditText editTextName;
    private EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // "DataStore"という名前でインスタンスを生成
        dataStore = getSharedPreferences(Constants.SETTING_NAME, MODE_PRIVATE);

        editTextName = findViewById(R.id.userName);
        editTextNumber = findViewById(R.id.employeeNumber);

        //ユーザー登録ボタンを押したときの処理
        Button buttonWrite = findViewById(R.id.userRegistButton);
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String textName = editTextName.getText().toString();
                String textNumber = editTextNumber.getText().toString();
                // 入力文字列を"input"に書き込む
                SharedPreferences.Editor editorName = dataStore.edit();
                SharedPreferences.Editor editorNumber = dataStore.edit();
                editorName.putString(Constants.KEY_NAME, textName);
                editorNumber.putString(Constants.KEY_EMPLOYEE_NUMBER, textNumber);
                editorName.commit();
                editorNumber.commit();
                finish();
            }
        });

    }
}