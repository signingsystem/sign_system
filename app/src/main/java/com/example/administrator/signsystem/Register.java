package com.example.administrator.signsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import web.WebService;

public class Register extends AppCompatActivity {
    //注册按钮
    Button regBtn;
    EditText regUser, regPass, reRegPass;
    //创建等待框
    private ProgressDialog dialog;
    //返回主线程更新的数据
    private static Handler handler;
    //接受服务器返回数据
    String info;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        handler = new Handler();

        regBtn = findViewById(R.id.register);
        regUser = findViewById(R.id.registerUser);
        regPass = findViewById(R.id.registerPass);
        reRegPass = findViewById(R.id.rePass);

        addOnClickListener();
        setBackButton();
    }

    private void setBackButton() {
        ImageButton imageButton = this.findViewById(R.id.registerReturnBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,MainActivity.class );
                startActivity(intent);
            }
        });
    }

    public void addOnClickListener(){
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkNetwork()){
                    Toast toast=Toast.makeText(Register.this, "网络未连接", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                //提示框
                dialog = new ProgressDialog(Register.this);
                dialog.setTitle("提示");
                dialog.setMessage("正在注册，请稍后...");
                dialog.setCancelable(false);
                dialog.show();
                //创建子线程，分别进行Get和Post传输
                new Thread(new MyThread()).start();
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
            info = WebService.executeRegisterHttpGet(regUser.getText().toString(),regPass.getText().toString());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Toast toast;
                    if(info.equals("true")){
                        toast=Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT);
//                        Intent intent = new Intent(Register.this,Takephoto.class );
//                        startActivity(intent);

                    }else{
                        toast=Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
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
