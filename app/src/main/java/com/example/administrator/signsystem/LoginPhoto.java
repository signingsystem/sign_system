package com.example.administrator.signsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


import UpLoadApi.VerifyAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import web.UpLoadPhoto;

import static com.example.administrator.signsystem.RegisterPhoto.CAMERA_REQUEST;

public class LoginPhoto extends AppCompatActivity {

    private File mPhotoFile;
    private String mPhotoPath;
    private String username;
    private Intent intent;
    private Retrofit retrofit;
    private String URL="http://188.131.169.231:5000";
    private VerifyAPI api;
    private Call<ResponseBody> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_photo);
        intent=getIntent();
        username=intent.getStringExtra("name");
        setGetPhotoButton();
        setCancelButton();
        setVerifyButton();
}

    private void setVerifyButton() {
        Button button = this.findViewById(R.id.verify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Verify();
            }
        });
    }

    public void Verify(){//算是一个经验教训，这个地方只能这样写，因为实际上服务器返回值是有延迟的，所以必须当服务器有response的时候，再去进行下一步操作
        retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api=retrofit.create(VerifyAPI.class);
        call=api.postusername(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String message=response.body().string();
                    Double value=Double.valueOf(message.toString());
                    if(value>70){
                        Intent intent = new Intent(LoginPhoto.this,SignIn.class );
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){

            }
        });
    }

    private void setGetPhotoButton() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();//忽视警告
        StrictMode.setVmPolicy(builder.build());
        Button button = this.findViewById(R.id.getPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//照相
                try {
                    mPhotoPath="/storage/emulated/0/"+getPhotoFileName();
                    mPhotoFile=new File(mPhotoPath);
                    mPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //cameraIntent.putExtra("android.intent.extras.CAMERA_FACING",1);//调用前置摄像头，此处应该加一个判断，用户手机是否有前置摄像头
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mPhotoFile));
                startActivityForResult(cameraIntent, CAMERA_REQUEST); //启动照相

            }
        });
    }

    private String getPhotoFileName(){
        intent=getIntent();
        username=intent.getStringExtra("name");
        return username+".jpg";
    }

    private void setCancelButton() {
        Button button = this.findViewById(R.id.cancelButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPhoto.this,Login.class );
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST) {
            Bitmap photo = BitmapFactory.decodeFile(mPhotoPath,null);
            if (photo != null) {
                ImageView imageView = findViewById(R.id.takephoto);
                imageView.setImageBitmap(photo);
            }
        }
        PhotoDispose photodispose=new PhotoDispose(mPhotoPath);
        UpLoadPhoto upload=new UpLoadPhoto(mPhotoPath);//上传照片
    }
}
