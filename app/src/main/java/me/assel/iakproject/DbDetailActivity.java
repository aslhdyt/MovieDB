package me.assel.iakproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.assel.iakproject.adapter.MoviePresenter;
import me.assel.iakproject.adapter.ReviewAdapter;
import me.assel.iakproject.adapter.VideoAdapter;
import me.assel.iakproject.api.request.RequestInterface;
import me.assel.iakproject.api.response.Reviews;
import me.assel.iakproject.api.response.Videos;
import me.assel.iakproject.db.DbObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.assel.iakproject.adapter.MoviePresenter.API_KEY;
import static me.assel.iakproject.adapter.MoviePresenter.IMG_BASE_URL;

public class DbDetailActivity extends AppCompatActivity {
    ImageView poster, like;
    TextView title, release, overView, star;

    TabHost host;

    DbObject movie;
    List<Videos.Result> videos;
    List<Reviews.Result> reviews;

    Realm realm;

    boolean isLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        poster = (ImageView) findViewById(R.id.imageView_poster);
        title = (TextView) findViewById(R.id.textView_author);
        release = (TextView) findViewById(R.id.textView_release);
        overView = (TextView) findViewById(R.id.textView_overView);
        star = (TextView) findViewById(R.id.textView_star);
        like = (ImageView) findViewById(R.id.imageView_like);


        long id = getIntent().getLongExtra("id", 0);
        realm = Realm.getDefaultInstance();

        movie =  realm.where(DbObject.class)
                .equalTo("id", id)
                .findFirst();
        if(movie == null) return;
        setLike(true);
        title.setText(movie.getTitle());
        release.setText(movie.getRelease());
        overView.setText(movie.getOverview());
        star.setText(String.valueOf(movie.getRating()));
        Picasso.with(this).load(IMG_BASE_URL+ movie.getImgUrl()).placeholder(R.drawable.video).into(poster);




        //init tab
        host = (TabHost)findViewById(R.id.TabHost_content);
        host.setup();

        //tab1
        TabHost.TabSpec spec = host.newTabSpec("Videos");
        spec.setContent(R.id.recyclerView_trailer);
        spec.setIndicator("Videos");
        host.addTab(spec);

        //tab2
        spec = host.newTabSpec("Reviews");
        spec.setContent(R.id.recyclerView_review);
        spec.setIndicator("Reviews");
        host.addTab(spec);


        //getTrailerVideos
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviePresenter.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Videos> call = request.getVideos(movie.getId(), API_KEY);

        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                List<Videos.Result> result = response.body().getResults();
                videos = result;

//                int i = 0;
//                for (Videos.Result video : result) {
//                    Log.d("Videos", "index = "+i
//                        +"\nName = "+video.getName()
//                        +"\nid = "+video.getId()
//                        +"\nsite = "+video.getSite()
//                        +"\ntype = "+video.getType()
//                        +"\nsize = "+video.getSize());
//                    i++;
//                }
                VideoAdapter adapter = new VideoAdapter(getBaseContext(), result);

                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView_trailer);
                LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
                manager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Call<Reviews> call1 = request.getReviews(movie.getId(), API_KEY);
        call1.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                List<Reviews.Result> result = response.body().getResults();
                reviews = result;

                Log.d("review", response.toString());
                ReviewAdapter adapter = new ReviewAdapter(getBaseContext(), result);

                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView_review);
                LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
                manager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });

        ImageView like = (ImageView)findViewById(R.id.imageView_like);
        like.setTag(R.drawable.unlike);
    }

    public void like (View view) {
        //TODO: 5/26/17 SAVE TO SQLITE
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ImageView v = (ImageView) view;
        if (!isLike) {
            DbObject db = realm.createObject(DbObject.class, movie.getId());
            db.setTitle(movie.getTitle());
            db.setRelease(movie.getRelease());
            db.setOverview(movie.getOverview());
            db.setRating(movie.getRating());
            db.setImgUrl(movie.getImgUrl());
            setLike(true);
        } else {
            RealmResults<DbObject> result = realm
                    .where(DbObject.class)
                    .equalTo("title", movie.getTitle())
                    .findAll();
            result.deleteAllFromRealm();
            setLike(false);
        }
        realm.commitTransaction();
    }

    void setLike(boolean l) {
        if(l) {
            like.setImageDrawable(getResources().getDrawable(R.drawable.liked));
            like.setTag(R.drawable.liked);
            isLike = true;
            ((TextView)findViewById(R.id.textView_like)).setText("You liked this");

        } else {
            like.setImageDrawable(getResources().getDrawable(R.drawable.unlike));
            like.setTag(R.drawable.unlike);
            isLike = false;
            ((TextView)findViewById(R.id.textView_like)).setText("Like this?");
        }
    }


}
