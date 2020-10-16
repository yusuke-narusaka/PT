package com.example.pt;

/** 定数を定義するクラス */
public class Constants {

    /** SharedPreferencesの設定ファイル名 */
    public static final String SETTING_NAME = "DataStore";

    /** SharedPreferencesのキー */
    /** ユーザー名 */
    public static final String KEY_NAME = "inputName";
    /** 社員番号 */
    public static final String KEY_EMPLOYEE_NUMBER = "inputNumber";
    /** 始業時間 */
    public static final String KEY_START_TIME = "startTime";
    /** 終業時間 */
    public static final String KEY_END_TIME = "endTime";
    /** ゲームの獲得ポイント */
    public static final String KEY_GAME_POINT = "gamePoint";

    /** Firebase Storage */
    /** Firebase Storage内のファイルを保存するフォルダ */
    public static final String FIREBASE_FILE_PATH = "CsvFiles";
    /** Firebase Storageからダウンロードしたファイルを保存するフォルダ */
    public static final String DOWNLOAD_PATH = "Download";
}
