package com.example.administrator.signsystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RegisterPhtot extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 592;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phtot);
        setTakePhotoButton();
        setReturnButton();

    }
    //返回主页面
    private void setReturnButton() {
        Button button = this.findViewById(R.id.returnMainButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPhtot.this,MainActivity.class );
                startActivity(intent);
            }
        });
    }

    private void setTakePhotoButton() {
        Button button = this.findViewById(R.id.takephoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//照相
                startActivityForResult(cameraIntent, CAMERA_REQUEST); //启动照相
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(photo);
            }
        }
    }
}
