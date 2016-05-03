package com.example.kiwicai.pocketbox.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.proxy.LoginConstant;
import com.example.kiwicai.pocketbox.proxy.MailSendConstant;

public class MailMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginConstant, MailSendConstant {

    Toolbar toolbar;

    Activity main = this;

    String username, password, incoming_server, outgoing_server;
//    TabLayout mailTabLayout;
//    ViewPager mailViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean connectToEmail;

        Bundle bundle = getIntent().getExtras();

        if(bundle != null && bundle.getString(USERNAME) != null) {
            username = bundle.getString(USERNAME);
            password = bundle.getString(PASSWORD);
            incoming_server = bundle.getString(INCOMING_SERVER);
            outgoing_server = bundle.getString(OUTGOING_SERVER);

            connectToEmail = true;
        }
        else {
            // haven't receive information from LoginActivity
            SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_EMAIL, MODE_PRIVATE);
            username = pref.getString(USERNAME, "");
            password = pref.getString(PASSWORD, "");
            incoming_server = pref.getString(INCOMING_SERVER, "");
            outgoing_server = pref.getString(OUTGOING_SERVER, "");
            boolean auto_login = pref.getBoolean(AUTO_LOGIN, false);
            boolean save_password = pref.getBoolean(SAVE_PASSWORD, false);
            boolean is_login = pref.getBoolean(IS_LOGIN, false);
            if(!auto_login || !is_login) {
                Intent intent = new Intent(this, MailSetUpActivity.class);
                Bundle bd = new Bundle();
                bd.putString(USERNAME, username);
                bd.putString(PASSWORD, password);
                bd.putString(INCOMING_SERVER, incoming_server);
                bd.putString(OUTGOING_SERVER, outgoing_server);
                bd.putString(FOLDER_CHOICE, INBOX);
                bd.putString(EXCEPTION, "");
                bd.putBoolean(SAVE_PASSWORD, save_password);
                bd.putBoolean(AUTO_LOGIN, false);
                intent.putExtras(bd);
                startActivity(intent);
                connectToEmail = false;
                System.out.println();
                finish();
            }
            else {
                connectToEmail = true;
            }
        }

        if(connectToEmail) {
            setContentView(R.layout.mail_main_activity);
            toolbar = (Toolbar) findViewById(R.id.mail_main_toolbar);
            toolbar.setTitle("Inbox");
            setSupportActionBar(toolbar);

//        mailTabLayout = (TabLayout) findViewById(R.id.main_tab);
//        mailViewPager = (ViewPager) findViewById(R.id.main_view_pager);


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    Toast.makeText(main, "new", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(main, MailSendActivity.class);
                    Bundle bd = new Bundle();
                    bd.putString(METHOD, NEW);
                    intent.putExtras(bd);
                    startActivity(intent);
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);



            MailDefaultBoxFragment fragment = new MailDefaultBoxFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.remove(fragmentManager.findFragmentById(R.id.activity_main_content));
            Bundle bd = new Bundle();
            bd.putString(USERNAME, username);
            bd.putString(PASSWORD, password);
            bd.putString(INCOMING_SERVER, incoming_server);
            bd.putString(OUTGOING_SERVER, outgoing_server);
            bd.putString(FOLDER_CHOICE, INBOX);
            fragment.setArguments(bd);
            transaction.replace(R.id.mail_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mail_main_toolbar_menu, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean needRefresh = false;


//                transaction.remove(fragmentManager.findFragmentById(R.id.activity_main_content));
        Bundle bd = new Bundle();
        switch (id) {
            case R.id.nav_inbox:
                if(!toolbar.getTitle().equals(INBOX)) {
                    toolbar.setTitle(INBOX);
                    bd.putString(FOLDER_CHOICE, INBOX);
                    needRefresh = true;
                }
                break;
            case R.id.nav_outbox:
                if(!toolbar.getTitle().equals(OUTBOX)) {
                    toolbar.setTitle(OUTBOX);
                    bd.putString(FOLDER_CHOICE, OUTBOX);
                    needRefresh = true;
                }
                break;
            case R.id.nav_draft:
                if(!toolbar.getTitle().equals(DRAFT)) {
                    toolbar.setTitle(DRAFT);
                    bd.putString(FOLDER_CHOICE, DRAFT);
                    needRefresh = true;
                }
                break;
            case R.id.nav_trash:
                if(!toolbar.getTitle().equals(TRASH)) {
                    toolbar.setTitle(TRASH);
                    bd.putString(FOLDER_CHOICE, TRASH);
                    needRefresh = true;
                }
                break;
            case R.id.nav_settings:
                if(!toolbar.getTitle().equals("Tools")) {
                    toolbar.setTitle("Tools");
                    bd.putString(FOLDER_CHOICE, INBOX);
                    needRefresh = true;
                }
                break;
            default:
                break;
        }
        if(needRefresh) {
            bd.putString(USERNAME, username);
            bd.putString(PASSWORD, password);
            bd.putString(INCOMING_SERVER, incoming_server);
            bd.putString(OUTGOING_SERVER, outgoing_server);
            MailDefaultBoxFragment fragment = new MailDefaultBoxFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            fragment.setArguments(bd);
            transaction.replace(R.id.mail_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

//        if (id == R.id.nav_inbox) {
//            toolbar.setTitle("Inbox");
//        } else if (id == R.id.nav_outbox) {
//            toolbar.setTitle("Outbox");
//        } else if (id == R.id.nav_draft) {
//            toolbar.setTitle("Draft");
//        } else if (id == R.id.nav_settings) {
//            toolbar.setTitle("Tools");
//        } else if (id == R.id.my_folder_1) {
//
//        } else if (id == R.id.my_folder_2) {
//
//        } else if (id == R.id.my_folder_add) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
    }
}
