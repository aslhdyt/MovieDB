package me.assel.moviedb;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.assel.moviedb.api.RequestInterface;
import me.assel.moviedb.contentProvider.Contract;
import me.assel.moviedb.contentProvider.DBHelper;
import me.assel.moviedb.model.Movies;
import me.assel.moviedb.model.Reviews;
import me.assel.moviedb.model.Videos;
import me.assel.moviedb.presenter.adapter.ReviewAdapter;
import me.assel.moviedb.presenter.adapter.VideoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.assel.moviedb.AppConfig.API_KEY;
import static me.assel.moviedb.AppConfig.BASE_URL;
import static me.assel.moviedb.AppConfig.IMG_BASE_URL;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.imageView_poster) ImageView poster;
    @BindView(R.id.imageView_like) ImageView like;
    CollapsingToolbarLayout title_colaps;
    TextView title_txt;
    @BindView(R.id.textView_release) TextView release;
    @BindView(R.id.textView_overView) TextView overView;
    @BindView(R.id.textView_star) TextView star;

    TabHost host;

    Movies movie;
    List<Videos.Result> videos;
    List<Reviews.Result> reviews;

    boolean isLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);
        ButterKnife.bind(this);

        movie = getIntent().getExtras().getParcelable("result");
        if(movie == null) return;

        Picasso.with(this).load(IMG_BASE_URL+ movie.getPosterPath()).placeholder(R.drawable.video).into(poster);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            title_colaps = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            title_colaps.setTitle(movie.getTitle());
            title_colaps.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        } else {
            title_txt = (TextView)findViewById(R.id.textView_title);
            title_txt.setText(movie.getTitle());
        }

        release.setText(movie.getReleaseDate());
        overView.setText(movie.getOverview());
        star.setText(String.valueOf(movie.getVoteAverage()));

        //determine movie is liked
        String stringId = Integer.toString(movie.getId());
        Uri uri = Contract.Entry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor singleData = getContentResolver().query(uri, null, null, null, null);
        if (singleData.getCount() > 0) {
            singleData.close();
            setLike(true);
        }

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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Videos> call = request.getVideos(movie.getId(), API_KEY);

        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(@NonNull Call<Videos> call, @NonNull Response<Videos> response) {
                List<Videos.Result> result = response.body().getResults();
                videos = result;

                VideoAdapter adapter = new VideoAdapter(getBaseContext(), result);

                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView_trailer);
                LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
                manager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<Videos> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        Call<Reviews> call1 = request.getReviews(movie.getId(), API_KEY);
        call1.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {
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
            public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {

            }
        });

        ImageView like = (ImageView)findViewById(R.id.imageView_like);
        like.setTag(R.drawable.unlike);
    }


    public void like (View view) {
        if (!isLike) {
            //INSERT to content provider

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.ID, movie.getId());
            contentValues.put(DBHelper.VOTE_COUNT, movie.getVoteCount());
            contentValues.put(DBHelper.VIDEO, movie.isVideo());
            contentValues.put(DBHelper.VOTE_AVERAGE, movie.getVoteAverage());
            contentValues.put(DBHelper.TITLE, movie.getTitle());
            contentValues.put(DBHelper.POPULARITY, movie.getPopularity());
            contentValues.put(DBHelper.POSTER_PATH, movie.getPosterPath());
            contentValues.put(DBHelper.ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
            contentValues.put(DBHelper.ORIGINAL_TITLE, movie.getOriginalTitle());
            String genreIds_CSV = toCSV(movie.getGenreIds());
            Log.d("CSV", genreIds_CSV);
            contentValues.put(DBHelper.GENRE_IDS, genreIds_CSV);
            contentValues.put(DBHelper.BACKDROP_PATH, movie.getBackdropPath());
            contentValues.put(DBHelper.ADULT, movie.isAdult());
            contentValues.put(DBHelper.OVERVIEW, movie.getOverview());
            contentValues.put(DBHelper.RELEASE_DATE, movie.getReleaseDate());

            Uri uri = getContentResolver().insert(Contract.Entry.CONTENT_URI, contentValues);

            if (uri != null) {
                setLike(true);
            }
        } else {
            // DELETE from content provider
            String stringId = Integer.toString(movie.getId());
            Uri uri = Contract.Entry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            int taskDelete = getContentResolver().delete(uri, null, null);
            if (taskDelete != 0) {
                setLike(false);
            }
        }
    }

    private String toCSV(int[] genreIds) {
        if (genreIds.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int n : genreIds) {
                builder.append(String.valueOf(n)).append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        } else {
            return "";
        }
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
