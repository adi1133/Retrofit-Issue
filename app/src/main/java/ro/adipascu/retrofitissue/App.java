package ro.adipascu.retrofitissue;

import android.app.Application;

import retrofit.RestAdapter;

/**
 * Created by Adi Pascu on 5/7/2015.
 * Email mail@adipascu.ro
 */

public class App extends Application {
    private static final String ENDPOINT = "http://echo.jsontest.com";

    //hacky cache :)
    public static Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .build();

        api = restAdapter.create(Api.class);
    }
}
