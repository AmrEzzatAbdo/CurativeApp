package com.example.amrezzat.myapplication;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static android.R.attr.name;
import static android.R.attr.password;
import static android.R.attr.start;
import static android.R.attr.theme;

public class searchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   // private String[] Name,SideEffect,Reason,information;
    ArrayList<String> Name;
    ArrayList<String> SideEffect;
    ArrayList<String> Reason;
    ArrayList<String> information;
    ArrayList<Integer> id;
    ListView list;
    Cursor cursor;
    ConnClass con;
    curativeAdapter adapter;
    private FirebaseAuth.AuthStateListener authlistener;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    Statement stmt = null;
    ResultSet rs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        list= (ListView) findViewById(R.id.list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //data set
        Name = new ArrayList<String>();
        SideEffect = new ArrayList<String>();
        Reason = new ArrayList<String>();
        information = new ArrayList<String>();
        id=new ArrayList<Integer>();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent=new Intent(searchActivity.this,AddCurative.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent, options.toBundle());

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //for user
        getData();

        //for Admin
        try {
            if (getIntent().getStringExtra("CheckUser").toString().equals("admin")){
                fab.setVisibility(View.VISIBLE);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent=new Intent(searchActivity.this,EditAndDeleteCurative.class);
                        intent.putExtra("id",id.get(i));
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent, options.toBundle());
                    }
                });
            }
        }catch (Exception e){
            Log.e("Search Activity", "onCreate: ",e);
        }


        mAuth=FirebaseAuth.getInstance();
        authlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }
                // ...
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(authlistener);
        /*
 Snackbar.make(view,adapter.getItem(i).toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    String text=s;
                    adapter.getFilter().filter(text);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    adapter.getFilter().filter(s);
                    /*
                    try {

                        ConnClass co=new ConnClass();
                        Statement st=co.CONN().createStatement();
                        String q = " SELECT  * FROM Curative WHERE C_Name LIKE  '%" + s + "%'" ;
                        ResultSet rs = st.executeQuery( q );
                            while (rs.next()) {
                                adapter.getFilter().filter(rs.getString( 2 ));
                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    return false;

                }

            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.admin) {
            Intent i =new Intent(this,MainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i, options.toBundle());
            finish();
        } else if (id == R.id.doctor) {

        } else if (id == R.id.about) {
            Intent i =new Intent(this,Main2Activity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i, options.toBundle());
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id==R.id.signOut){
            mAuth.signOut();
            Snackbar.make(getWindow().getDecorView().getRootView(),"successful signout", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Intent myIntent = new Intent(searchActivity.this, MainActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fade_in, R.anim.fade_out);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(myIntent, options.toBundle());
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getData(){
        /*//init array
        Name=new String[50];
        information=new String[50];
        SideEffect=new String[50];
        Reason=new String[50];*/
        //sqlserver getdata
        try
        {
            ConnClass co=new ConnClass();
            Statement st=co.CONN().createStatement();
            ResultSet rs = st.executeQuery( "select * from Curative" );
            while ( rs.next() )
            {
                id.add(rs.getInt(1));
                Name.add(rs.getString( 2 )) ;
                information.add(rs.getString(3));
                SideEffect.add(rs.getString(4));
                Reason.add(rs.getString(5));
            }

            adapter=new curativeAdapter(searchActivity.this, Name,SideEffect,Reason,information);
            list.setAdapter(adapter);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }
}
