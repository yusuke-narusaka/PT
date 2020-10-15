package com.example.pt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class PasswordActivity extends AppCompatActivity {
    private SharedPreferences dataStore;
    private EditText editTextPassword;
    private EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        editTextPassword = findViewById(R.id.Password);
        //ユーザー登録ボタンを押したときの処理
        Button buttonWrite = findViewById(R.id.PasswordButton);
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String textPassword = editTextPassword.getText().toString();
                if(textPassword.equals("abcd")){
                    Intent intent = new Intent(getApplication(), ManagerActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"パスワードが間違っています", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}