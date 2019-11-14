package parallelcodes.mysqlapp;



import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity  extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private static final String url = "jdbc:mariadb://10.123.21.91:3306/EarthDrone";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private static boolean update = false;
    MyTask myTask = new MyTask(this);
    MyTask myTask2 = new MyTask(this);

    private GoogleMap mMap;

    Button btnFetch,btnClear;
    TextView txtData;

    // http://10.123.21.91/phpmyadmin/index.php

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (TextView) this.findViewById(R.id.txtData);
        btnFetch = (Button) findViewById(R.id.btnFetch);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myTask.execute();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtData.setText("");
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        //Toast.makeText(this, "Location found");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }

    public final class MyTask extends AsyncTask<String,Void,String>
    {
        String res = "";

        MainActivity help;

        private MyTask(MainActivity main){
            help = main;
            txtData.setText("Database is being updated");
        }

        @Override
        protected String doInBackground(String... strings) {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String result = "Database updated with new coordinates\n";
            Connection conn = null;

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    try {
                        conn = DriverManager.getConnection(url, user, pass);
                        System.out.println("Database connection success");

                        //Context context = new Context();

                        //IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                        //Intent batteryStatus = context.registerReceiver(null, ifilter);

                        //int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

                        if (ContextCompat.checkSelfPermission(help, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // coarse permission is granted
                            //Location loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

                            // the mysql insert statement
                            int i = 0;
                            while(true) {
                                i++;
                                if(update) {
                                    Statement preparedStmt = conn.createStatement();

                                    preparedStmt.execute(" Update Test Set Battery='" + i + "' Where Row = '1'");

                                    preparedStmt.execute(" Update Test Set Heading='W' Where Row = '1'");

                                    preparedStmt.execute(" Update Test Set Lon='-82.565806' Where Row = '1'");

                                    preparedStmt.execute(" Update Test Set Lat='35.615243' Where Row = '1'");
                                    update = false;
                                }else{
                                    Statement preparedStmt = conn.createStatement();

                                    preparedStmt.execute(" Update Test Set Battery='" + i + "' Where Row = '1'");

                                    preparedStmt.execute(" Update Test Set Heading='E' Where Row = '1'");

                                    preparedStmt.execute(" Update Test Set Lon='-82.565981' Where Row = '1'");

                                    preparedStmt.execute(" Update Test Set Lat='35.616759' Where Row = '1'");
                                    update = true;
                                }

                                Thread.sleep(3000);

                            }
                            /*try {
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }*/
                        } else { // permission is not granted, request for permission
                            if (ActivityCompat.shouldShowRequestPermissionRationale(help, Manifest.permission.ACCESS_COARSE_LOCATION)) { // show some info to user why you want this permission
                                //Toast.makeText(this, "Allow Location Permission to use this functionality.", Toast.LENGTH_SHORT).show();
                                ActivityCompat.requestPermissions(help, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123 /*LOCATION_PERMISSION_REQUEST_CODE*/);
                            } else {
                                ActivityCompat.requestPermissions(help, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123 /*LOCATION_PERMISSION_REQUEST_CODE*/);

                            }
                        }

                        res = result;

                    } catch (Exception e) {
                        e.printStackTrace();
                        res = e.toString();
                    }
                    return res;

                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Data base selection success");

            return null;
        }

    protected void onPostExecute(String result) {
        txtData.setText(result);
    }



}//mytask




}
