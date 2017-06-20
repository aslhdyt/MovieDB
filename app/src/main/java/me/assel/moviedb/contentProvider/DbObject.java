package me.assel.moviedb.contentProvider;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by assel on 5/27/17.
 */

public class DbObject extends RealmObject {
    @PrimaryKey
    private long id;
    private String title;
    private String overview;
    private String release;
    private float rating;
    private String imgUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public String toString() {
        return "id = " +id+
                "\ntitle = " +title+
                "\noverview = " +overview+
                "\nrelease = " +release+
                "\nrating = " +rating+
                "\nimgUrl = " +imgUrl;
    }


}
