package me.assel.moviedb.api.utils;

import io.realm.RealmObject;

// https://gist.github.com/cmelchior/1a97377df0c49cd4fca9
public class RealmInt extends RealmObject {
    private int val;

    public RealmInt() {
    }
    public RealmInt(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}