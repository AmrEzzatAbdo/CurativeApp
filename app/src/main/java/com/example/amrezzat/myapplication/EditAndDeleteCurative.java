package com.example.amrezzat.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class EditAndDeleteCurative extends AppCompatActivity {
    EditText name, information, sideEffect, Reason;
    ConnClass co;
    Statement st;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_delete_curative);

        name = (EditText) findViewById(R.id.name);
        information = (EditText) findViewById(R.id.curativeInformation);
        sideEffect = (EditText) findViewById(R.id.curativeSideEffect);
        Reason = (EditText) findViewById(R.id.curativeReason);

        //view data by clicked id
        getData(getIntent().getIntExtra("id", -1));
    }

    //id.get(i).toString()
    public void getData(int i) {
        try {
            co = new ConnClass();
            st = co.CONN().createStatement();
            rs = st.executeQuery("select * from Curative where C_ID='" + i + "'");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int q = 1; q <= columnsNumber; q++) {
                    name.setText(rs.getString(2).toString());
                    information.setText(rs.getString(3).toString());
                    sideEffect.setText(rs.getString(4).toString());
                    Reason.setText(rs.getString(5));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void Update(final int Did, final String nam, final String info, final String SideEFCT, final String Reasn) {
        new AlertDialog.Builder(EditAndDeleteCurative.this)
                .setTitle("Attention")
                .setMessage("Saving Update?!")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    rs = st.executeQuery("UPDATE Curative SET C_Name ='" + nam
                                            + "', C_Information = '" + info + "',C_Side_Effect = '"
                                            + SideEFCT + "', C_Reason = '" + Reasn
                                            + "' WHERE C_ID = '" + Did + "'");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                Intent intent=new Intent(EditAndDeleteCurative.this,searchActivity.class);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent, options.toBundle());
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Data Updated", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void Delete(final int Did) {
        new AlertDialog.Builder(EditAndDeleteCurative.this)
                .setTitle("Attention")
                .setMessage("Saving Update?!")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    rs = st.executeQuery("DELETE FROM Curative WHERE C_ID = '" + Did + "'");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                Intent myIntent = new Intent(EditAndDeleteCurative.this, searchActivity.class);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(myIntent, options.toBundle());
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Dara Deleted", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }


    public void DeleteActionBTN(View view) {
        Delete(getIntent().getIntExtra("id", -1));
    }

    public void UpdateActionBTN(View view) {
        Update(getIntent().getIntExtra("id", -1), name.getText().toString(), information.getText().toString(), sideEffect.getText().toString(), Reason.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}