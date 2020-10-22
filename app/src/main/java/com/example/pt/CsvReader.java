package com.example.pt;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader extends AppCompatActivity {
    List<ListData> objects = new ArrayList<ListData>();

    public void reader(Context context, String filePath) {
        try {
            // CSVファイルの読み込み
            InputStream inputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferReader.readLine()) != null) {

                //カンマ区切りで１つづつ配列に入れる
                ListData data = new ListData();
                String[] RowData = line.split(",");

                //CSVの左([0]番目)から順番にセット
                data.setDate(RowData[0]);
                data.setName(RowData[1]);
                data.setStarttime(RowData[2]);
                data.setClosingtime(RowData[3]);
                data.setGetpoint(RowData[4]);

                objects.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}