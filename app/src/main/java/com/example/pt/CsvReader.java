package com.example.pt;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CsvReader extends AppCompatActivity {
    List<ListData> objects = new ArrayList<ListData>();
    String fileName = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;

    public void reader(Context context, String selectedText) {

        AssetManager assetManager = context.getResources().getAssets();
        try {

            if (selectedText.equals("福地家宏")){
                fileName = "data_fukuchi.csv";
            } else if (selectedText.equals("石神国子")){
                fileName = "data_ishigami.csv";
            } else if (selectedText.equals("松井貴博")){
                fileName = "data_matsui.csv";
            } else if (selectedText.equals("表もこみち")){
                fileName = "data_omote.csv";
            } else if (selectedText.equals("椎名裕美子")){
                fileName = "data_shiina.csv";
            } else if (selectedText.equals("杉本高文")){
                fileName = "data_sugimoto.csv";
            } else if (selectedText.equals("菅生大将")){
                fileName = "data_sugou.csv";
            } else if (selectedText.equals("駿河学")){
                fileName = "data_suruga.csv";
            } else if (selectedText.equals("竹村桐子")){
                fileName = "data_takemura.csv";
            } else if (selectedText.equals("寺西修")){
                fileName = "data_teranishi.csv";
            } else if (selectedText.equals("ダウンロード")){
                this.fileDownloader();
            }

            // CSVファイルの読み込み
            //String relativePath = "data_fukuchi" + "*.csv";
            //String filesDirectoryPath = context.getFilesDir().getPath();
            //String absolutePath = context.getCacheDir().getPath() + "/" + relativePath;
            //FileInputStream fs = new FileInputStream(absolutePath);
            //InputStreamReader inputStreamReader = new InputStreamReader(fs);
            InputStream inputStream = assetManager.open(String.valueOf(fileName));
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
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileDownloader() {

        //ダウンロード処理
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef;
        islandRef = storageRef.child("CsvFiles/sutou kuniyuki_data.csv");

        File localFile = null;
        try {
            localFile = File.createTempFile("sutou kuniyuki", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}