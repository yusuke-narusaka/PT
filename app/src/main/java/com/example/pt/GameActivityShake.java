package com.example.pt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import java.util.List;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;

public class GameActivityShake extends AppCompatActivity implements SensorEventListener {
    //変数宣言
    private static final int LOWEST_SPEED = 3000;  //1カウントに必要な端末の最低速度
    private static final int SHAKE_TIMEOUT = 1000;    //端末が最低速度で振られるまでの時間
    private static final int SHAKE_COUNT = 10; //端末が振るのに必要なカウント数
    private int shakeCount = 0;  //端末が振られたカウント数
    private long lastTimeDetectAcceleration = 0;    //一番最後に端末が最低速度以上の速度で振られた時間
    private float xDimen = 0;  //端末が一番最後に振られたときのX座標の位置
    private float yDimen = 0;  //端末が一番最後に振られたときのY座標の位置
    private float zDimen = 0;  //端末が一番最後に振られたときのZ座標の位置
    private SensorManager manager;//位置情報を取得する
    private Vibrator vibrator;//振動を感知する
    private MediaPlayer mp1;//BGM1
    private MediaPlayer mp2;//BGM2
    private MediaPlayer mp3;//BGM3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_shake);

        //３つのBGMを順番に流すために、各BGMの変数をインスタンス化
        mp1 = MediaPlayer.create(this, R.raw.shake_sound);
        mp2 = MediaPlayer.create(this, R.raw.japanese_boise);
        mp3 = MediaPlayer.create(this, R.raw.drum_japanese);

        //アクティビティ開始後すぐにBGM1再生開始
        mp1.start();

        //BGM2を1秒待機後に再生開始
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mp2.start();
            }
        }, 1000);

        //BGM3を6秒待機後に再生開始
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mp3.start();
            }
        }, 6000);

        //センサーサービスを利用する
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //バイブレーションのサービスを利用する
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //加速度センサーの値を取得する
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if(sensors.size() > 0) {
            Sensor s = sensors.get(0);
            //リスナーの登録
            manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    protected void onPause(){
        super.onPause();

        //イベントリスナーの登録解除
        if(manager != null){
            manager.unregisterListener(this);
        }
    }

    /*
     * 端末が動いたときに呼び出される処理
     *  @param event : 端末の座標
     *  @return なし
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // 現在の時間をチェック
        long now = System.currentTimeMillis();

        // 現在の時間と最後に加速度を検知した時間の差を計算する
        long diffTime = now - lastTimeDetectAcceleration;

        // 一定時間を越えていたとき
        if(diffTime > SHAKE_TIMEOUT){

            //カウントをリセットする
            shakeCount = 0;
        }

        //端末の座標位置を取得する
        float x = event.values[0];  //x座標
        float y = event.values[1];  //y座標
        float z = event.values[2];  //z座標

        //振られている端末の縦の速度を取得
        float speed = Math.abs(x + y + z - xDimen - yDimen - zDimen) / diffTime * 10000;

        //端末が動いた速度が一定速度よりも速い場合
        if(speed > LOWEST_SPEED){

            //バイブレーションを振動
            vibrator.vibrate(40);

            //カウントを1回増やす
            ++shakeCount;

            //端末のカウントが一定回数を超えた場合
            if(shakeCount >= SHAKE_COUNT){

                //バイブレーションを振動
                vibrator.vibrate(1000);

                //SwipeActivityに遷移するように設定
                Intent intent = new Intent(getApplicationContext(),GameActivitySwipe.class);

                //インテントの開始
                startActivity(intent);

                //ShakeActivityの終了
                finish();
            }

        }

        //一番最後に端末が振られたときの時間と位置をセット
        lastTimeDetectAcceleration = now;
        xDimen = x;
        yDimen = y;
        zDimen = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}