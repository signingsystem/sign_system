package com.example.administrator.signsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import web.WebService;
import web.WebServicePost;

public class Login extends AppCompatActivity{
    //登陆按钮
    private Button loginBtn;
    //调试文本，注册按钮
    private TextView infotv;
    private Button register;
    //显示用户名和密码
    private EditText username, password;
    //创建等待框
    private ProgressDialog dialog;
    //返回的数据
    private String info;
    //返回主线程更新的数据
    private static Handler handler;

    private Toast toast;
    private SharedPreferences sf;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        handler = new Handler();

        //获取控件
        username = findViewById(R.id.user);
        password = findViewById(R.id.pass);
        loginBtn = findViewById(R.id.loginButton);
        infotv = findViewById(R.id.info);
        register = findViewById(R.id.quick_registerBtn);

        setBtnOnClickListener();
    }

    public void setTextView(){
        TextView usernameTV = this.findViewById(R.id.username);
        TextView pwdTV = this.findViewById(R.id.password);
        usernameTV.setEnabled(false);//设置不可点击
        pwdTV.setEnabled(false);//设置不可点击
    }



    public void setBtnOnClickListener(){
        //设置登录返回图片按钮属性
        ImageButton lgReturnBtn = this.findViewById(R.id.loginReturnBtn);
        lgReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //设置登录界面登录按钮监听器
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkNetwork()){
                    Toast toast=Toast.makeText(Login.this, "网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                //提示框
                dialog = new ProgressDialog(Login.this);
                dialog.setTitle("提示");
                dialog.setMessage("正在登录，请稍后...");
                dialog.setCancelable(false);
                dialog.show();
                //创建子线程，分别进行Get和Post传输
                new Thread(new MyThread()).start();
            }
        });

        //设置登录界面注册按钮监听器
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
    }

    //检查网络
     private boolean checkNetwork(){
         ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         if(connectivityManager.getActiveNetworkInfo() != null){
             return connectivityManager.getActiveNetworkInfo().isAvailable();
         }
        return false;
     }

     //子线程接受数据，主线程修改数据
     public class MyThread implements Runnable{
         @Override
         public void run() {
            info = WebService.executeLoginHttpGet(username.getText().toString(),password.getText().toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    infotv.setText(info);
                    dialog.dismiss();
                    Intent intent = new Intent(Login.this,LoginPhoto.class);
                    intent.putExtra("name",username.getText().toString());//传递username
                    startActivity(intent);

                    /*try {s
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent*/
                }
            });
         }
     }
}
