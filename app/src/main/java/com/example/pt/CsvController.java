package com.example.pt;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** CSVファイルを作成、更新、読込するクラス */
public class CsvController {

    /** CSVファイル名を作成する関数 */
    private String getCsvFileName(Context context) {
        // SharedPreferencesからidを取得
        SharedPreferences preferences = context.getSharedPreferences(Constants.SETTING_NAME, Context.MODE_PRIVATE);
        String id = preferences.getString(Constants.KEY_EMPLOYEE_NUMBER, "");

        // ファイル名を作成
        String fileName = id + ".csv";

        return fileName;
    }

    /** 始業時間をSharedPreferencesに保存する関数 */
    public void saveStartTime(Context context) {
        // 現在時刻を取得
        Date date = new Date(System.currentTimeMillis());

        // SharedPreferencesに始業時間を保存
        SharedPreferences preferences = context.getSharedPreferences(Constants.SETTING_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_START_TIME, new SimpleDateFormat("HH:mm").format(date));
        editor.commit();
    }


    /** 終業時間をSharedPreferencesに保存する関数 */
    public void saveEndTime(Context context){
        // 現在時刻を取得
        Date date = new Date(System.currentTimeMillis());

        // SharedPreferencesに終業時間を保存
        SharedPreferences preferences = context.getSharedPreferences(Constants.SETTING_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_END_TIME, new SimpleDateFormat("HH:mm").format(date));
        editor.commit();
    }

    /** ゲームの獲得ポイントをSharedPreferencesに保存する関数 */
    public void savePoint(Context context, String point) {
        // SharedPreferencesにゲームの獲得ポイントを保存
        SharedPreferences preferences = context.getSharedPreferences(Constants.SETTING_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_GAME_POINT, point);
        editor.commit();
    }

    /** CSVファイルにデータを保存する関数 */
    public void saveFile(Context context) {
        String[] csvData = new String[5];
        String output = "";
        FileOutputStream outputStream;

        //ファイル名作成
        String fileName = this.getCsvFileName(context);

        // 現在時刻を取得
        Date date = new Date(System.currentTimeMillis());

        //SharedPreferencesから値を読み込み
        SharedPreferences preferences = context.getSharedPreferences(Constants.SETTING_NAME, Context.MODE_PRIVATE);
        csvData[0] = new SimpleDateFormat("yyyy/MM/dd").format(date); //日付
        csvData[1] = preferences.getString(Constants.KEY_NAME, "-"); //名前
        csvData[2] = preferences.getString(Constants.KEY_START_TIME, "-"); //始業時間
        csvData[3] = preferences.getString(Constants.KEY_END_TIME, "-"); //終業時間
        csvData[4] = preferences.getString(Constants.KEY_GAME_POINT, "0");//獲得ポイント

        //書き込むcsvデータを作成
        for (int i = 0; i < csvData.length; i++) {
            output = output + csvData[i];
            if (i == (csvData.length - 1)) {
                //配列の最後の要素だったら改行を追加
                output = output + "\n";
            } else {
                output = output + ",";
            }
        }

        try {
            //ファイル書き込み(ファイルがあると追記される)
            outputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //SharedPreferencesから値を削除
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.KEY_START_TIME);
        editor.remove(Constants.KEY_END_TIME);
        editor.remove(Constants.KEY_GAME_POINT);
        editor.commit();
    }

    /** CSVファイルにデータを保存する関数 */
    public void uploadFile(Context context){
        //firebaseと連携する準備
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        //ファイル名作成
        String fileName = this.getCsvFileName(context);

        //firebaseのStorageにファイルをアップロード
        Uri file = Uri.fromFile(new File(context.getFilesDir().getPath() + "/" + fileName));
        StorageReference riversRef = storageRef.child(Constants.FIREBASE_FILE_PATH + "/" +file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
    }
}
