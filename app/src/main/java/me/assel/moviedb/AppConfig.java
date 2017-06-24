package me.assel.moviedb;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import me.assel.moviedb.api.utils.RealmInt;
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
        Gson gson = gsonBuilder();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit;
    }

    public static Gson gsonBuilder() {
        Type token = new TypeToken<RealmList<RealmInt>>(){}.getType();
        return new GsonBuilder()
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
                .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmInt>>() {
                    @Override
                    public void write(JsonWriter out, RealmList<RealmInt> value) throws IOException {
                        //ignore
                    }
                    @Override
                    public RealmList<RealmInt> read(JsonReader in) throws IOException {
                        RealmList<RealmInt> list = new RealmList<RealmInt>();
                        in.beginArray();
                        while (in.hasNext()) {
                            list.add(new RealmInt(in.nextInt()));
                        }
                        in.endArray();
                        return list;
                    }
                })
                .create();
    }
}
