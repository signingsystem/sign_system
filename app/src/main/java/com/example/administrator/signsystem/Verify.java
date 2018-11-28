package com.example.administrator.signsystem;

import android.support.annotation.Nullable;

import java.io.IOException;

import UpLoadApi.UpLoadPhotoAPI;
import UpLoadApi.VerifyAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Verify {
    private Retrofit retrofit;
    private String URL="http://188.131.169.231:5000";
    private VerifyAPI api;
    private String message;
    private Call<ResponseBody> call;

    public Verify(String username){
        retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api=retrofit.create(VerifyAPI.class);
        call=api.postusername(username);
        message="80";
    }

    public void sendMessage(){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body().string()!=null) {
                        message = response.body().string();
                    }
                    else{
                        message="error";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(message);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){

            }
        });
    }

    public String returnMessage(){
        return message;
    }
}
