package example.com.iot;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int su1=0;
    int su2=0;
    int su3=0;
    boolean tampon = false;
    TextView tv1,tv2,tv3;

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         tv1 = findViewById(R.id.textView5);
         tv2 = findViewById(R.id.textView7);
         tv3 = findViewById(R.id.textView9);

        callAsynchronousTask();

    }

    class arkaPlan extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                String data = "";
                while ((line = br.readLine()) != null) {
                    Log.d("line:", line);
                    data += line;
                }
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Hata";
        }

        protected void onPostExecute(String s) {
            try {

                JSONObject jo = new JSONObject(s);
                String x = jo.getString("feeds");
                JSONArray ja = new JSONArray(x);
                JSONObject jo1 = (JSONObject) ja.get(0);
                String aranan = jo1.getString("field1");
                su1 = Integer.parseInt(aranan);
                tv1.setText(aranan);
                System.out.println("1:"+aranan);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class arkaPlan2 extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                String data = "";
                while ((line = br.readLine()) != null) {
                    Log.d("line:", line);
                    data += line;
                }
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Hata";
        }

        protected void onPostExecute(String s) {
            try {

                JSONObject jo = new JSONObject(s);
                String x = jo.getString("feeds");
                JSONArray ja = new JSONArray(x);
                JSONObject jo1 = (JSONObject) ja.get(0);
                String aranan = jo1.getString("field2");
                su2 = Integer.parseInt(aranan);
                tv2.setText(aranan);
                System.out.println("2:"+aranan);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class arkaPlan3 extends AsyncTask<String, String, String> {

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                String data = "";
                while ((line = br.readLine()) != null) {
                    Log.d("line:", line);
                    data += line;
                }
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Hata";
        }

        protected void onPostExecute(String s) {
            try {

                JSONObject jo = new JSONObject(s);
                String x = jo.getString("feeds");
                JSONArray ja = new JSONArray(x);
                JSONObject jo1 = (JSONObject) ja.get(0);
                String aranan = jo1.getString("field3");
                su3 = Integer.parseInt(aranan);
                tv3.setText(aranan);
                System.out.println("3:"+aranan);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new arkaPlan().execute("https://api.thingspeak.com/channels/931873/fields/1.json?api_key=D7Y6CO5CN4CHJ3SM&results=1");
                            new arkaPlan2().execute("https://api.thingspeak.com/channels/931873/fields/2.json?api_key=D7Y6CO5CN4CHJ3SM&results=1");
                            new arkaPlan3().execute("https://api.thingspeak.com/channels/931873/fields/3.json?api_key=D7Y6CO5CN4CHJ3SM&results=1");
                            int toplam=su1+su2+su3;
                            if(toplam >= 5 && tampon == false){
                                showNotification("SU BASKINI","ISLANDIN");
                                tampon = true;

                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000);
    }

    public void showNotification(String title, String body){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID="com.example.myfcm.newChannel";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"NotificationName",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Test channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});

            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");


        notificationManager.notify(new Random().nextInt(),notificationBuilder.build() );
    }
}