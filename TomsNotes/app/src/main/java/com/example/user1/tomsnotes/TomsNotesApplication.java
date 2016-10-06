package com.example.user1.tomsnotes;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Chen on 06/10/2016.
 */

public class TomsNotesApplication extends Application {

    public static String APPLICATION_ID = "qZi5lpUgYesgdTvo9Weh5iwOcd9TxZDw1qUxwo08";

    public static String SERVER_ADDRESS = "https://parseapi.back4app.com/";

    public static String CLIENT_KEY = "QCUUC3lJrahGlrTqqiSkHextbFrdnSbDQW37rnA3";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Note.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .server(SERVER_ADDRESS)
                .clientKey(CLIENT_KEY)
                .build()
        );
    }
}