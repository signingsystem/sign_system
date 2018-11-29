package com.example.administrator.signsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private LocationManager locationManager;
    private TextView current;
    private double x,y;
    private CharSequence text;//用来储存坐标并放置在TextView上
    private Button Sign;
    private static final String TAG="GpsActivity";
    private TextView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initView();

        Sign.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        openGPSSettings();
        returnLocation();
    }

    private void initView(){
        Sign=(Button)findViewById(R.id.signButton);
        current=(TextView)findViewById(R.id.currentXY);
    }
    private void openGPSSettings(){//判断是否开启权限，以及打开权限
        LocationManager alm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)){
            Toast.makeText(this,"GPS normal",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Please open GPS!",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0);
    }


    private void returnLocation() {
        // 获取位置管理服务
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            Log.d(TAG, "GPS_PROVIDER is null.");
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//换成网络定位
        }
        x=location.getLatitude();
        y=location.getLongitude();
        String coordinate="经度："+y+" 纬度："+x;
        text=coordinate;
        current.setText(text);
    }


        private boolean judge() {
            double distance = 1;//范围1千米
            double Bjupt_x = 40.10891;//北邮宏福校区坐标
            double Bjupt_y = 116.375299;
            double dx = 2 * Math.asin(Math.sin(distance / (2 * 6371)) / Math.cos(degToRad(Bjupt_y)));
            dx = radToDeg(dx);

            double dy = distance / 6371;
            dy = radToDeg(dy);

            Coord left_top = new Coord(Bjupt_x - dx, Bjupt_y + dy);
            Coord right_top = new Coord(Bjupt_x + dx, Bjupt_y + dy);
            Coord left_bottom = new Coord(Bjupt_x - dx, Bjupt_y - dy);
            Coord right_bottom = new Coord(Bjupt_x + dx, Bjupt_y - dy);
            System.out.println("left_top:" + left_top.returnX() + " " + left_top.returnY());
            System.out.println("right_top:" + right_top.returnX() + " " + right_top.returnY());
            System.out.println("left_bottom:" + left_bottom.returnX() + " " + left_bottom.returnY());
            System.out.println("right_bottom:" + right_bottom.returnX() + " " + right_bottom.returnY());
            if (x < left_top.returnX() && x > right_top.returnX()
                    && y < left_top.returnY() && y > left_bottom.returnY()) {
                return true;
            } else {
                return false;
            }

        }

    @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.signButton){
                        if(judge()){
                            Toast.makeText(this, "签到成功", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "你不在签到范围内", Toast.LENGTH_LONG).show();
            }
        }
    }

        private double degToRad(double degree) {

            return degree * Math.PI / 180.0;
        }

        private double radToDeg(double radian) {

            return radian * 180 / Math.PI;
        }
    }