package com.example.amrezzat.myapplication;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by amrezzat on 4/7/2018.
 */

public class ConnClass {

    //DB hosted connection
    String ip="SQL5005.site4now.net";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db="DB_A397BE_curative";
    String un = "DB_A397BE_curative_admin";
    String password = "AmR1271997";

    //local Db connection
    /*
    String ip = "192.168.173.1";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "S.U.D&&N.U.C";
    String un = "test";
    String password = "123";
    */

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }

}
