package me.assel.iakproject;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import me.assel.iakproject.api.utils.RealmInt;
import me.assel.iakproject.api.utils.RealmStringDeserializer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by assel on 6/20/17.
 */

public class AppConfig {
    public static final String API_KEY = "0435f42cef8dc5bf18741d2c0df0034a";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String IMG_BASE_URL = "https://image.tmdb.org/t/p/w780";

    public static RealmConfiguration realmConfig () {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("fav.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(2)
                .build();
        return config;

    }

    public static Retrofit retrofitBuilder() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapter(new TypeToken<RealmList<RealmInt>>() {
                }.getType(), new RealmStringDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit;
    }
}
