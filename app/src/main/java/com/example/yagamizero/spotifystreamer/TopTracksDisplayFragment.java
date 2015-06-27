package com.example.yagamizero.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Fragment for displaying top tracks of the artist
 */
public class TopTracksDisplayFragment extends Fragment {

    TopTracksAdapter topTracksAdapter;
    List<Track> resultTracks;

    public TopTracksDisplayFragment(){    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_top_tracks, container, false);
        String artistId = "";

        // getting the artist_id
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            artistId = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        resultTracks = new ArrayList<Track>();
        topTracksAdapter = new TopTracksAdapter(
                getActivity(),
                resultTracks
        );

        ListView tracksListView = (ListView) rootView.findViewById(R.id.listview_tracks);

        tracksListView.setAdapter(topTracksAdapter);

        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String trackPreviewUrl = topTracksAdapter.getItem(position).preview_url;
                // TODO: For part 2 - create a player to play the preview audio
/*                Intent intent = new Intent(getActivity(), TopTracks.class)
                        .putExtra(Intent.EXTRA_TEXT, artist);
                startActivity(intent);*/
            }
        });


        FetchTopTracksTask fetchTopTracksTask = new FetchTopTracksTask();
        fetchTopTracksTask.execute(artistId);
        return rootView;
    }

    public class FetchTopTracksTask extends AsyncTask<String, Void, List<Track>>{

        @Override
        protected List<Track> doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                HashMap queryMap = new HashMap();
                queryMap.put("country", "US");
                Tracks results = spotify.getArtistTopTrack(params[0], queryMap);
                int trackCount;
                try {
                    trackCount = results.tracks.size();
                } catch (NullPointerException e){
                    return null;
                }
                if (trackCount == 0) return null;

                return results.tracks;

            } catch (IOError e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            topTracksAdapter.clear();
            if (tracks != null ){
                for (Track track : tracks){
                    topTracksAdapter.add(track);
                }
            }
        }
    }
}
