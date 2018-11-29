package web;

/**
 * Created by huohu on 2018/11/23.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Date;

public class WebService {
    private static String IP="188.131.169.231:8080";

    //通过Get方式获取HTTP服务器数据
    public static String executeLoginHttpGet(String username, String password){
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            String path = "http://" + IP + "/sign_system/Login";
            path = path + "?username=" + username + "&password=" + password;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");//设置获取信息的方式
            conn.setRequestProperty("Charset", "UTF-8");

            if (conn.getResponseCode() == 200){
                inputStream = conn.getInputStream();
                return parseInfo(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //意外退出时进行连接关闭保护
            if(conn != null){
                conn.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String executeRegisterHttpGet(String username, String password){
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            String path = "http://" + IP + "/sign_system/Register";
            path = path + "?username=" + username + "&password=" + password;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");//设置获取信息的方式
            conn.setRequestProperty("Charset", "UTF-8");

            if (conn.getResponseCode() == 200){
                inputStream = conn.getInputStream();
                return parseInfo(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //意外退出时进行连接关闭保护
            if(conn != null){
                conn.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //上传签到信息
    public static String executeSignHttpGet(String username){
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            String path = "http://" + IP + "/sign_system/Sign";
            path = path + "?username=" + username;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");//设置获取信息的方式
            conn.setRequestProperty("Charset", "UTF-8");

            if (conn.getResponseCode() == 200){
                inputStream = conn.getInputStream();
                return parseInfo(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //意外退出时进行连接关闭保护
            if(conn != null){
                conn.disconnect();
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //根据用户账号和当前月份获取当月签到天数
    private static int getSignDays(String username, int month){
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try{
            String path = "http://" + IP + "sign_system/GetSignDays";
            path = path + "?username=" + username + "&month=" + month;

            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");

            if (conn.getResponseCode() == 200){
                inputStream = conn.getInputStream();
                return Integer.parseInt(parseInfo(inputStream));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            // 意外退出断开连接
            if(conn != null)
                conn.disconnect();
            if(inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return -1;//返回-1表示异常
    }

    private static String parseInfo(InputStream inputStream)throws IOException{
        byte[] data = read(inputStream);
        //转化为字符串
        return new String(data, "UTF-8");
    }

    //将输入流转化为byte型
    public static byte[] read(InputStream inputStream)throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (( len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }

}
