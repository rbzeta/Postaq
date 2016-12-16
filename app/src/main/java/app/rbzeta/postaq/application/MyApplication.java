package app.rbzeta.postaq.application;

import android.app.Application;

import app.rbzeta.postaq.bc.ConnectivityReceiver;
import app.rbzeta.postaq.database.MyDBHandler;
import app.rbzeta.postaq.helper.SessionManager;
import app.rbzeta.postaq.rest.NetworkService;

/**
 * Created by Robyn on 30/09/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private NetworkService mNetworkService;
    private MyDBHandler myDBHandler;
    private SessionManager mSessionManager;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
        mNetworkService = new NetworkService();
        myDBHandler = MyDBHandler.getInstance(this);
        mSessionManager = new SessionManager(this,MODE_PRIVATE);
    }

    public static synchronized MyApplication getInstance(){return mInstance;}

    public NetworkService getNetworkService(){
        return mNetworkService;
    }

    public MyDBHandler getDBHandler(){return myDBHandler;}

    public SessionManager getSessionManager(){
        return mSessionManager;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
