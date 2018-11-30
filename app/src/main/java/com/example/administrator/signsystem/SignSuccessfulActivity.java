package com.example.administrator.signsystem;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

import web.WebService;

public class SignSuccessfulActivity extends AppCompatActivity {
    TextView signDays;
    //返回主线程更新的数据
    private static Handler handler;
    //获取当前签到天数
    int signTimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_successful);

        handler = new Handler();
        signDays = findViewById(R.id.signSumDay);
        new Thread(new MyThread()).start();
    }

    private class MyThread implements Runnable{
        @Override
        public void run() {
            String username = "wdz";/*需要获取当前用户名*/
            signTimes = WebService.getSignDays(username,2);
            //子线程修改数据
            handler.post(new Runnable() {
                @Override
                public void run() {
                    signDays.setText(signTimes);
                }
            });
            }
        }
}
