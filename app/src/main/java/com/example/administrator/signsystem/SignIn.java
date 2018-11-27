package com.example.administrator.signsystem;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private LocationManager locationManager;
    private String provider;
    private TextView Latitu,Longtitu;
    private double x,y;
    private CharSequence lx,ly;//用来储存坐标并放置在TextView上
    private List<String> list;//用来储存可以使用的定位组件
    private Button Sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initView();

        Sign.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        list = locationManager.getProviders(true);
        SignLocation();
    }

    private void initView(){
        Latitu=(TextView)findViewById(R.id.latitu);
        Longtitu=(TextView)findViewById(R.id.longtitu);
        Sign=(Button)findViewById(R.id.sign);
    }

    private void SignLocation(){
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(this, "请允许定位权限", Toast.LENGTH_LONG).show();
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
            x=location.getLatitude();
            y=location.getLongitude();
        }
        else{
            x=0;
            y=0;
        }
        lx=String.valueOf(x);
        ly=String.valueOf(y);
        Latitu.setText(lx);
        Longtitu.setText(ly);
    }

    private boolean judge(){
        double distance=1;//范围1千米
        double Bjupt_x=40.10891;//北邮宏福校区坐标
        double Bjupt_y=116.375299;
        double dx=2*Math.asin(Math.sin(distance/(2*6371))/Math.cos(degToRad(Bjupt_y)));
        dx=radToDeg(dx);

        double dy=distance/6371;
        dy=radToDeg(dy);

        Coord left_top=new Coord(Bjupt_x-dx,Bjupt_y+dy);
        Coord right_top=new Coord(Bjupt_x+dx,Bjupt_y+dy);
        Coord left_bottom=new Coord(Bjupt_x-dx,Bjupt_y-dy);
        Coord right_bottom=new Coord(Bjupt_x+dx,Bjupt_y-dy);
        System.out.println("left_top:"+left_top.returnX()+" "+left_top.returnY());
        System.out.println("right_top:"+right_top.returnX()+" "+right_top.returnY());
        System.out.println("left_bottom:"+left_bottom.returnX()+" "+left_bottom.returnY());
        System.out.println("right_bottom:"+right_bottom.returnX()+" "+right_bottom.returnY());
        if(x<left_top.returnX()&&x>right_top.returnX()
                &&y<left_top.returnY()&&y>left_bottom.returnY()){
            return true;
        }
        else{
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

    private double degToRad(double degree){

        return degree*Math.PI/180.0;
    }

    private double radToDeg(double radian){

        return radian*180/Math.PI;
    }
}