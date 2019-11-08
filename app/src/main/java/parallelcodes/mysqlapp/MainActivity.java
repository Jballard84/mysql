package parallelcodes.mysqlapp;



import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity  extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";

    private GoogleMap mMap;

    Button btnFetch,btnClear;
    TextView txtData;

    // http://10.123.21.91/phpmyadmin/index.php

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
                MyTask myTask = new MyTask();
                myTask.execute();
// TODO Auto-generated method stub

            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtData.setText("");
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
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
        // public Location getMyLocation
        // Add a marker in Sydney and move the camera
        //LatLng UNCA_Quad = new LatLng(35.616314, -82.56732);
        //mMap.addMarker(new MarkerOptions().position(UNCA_Quad).title("Marker in Asheville quad"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(UNCA_Quad));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

    }

    private class MyTask extends AsyncTask<String,Void,String>{
        String res = "";
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location loc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                try {
                    Connection conn = DriverManager.getConnection(url,user,pass);
                    System.out.println("Database connection success");

                    // the mysql insert statement
                    String query = " insert into Test (Heading, Speed, Longitude, Latitude)"
                            + " values (?, ?, ?, ?)";

                    // create the mysql insert preparedstatement
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt (1, 't');
                    preparedStmt.setFloat (2, 3);
                    loc = mMap.getMyLocation();
                    preparedStmt.setDouble (3, loc.getLongitude());
                    preparedStmt.setDouble (4, loc.getLatitude());

                    String result = "Database Connection Successful\n";
                    preparedStmt.execute();

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
