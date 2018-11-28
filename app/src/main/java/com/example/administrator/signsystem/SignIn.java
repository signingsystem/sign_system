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

import java.io.IOException;
import java.util.List;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private LocationManager locationManager;
    private String provider;
    private TextView Latitu,Longtitu;
    private double x,y;
    private CharSequence lx,ly;//用来储存坐标并放置在TextView上
    private List<String> list;//用来储存可以使用的定位组件
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
        list = locationManager.getProviders(true);
        openGPSSettings();
        returnLocation();
        //SignLocation();
    }

    private void initView(){
        Latitu=(TextView)findViewById(R.id.coordinateX);
        Longtitu=(TextView)findViewById(R.id.coordinateY);
        Sign=(Button)findViewById(R.id.signButton);
    }
//
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
//
//    private void SignLocation(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            Toast.makeText(this, "请允许定位权限", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(location==null){
//            Log.d(TAG,"GPS_PROVIDER is null.");
//            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);//换成网络定位
//        }
//        else{
//            Log.d(TAG,"location"+location);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,5,locationListener);
//        }
//
//    }

//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.d(TAG, "onProviderDisabled.location = " + location);
////            updateView(location);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.d(TAG, "onStatusChanged() called with " + "provider = [" + provider + "], status = [" + status + "], extras = [" + extras + "]");
//            switch (status) {
//                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "AVAILABLE");
//                    break;
//                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "OUT_OF_SERVICE");
//                    break;
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "TEMPORARILY_UNAVAILABLE");
//                    break;
//            }
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }

//        private void updateView(Location location) {
//            Geocoder gc = new Geocoder(this);
//            List<Address> addresses = null;
//            String msg = "";
//            Log.d(TAG, "updateView.location = " + location);
//            if (location != null) {
//                try {
//                    addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                    Log.d(TAG, "updateView.addresses = " + addresses);
//                    if (addresses.size() > 0) {
//                        msg += addresses.get(0).getAdminArea().substring(0,2);
//                        msg += " " + addresses.get(0).getLocality().substring(0,2);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                editText.setText("定位到的位置：\n");
//                editText.append(msg);
//                editText.append("\n经度：");
//                editText.append(String.valueOf(location.getLongitude()));
//                editText.append("\n纬度：");
//                editText.append(String.valueOf(location.getLatitude()));
//
//            } else {
//                editText.getEditableText().clear();
//                editText.setText("定位中");
//            }
//        }

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
        lx=String.valueOf(x);
        ly=String.valueOf(y);
        Latitu.setText(lx);
        Longtitu.setText(ly);
    }
//
//    private void updateToNewLocation(Location location) {
//        TextView tv1;
//        tv1 = (TextView)this.findViewById(R.id.tv1);
//        if (location != null) {
//            double latitude = location.getLatitude();
//            double longitude=location.getLongitude();
//            tv1.setText( "维度："+ latitude+ "\n经度" +longitude);
//        } else {
//            tv1.setText("无法获取地理信息" );
//        }
//    }

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
        if(v.getId()==R.id.sign){
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