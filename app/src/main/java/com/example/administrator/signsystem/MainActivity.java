package com.example.administrator.signsystem;

import android.content.Intent;
//import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLoginButton();
        setRegisterButton();

//        SharedPreferences sf = getSharedPreferences("file_name", 0);
//        SharedPreferences.Editor editor = sf.edit();
    }

    private void setRegisterButton() {
        Button button = this.findViewById(R.id.registerBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Register.class );
                startActivity(intent);
            }
        });
    }

    private void setLoginButton(){
        Button button = this.findViewById(R.id.loginBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Login.class );
                startActivity(intent);
            }
        });
    }
}
