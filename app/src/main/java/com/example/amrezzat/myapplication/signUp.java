package com.example.amrezzat.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class signUp extends AppCompatActivity {
    EditText name,pass;
    NumberPicker age;
    Spinner gender;
    Button signUp;
    private FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 1;
    SignInButton GoogleSignIn;
    private FirebaseAuth.AuthStateListener mAuthListner;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String[] Gender=new String[]{"ذكر","انثى"};
        name= (EditText) findViewById(R.id.name);
        age= (NumberPicker) findViewById(R.id.age);
        gender= (Spinner) findViewById(R.id.gender);
        pass= (EditText) findViewById(R.id.pass);
        signUp= (Button) findViewById(R.id.signUp);
        //spinner adapter
        age.setMaxValue(100);
        age.setMinValue(17);
        age.setValue(17);
        age.setWrapSelectorWheel(true);
        age.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
       // age.setDescendantFocusability(NumberPicker.DRAWING_CACHE_QUALITY_HIGH);
        ArrayAdapter s=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,Gender);
        gender.setAdapter(s);
        //google signUp
        GoogleSignIn= (SignInButton) findViewById(R.id.GoogleSignUP);
        GoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Snackbar.make(getWindow().getDecorView().getRootView(),"Please Wait", Snackbar.LENGTH_LONG).show();
                signIn();

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        progressDialog.show();
                    }
                }, 300);
                signUpInDB();

                //Toast.makeText(signUp.this, String.valueOf(age.getValue())+ "      " +gender.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        //Google SignIn
        //sign out if account signin
        try {
            mAuth.signOut();
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e);
        }
        //auth for firbase
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent l = new Intent(signUp.this, searchActivity.class);
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
                Toast.makeText(signUp.this, "Sorry can't sign in", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

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
                if (task.isSuccessful()){
                    ConnClass co=new ConnClass();
                    Statement st= null;
                    try {
                        st = co.CONN().createStatement();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user!=null) {
                            st.executeQuery("insert into Useres (U_Name,U_Age,U_Gender,pass) VALUES ('" + user.getEmail().toString() + "','" + 18 + "','" + "googleSign Unknown" + "','" + user.getToken(true).toString()+" + "+user.getUid().toString()+ "');");
                            Snackbar.make(getWindow().getDecorView().getRootView(), "signup successful", Snackbar.LENGTH_LONG).show();
                        }
                        else {
                            Log.d(TAG, "onAuthStateChanged:signed_out");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent l = new Intent(signUp.this, searchActivity.class);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                    l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(l, options.toBundle());
                    progressDialog.dismiss();
                    finish();

                }
                if (!task.isSuccessful()) {

                    Log.w("Main Activity", "onComplete:signInWithCredential ", task.getException());
                    Toast.makeText(signUp.this, "Authentication faild", Toast.LENGTH_SHORT).show();

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sIgnIn(View view) {
        Intent i =new Intent(signUp.this, MainActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i, options.toBundle());
        finish();
    }
    public void signUpInDB(){
        try
        {
            ConnClass co=new ConnClass();
            Statement st=co.CONN().createStatement();
            // st.executeQuery("insert into Useres (U_Name,U_Age,U_Gender,pass) VALUES ('"+name.getText().toString()+"','"+age.getValue()+"','"+gender.getSelectedItem().toString()+"','"+pass.getText().toString()+"');");
            st.executeQuery("insert into Useres (U_Name,U_Age,U_Gender,pass) VALUES ('"+name.getText().toString()+"','"+Integer.valueOf(age.getValue())+"','"+gender.getSelectedItem().toString()+"','"+pass.getText().toString()+"');");
            Snackbar.make(getWindow().getDecorView().getRootView(), "signup successful", Snackbar.LENGTH_LONG).show();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        Intent myIntent = new Intent(signUp.this, searchActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(myIntent, options.toBundle());
        progressDialog.dismiss();
        finish();
    }
}
