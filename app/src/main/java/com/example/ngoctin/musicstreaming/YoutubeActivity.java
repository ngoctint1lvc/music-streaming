package com.example.ngoctin.musicstreaming;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ngoctin.musicstreaming.data.StaticConfig;
import com.example.ngoctin.musicstreaming.model.VideoItem;
import com.example.ngoctin.musicstreaming.utils.YoutubeConnector;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YoutubeActivity extends AppCompatActivity {
    private static String TAG = "YoutubeActivity";
    private EditText searchInput;
    private YoutubeAdapter youtubeAdapter;
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;
    private Handler handler;
    private List<VideoItem> searchResults;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        mProgressDialog = new ProgressDialog(this);
        searchInput = findViewById(R.id.search_input);
        mRecyclerView = findViewById(R.id.videos_recycler_view);
        searchButton = findViewById(R.id.button_search);

        mProgressDialog.setTitle("Searching...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        handler = new Handler();

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "Search youtube event");
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchOnYoutube(v.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return false;
                }
                return true;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOnYoutube(searchInput.getText().toString());
            }
        });
    }

    private void searchOnYoutube(final String keywords){
        mProgressDialog.setMessage("Finding videos for " + searchInput.getText().toString());
        mProgressDialog.show();

        new Thread(){
            public void run(){
                Log.d("TAG","search Youtube");
                //create our YoutubeConnector class's object with Activity context as argument
                YoutubeConnector yc = new YoutubeConnector(YoutubeActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){
                    public void run(){
                        fillYoutubeVideos();
                        mProgressDialog.dismiss();
                    }
                });
            }
        }.start();
    }

    private void fillYoutubeVideos(){
        youtubeAdapter = new YoutubeAdapter(getApplicationContext(),searchResults);
        mRecyclerView.setAdapter(youtubeAdapter);
        youtubeAdapter.notifyDataSetChanged();
    }

    class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.MyViewHolder> {
        private Context mContext;
        private List<VideoItem> mVideoList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView thumbnail;
            public TextView video_title, video_id, video_description;
            public RelativeLayout video_view;

            public MyViewHolder(View view) {
                super(view);
                thumbnail = view.findViewById(R.id.video_thumbnail);
                video_title = view.findViewById(R.id.video_title);
                video_id = view.findViewById(R.id.video_id);
                video_description = view.findViewById(R.id.video_description);
                video_view = view.findViewById(R.id.video_view);
            }
        }


        public YoutubeAdapter(Context mContext, List<VideoItem> mVideoList) {
            this.mContext = mContext;
            this.mVideoList = mVideoList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final VideoItem singleVideo = mVideoList.get(position);

            holder.video_id.setText("Video ID : "+singleVideo.getId()+" ");
            holder.video_title.setText(singleVideo.getTitle());
            holder.video_description.setText(singleVideo.getDescription());

            Picasso.with(mContext)
                    .load(singleVideo.getThumbnailURL())
                    .resize(480,270)
                    .centerCrop()
                    .into(holder.thumbnail);

            holder.video_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data = new Intent();
                    data.putExtra("videoId", singleVideo.getId());
                    setResult(StaticConfig.RESULT_CODE_OK, data);
                    YoutubeActivity.this.finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mVideoList.size();
        }
    }
}