package com.example.administrator.signsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import web.UpLoadPhoto;

import static com.example.administrator.signsystem.RegisterPhoto.CAMERA_REQUEST;

public class LoginPhoto extends AppCompatActivity {

    private File mPhotoFile;
    private String mPhotoPath;
    private String username;
    private Intent intent;

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
                Verify verify=new Verify(username);
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
                try {
                    mPhotoPath="/storage/emulated/0/"+getPhotoFileName();
                    mPhotoFile=new File(mPhotoPath);
                    mPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//照相
                startActivityForResult(cameraIntent, CAMERA_REQUEST); //启动照相
                UpLoadPhoto upload=new UpLoadPhoto("0.jpg");//上传照片
            }
        });
    }

    private String getPhotoFileName(){
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
    }
}
