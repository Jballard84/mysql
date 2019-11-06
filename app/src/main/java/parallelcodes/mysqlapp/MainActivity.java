package parallelcodes.mysqlapp;



import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity  extends AppCompatActivity {

    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    Button btnFetch,btnClear;
    TextView txtData;

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

    }
private class MyTask extends AsyncTask<String,Void,String>{
    String res = "";
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                try {
                    Connection conn = DriverManager.getConnection(url,user,pass);
                    System.out.println("Database connection success");

                    String result = "Database Connection Successful\n";
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery("select distinct Heading from Test;");
                    ResultSetMetaData rsmd = rs.getMetaData();

                    while (rs.next()) {
                        result += rs.getString(1).toString() + "\n";
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

}
//mytask



        private class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                System.out.println("Databaseection success");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select distinct Heading from Test");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    result += rs.getString(1).toString() + "\n";
                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            txtData.setText(result);
        }
    }



}
