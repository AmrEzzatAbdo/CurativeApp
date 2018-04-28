package com.example.amrezzat.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ConnClass connectionClass;
    EditText edtuserid, edtpass;
    Button btnlogin;
    SignInButton GoogleSignIn;
    ProgressDialog progress;
    private FirebaseAuth mAuth;
    Spinner AorU;
    GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         String[] x=new String[]{"مشرف","مستخدم"};
        connectionClass = new ConnClass();
        edtuserid = (EditText) findViewById(R.id.et_username);
        edtpass = (EditText) findViewById(R.id.et_password);
        btnlogin = (Button) findViewById(R.id.btn_Login);
        GoogleSignIn= (SignInButton) findViewById(R.id.googleSignIn);
        progress=new ProgressDialog(this);
        progress.setMessage("please wait...");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        AorU= (Spinner) findViewById(R.id.AdminOrUser);
        ArrayAdapter s=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,x);
        AorU.setAdapter(s);
        AorU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (AorU.getSelectedItem().toString().equals("مشرف")){
                    edtuserid.setText("admin");
                    edtuserid.setEnabled(false);
                }
                else {
                    edtuserid.setText("");
                    edtuserid.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                edtuserid.setEnabled(true);
            }

        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });

        //Google SignIn
        GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                progress.show();
                Snackbar.make(getWindow().getDecorView().getRootView(),"Please Wait", Snackbar.LENGTH_LONG).show();
                signIn();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent l = new Intent(MainActivity.this, searchActivity.class);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                    l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(l, options.toBundle());
                    finish();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Sorry can't sign in", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Signup(View view) {
        Intent i =new Intent(MainActivity.this, signUp.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i, options.toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void skip(View view) {
        Intent i =new Intent(MainActivity.this, searchActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i, options.toBundle());
        finish();
    }


    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;


        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();


        @Override
        protected void onPreExecute() {
            progress.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progress.dismiss();
            Snackbar.make(getWindow().getDecorView().getRootView(), r, Snackbar.LENGTH_LONG).show();

            if (isSuccess) {
                Snackbar.make(getWindow().getDecorView().getRootView(), r, Snackbar.LENGTH_LONG).show();
            }

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected String doInBackground(String... params) {
            if (userid.trim().equals("") || password.trim().equals(""))
                z = "Please enter User Id and Password";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from Useres where U_Name='" + userid + "' and pass='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            z = "Login successfull";
                            isSuccess = true;
                            Intent i = new Intent(MainActivity.this, searchActivity.class);
                            i.putExtra("CheckUser", userid);
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(i, options.toBundle());
                            finish();
                        } else {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.toString();
                }
            }
            return z;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        } else {
            Toast.makeText(this, "Error in Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                Log.d(TAG, "onComplete:signInWithCredential " + task.isSuccessful());
                FirebaseUser user=mAuth.getCurrentUser();
                if (task.isSuccessful()){
                    String query = "select * from Useres where U_Name='" + user.getEmail().toString() + "' and U_Age='" +18+"'";
                    ConnClass co=new ConnClass();
                    try {
                        Statement stmt = co.CONN().createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            Intent l = new Intent(MainActivity.this, searchActivity.class);
                            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                            l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(l, options.toBundle());
                            progress.dismiss();
                            finish();
                        }
                        else {
                            Snackbar.make(getWindow().getDecorView().getRootView(),"Please signUp..", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (!task.isSuccessful()) {
                    Log.w("Main Activity", "onComplete:signInWithCredential ", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication faild", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
