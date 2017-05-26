
package me.assel.iakproject.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    public final static Creator<Videos> CREATOR = new Creator<Videos>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Videos createFromParcel(Parcel in) {
            Videos instance = new Videos();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.results, (Result.class.getClassLoader()));
            return instance;
        }

        public Videos[] newArray(int size) {
            return (new Videos[size]);
        }

    }
    ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return  0;
    }

    public static class Result implements Parcelable
    {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("iso_639_1")
        @Expose
        private String iso_639_1;
        @SerializedName("iso_3166_1")
        @Expose
        private String iso_3166_1;
        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("site")
        @Expose
        private String site;
        @SerializedName("size")
        @Expose
        private Integer size;
        @SerializedName("type")
        @Expose
        private String type;
        public final static Creator<Result> CREATOR = new Creator<Result>() {


            @SuppressWarnings({
                "unchecked"
            })
            public Result createFromParcel(Parcel in) {
                Result instance = new Result();
                instance.id = ((String) in.readValue((String.class.getClassLoader())));
                instance.iso_639_1 = ((String) in.readValue((String.class.getClassLoader())));
                instance.iso_3166_1 = ((String) in.readValue((String.class.getClassLoader())));
                instance.key = ((String) in.readValue((String.class.getClassLoader())));
                instance.name = ((String) in.readValue((String.class.getClassLoader())));
                instance.site = ((String) in.readValue((String.class.getClassLoader())));
                instance.size = ((Integer) in.readValue((Integer.class.getClassLoader())));
                instance.type = ((String) in.readValue((String.class.getClassLoader())));
                return instance;
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        }
        ;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public void setIso_3166_1(String iso_3166_1) {
            this.iso_3166_1 = iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(iso_639_1);
            dest.writeValue(iso_3166_1);
            dest.writeValue(key);
            dest.writeValue(name);
            dest.writeValue(site);
            dest.writeValue(size);
            dest.writeValue(type);
        }

        public int describeContents() {
            return  0;
        }

    }
}
