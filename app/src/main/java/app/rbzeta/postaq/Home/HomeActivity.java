package app.rbzeta.postaq.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import app.rbzeta.postaq.R;
import app.rbzeta.postaq.activity.LoginActivity;
import app.rbzeta.postaq.activity.SearchActivity;
import app.rbzeta.postaq.activity.UserProfileSettingsActivity;
import app.rbzeta.postaq.adapter.ViewPagerHomeAdapter;
import app.rbzeta.postaq.app.AppConfig;
import app.rbzeta.postaq.custom.CircleTransform;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.helper.UIHelper;
import app.rbzeta.postaq.home.view.HomeNotificationFragment;
import app.rbzeta.postaq.home.view.HomePostFragment;
import app.rbzeta.postaq.home.view.HomeProfileFragment;
import app.rbzeta.postaq.home.view.NewPostFragment;

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
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerHomeAdapter mAdapter;
    private FloatingActionButton fab;
    public static boolean fabIsVisible = true;


    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (makeTranslucent) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getBaseContext(),getBaseContext().MODE_PRIVATE);

        if (session.isUserLogin()){

            setContentView(R.layout.activity_home);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.title_activity_home);

            //setStatusBarTranslucent(true);

            viewPager = (ViewPager)findViewById(R.id.viewpagerHome);
            setupViewPager();
            tabLayout = (TabLayout)findViewById(R.id.tab_home);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                    setUITab(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    super.onTabUnselected(tab);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    super.onTabReselected(tab);

                    if (tab.getPosition() == AppConfig.TAB_HOME){
                        Fragment f = mAdapter.getItem(tab.getPosition());
                        if (f != null) {
                            View fragmentView = f.getView();
                            RecyclerView mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.homeRecycleView);
                            if (mRecyclerView != null) {
                                mRecyclerView.smoothScrollToPosition(0);
                                fab.show();
                            }
                        }
                    }

                }
            });


            fab = (FloatingActionButton) findViewById(R.id.fabUserProfilePhoto);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //show fragment new post
                    NewPostFragment fragment = new NewPostFragment();
                    fragment.show(getSupportFragmentManager(),"fragment_new_post");

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
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationViewHeaderView = navigationView.getHeaderView(0);
            profilePictureView = (ImageView)navigationViewHeaderView.findViewById(R.id.profile_image);
            textNavHeadUserName = (TextView)navigationViewHeaderView.findViewById(R.id.textNavHeadUserName);
            textNavHeadUserEmail = (TextView)navigationViewHeaderView.findViewById(R.id.textNavHeadUserEmail);
            textNavHeadUserBranchName = (TextView)navigationViewHeaderView.findViewById(R.id.textNavHeadUserBranchName);

            loadNavigationHeaderView();

        }else{
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void setUITab(int position) {
        switch (position){
            case AppConfig.TAB_HOME:
                getSupportActionBar().setTitle(getString(R.string.title_tab_home));
                fab.show();
                fabIsVisible = true;
                break;
            case AppConfig.TAB_NOTIFICATION:
                getSupportActionBar().setTitle(getString(R.string.title_tab_notification));
                fab.hide();
                fabIsVisible = false;
                break;
            case AppConfig.TAB_PROFILE:
                getSupportActionBar().setTitle(getString(R.string.title_tab_profile));
                fab.hide();
                fabIsVisible = false;
                break;
        }
    }

    private void setupViewPager() {
        mAdapter = new ViewPagerHomeAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new HomePostFragment(),getString(R.string.title_tab_home));
        mAdapter.addFragment(new HomeNotificationFragment(),getString(R.string.title_tab_notification));
        mAdapter.addFragment(new HomeProfileFragment(),getString(R.string.title_tab_profile));
        viewPager.setAdapter(mAdapter);
    }

    private void loadNavigationHeaderView() {
        textNavHeadUserName.setText(session.getUserName());
        textNavHeadUserEmail.setText(session.getUserEmailAddress());
        textNavHeadUserBranchName.setText(session.getBranchName());

        String url = session.getUserProfilePictureUrl();
        /*Glide.with(getApplicationContext())
                .load(url)
                .placeholder(R.drawable.img_user_profile_default)
                .error(R.drawable.img_user_profile_default)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CircleTransform(HomeActivity.this))
                .centerCrop()
                .crossFade()
                .thumbnail(0.2f)
                .into(profilePictureView);*/
        if (url == null) {
            Glide.with(this).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(profilePictureView);
        }else
            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.app_bar_search) {
            //Toast.makeText(this,"Search action pressed",Toast.LENGTH_LONG).show();
            //onSearchRequested();
            Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

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
        if (url == null) {
            Glide.with(this).load(R.drawable.img_user_profile_default)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(profilePictureView);
        }else
            Glide.with(this).load(url)
                    .crossFade()
                    .thumbnail(0.2f)
                    .centerCrop()
                    .error(R.drawable.img_user_profile_default)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(profilePictureView);
    }
}
