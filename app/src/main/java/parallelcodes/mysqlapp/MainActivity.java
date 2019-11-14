package parallelcodes.mysqlapp;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainActivity  extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private static boolean connected = false;
    private static boolean update = false;
    MyTask myTask = new MyTask(this);

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
                // myTask = new myTask();
                //    Try this to update one line in database at a time
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
        }

        @Override
        protected String doInBackground(String... strings) {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String result = "Database Connection Successful\n";
            Connection conn = null;

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    try {
                        conn = DriverManager.getConnection(url, user, pass);
                        System.out.println("Database connection success");

                        if (ContextCompat.checkSelfPermission(help, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // coarse permission is granted
                            //Location loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

                            // the mysql insert statement
                            for(int i=0; i<100; i++) {
                                String query = " insert into Test (Heading, Speed, Longitude, Latitude)"
                                        + " values (?, ?, ?, ?)";

                                // create the mysql insert prepared statement
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                if(update) {
                                    preparedStmt.setInt(1, 2);
                                    preparedStmt.setFloat(2, 3);
                                    preparedStmt.setDouble(3, -82.565981);
                                    preparedStmt.setDouble(4, 35.616759);

                                    preparedStmt.execute();
                                }else{
                                    preparedStmt.setInt(1, 2);
                                    preparedStmt.setFloat(2, 3);
                                    preparedStmt.setDouble(3, -82.565981);
                                    preparedStmt.setDouble(4, 35.616759);

                                    preparedStmt.execute();
                                }

                            }
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
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
