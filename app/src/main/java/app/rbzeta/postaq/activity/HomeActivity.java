package app.rbzeta.postaq.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.adapter.PostQuestionAdapter;
import app.rbzeta.postaq.app.AppConfig;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.rest.model.UserForm;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private long mBackPressed;
    private SessionManager session;
    private NavigationView navigationView;
    private View navigationViewHeaderView;
    private SmoothActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawer;
    private ImageView profilePictureView;
    private TextView textNavHeadUserName;
    private TextView textNavHeadUserEmail;
    private TextView textNavHeadUserBranchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getBaseContext(),getBaseContext().MODE_PRIVATE);

        if (session.isUserLogin()){

            setContentView(R.layout.activity_home);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title_activity_home);



            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabUserProfilePhoto);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "On progress", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();*/

            mDrawerToggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationViewHeaderView = navigationView.getHeaderView(0);
            profilePictureView = (ImageView)navigationViewHeaderView.findViewById(R.id.profile_image);
            textNavHeadUserName = (TextView)navigationViewHeaderView.findViewById(R.id.textNavHeadUserName);
            textNavHeadUserEmail = (TextView)navigationViewHeaderView.findViewById(R.id.textNavHeadUserEmail);
            textNavHeadUserBranchName = (TextView)navigationViewHeaderView.findViewById(R.id.textNavHeadUserBranchName);

            loadNavigationHeaderView();

            List<UserForm> list = new ArrayList<>();
            PostQuestionAdapter adapter = new PostQuestionAdapter(this,list);
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.homeRecycleView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            UserForm a = new UserForm();
            list.add(a);
            a = new UserForm();
            list.add(a);
            a = new UserForm();
            list.add(a);
            adapter.notifyDataSetChanged();

        }else{
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void loadNavigationHeaderView() {
        textNavHeadUserName.setText(session.getUserName());
        textNavHeadUserEmail.setText(session.getUserEmailAddress());
        textNavHeadUserBranchName.setText(session.getBranchName());

        String url = session.getUserProfilePictureUrl();
        Glide.with(getApplicationContext())
                .load(url)
                .placeholder(R.drawable.img_user_profile_default)
                .error(R.drawable.img_user_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .thumbnail(0.2f)
                .into(profilePictureView);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + AppConfig.TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();

            }
            else UIHelper.showToastLong(getBaseContext(),getResources()
                    .getString(R.string.msg_back_btn_exit));
            mBackPressed = System.currentTimeMillis();;
        }


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/

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

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        switch (id){
            case R.id.nav_logout: logoutAttempt();break;
            case R.id.nav_profile_settings: {
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        showProfileActivity();
                    }
                });
            }
                drawer.closeDrawers();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProfileActivity() {
        Intent intent = new Intent(this,UserProfileSettingsActivity.class);
        startActivity(intent);
    }

    private void logoutAttempt() {
        showAlertLogoutConfirmation();
    }

    private void showAlertLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.msg_dialog_logout));

        builder.setPositiveButton(getString(R.string.action_ok),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutProcess();
            }
        });
        builder.setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutProcess() {
        //logout user on server
        logoutUserOnServer();

        //clear local data
        session.setUserLogin(false);

        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void logoutUserOnServer() {
        //retrofit service for logging out user on server
    }

    private void showSettingsLayout() {
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(AppCompatActivity activity, DrawerLayout drawerLayout,
                                           Toolbar toolbar, int openDrawerContentDescRes,
                                           int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (session.isUserProfilePictureChanged()){
            reloadProfilePicture();
            session.setUserProfilePictureChanged(false);
        }

        if (session.isUserInfoChanged()){
            loadNavigationHeaderView();
            session.setUserInfoChanged(false);
        }
    }

    private void reloadProfilePicture() {
        String url = session.getUserProfilePictureUrl();
        Glide.with(getApplicationContext())
                .load(url)
                .placeholder(R.drawable.img_user_profile_default)
                .error(R.drawable.img_user_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .thumbnail(0.2f)
                .into(profilePictureView);
    }
}
