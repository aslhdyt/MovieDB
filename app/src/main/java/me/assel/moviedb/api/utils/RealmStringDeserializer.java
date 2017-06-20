package me.assel.moviedb.api.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.RealmList;

public class RealmStringDeserializer implements JsonDeserializer<RealmList<RealmInt>> {

    @Override
    public RealmList<RealmInt> deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context) throws JsonParseException {

        RealmList<RealmInt> realmStrings = new RealmList<>();
        JsonArray intList = json.getAsJsonArray();

        for (JsonElement intElement : intList) {
            realmStrings.add(new RealmInt(intElement.getAsInt()));
        }

        return realmStrings;
    }
}