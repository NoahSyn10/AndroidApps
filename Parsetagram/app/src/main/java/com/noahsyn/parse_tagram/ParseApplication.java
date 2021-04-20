package com.noahsyn.parse_tagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("G4zZKFILdc3D3yjwg80cfQNhOOGqWAlghsnh0jdl")
                .clientKey("d3a3LiHZf0lJl07AfWbjpZlRcdUnrAUxxFkmAB5A")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
