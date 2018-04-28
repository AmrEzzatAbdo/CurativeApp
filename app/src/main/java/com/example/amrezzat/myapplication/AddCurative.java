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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Statement;

import static com.example.amrezzat.myapplication.R.id.age;

public class AddCurative extends AppCompatActivity {
    EditText name,information,sideEffect,Reason;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curative);

        name= (EditText) findViewById(R.id.name);
        information= (EditText) findViewById(R.id.curativeInformation);
        sideEffect= (EditText) findViewById(R.id.curativeSideEffect);
        Reason= (EditText) findViewById(R.id.curativeReason);
    }

    public void addAction(View view) {
        new AlertDialog.Builder(AddCurative.this)
                .setTitle("Attention")
                .setMessage("Insert?!")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    ConnClass co=new ConnClass();
                                    Statement st=co.CONN().createStatement();
                                    st.executeQuery("insert into Curative (C_Name,C_Information,C_Side_Effect,C_Reason) VALUES ('"+name.getText().toString()+"','"+information.getText().toString()+"','"+sideEffect.getText().toString()+"','"+Reason.getText().toString()+"');");
                                    Snackbar.make(getWindow().getDecorView().getRootView(), "Dara Inserted", Snackbar.LENGTH_LONG).show();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                Intent myIntent = new Intent(AddCurative.this, searchActivity.class);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(myIntent, options.toBundle());
                                finish();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
