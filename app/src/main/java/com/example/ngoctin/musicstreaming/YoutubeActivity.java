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

import com.example.ngoctin.musicstreaming.model.VideoItem;
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

        //setting on click listener for each video_item to launch clicked video in new activity
        holder.video_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }
}

class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;
    public static final String KEY = "AIzaSyB0ryXUHwtNPMUqJGqdQpwtcSLXjEDh-3o";
    public static final String PACKAGENAME = "com.example.ngoctin.musicstreaming";
    public static final String SHA1 = "DE:EE:5B:AA:41:3F:07:50:4E:AD:DC:94:BF:98:82:26:DA:E9:9E:BF";
    private static final long MAXRESULTS = 25;

    public YoutubeConnector(final Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

            //initialize method helps to add any extra details that may be required to process the query
            @Override
            public void initialize(HttpRequest request) throws IOException {
                Log.d("TAG", "context: " +  context.getPackageName());
                request.getHeaders().set("X-Android-Package", context.getPackageName());
                request.getHeaders().set("X-Android-Cert",SHA1);
            }
        }).setApplicationName("SearchYoutube").build();
        try {
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");
        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " + e);
        }
    }

    public List<VideoItem> search(String keywords) {
        query.setQ(keywords);
        query.setMaxResults(MAXRESULTS);

        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<VideoItem> items = new ArrayList<VideoItem>();

            if (results != null) {
                items = setItemsList(results.iterator());
            }
            return items;

        } catch (IOException e) {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }

    private static List<VideoItem> setItemsList(Iterator<SearchResult> iteratorSearchResults) {
        List<VideoItem> tempSetItems = new ArrayList<VideoItem>();

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            if (rId.getKind().equals("youtube#video")) {
                VideoItem item = new VideoItem();
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getHigh();
                item.setId(singleVideo.getId().getVideoId());
                item.setTitle(singleVideo.getSnippet().getTitle());
                item.setDescription(singleVideo.getSnippet().getDescription());
                item.setThumbnailURL(thumbnail.getUrl());

                tempSetItems.add(item);
                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println(" Description: "+ singleVideo.getSnippet().getDescription());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
        return tempSetItems;
    }
}