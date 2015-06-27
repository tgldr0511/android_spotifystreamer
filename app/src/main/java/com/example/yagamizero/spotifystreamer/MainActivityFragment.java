package com.example.yagamizero.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment for searching artists.
 */
public class MainActivityFragment extends Fragment {

    EditText searchQuery;
    ArtistSearchAdapter artistSearchAdapter;
    List<Artist> resultArtists;


    public MainActivityFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        resultArtists = new ArrayList<Artist>();
        artistSearchAdapter = new ArtistSearchAdapter(
                getActivity(),
                resultArtists
        );

        ListView artistListView = (ListView) rootView.findViewById(R.id.listview_artists);

        artistListView.setAdapter(artistSearchAdapter);

        artistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String artistId = artistSearchAdapter.getItem(position).id;
                String artistName = artistSearchAdapter.getItem(position).name;
                Intent intent = new Intent(getActivity(), TopTracks.class)
                        .putExtra(Intent.EXTRA_TEXT, artistId)
                        .putExtra("artistName", artistName);
                startActivity(intent);
            }
        });



        searchQuery = (EditText) rootView.findViewById(R.id.searchQuery);
        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    FetchArtistTask fetchArtistTask = new FetchArtistTask();
                    fetchArtistTask.execute(s.toString());
                } else {
                    // search bar is empty
                    artistSearchAdapter.clear();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String, Void, List<Artist>> {


        @Override
        protected List<Artist> doInBackground(String... params) {

            if (params.length == 0){
                return null;
            }

            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                ArtistsPager results = spotify.searchArtists(params[0]);

                int artistCount;
                try {
                    artistCount = results.artists.total;
                } catch(NullPointerException e){
                    return null;
                }
                if (artistCount == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.not_found, Toast.LENGTH_SHORT).show();
                    return null;
                }

                return results.artists.items;
            } catch (IOError e){

                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            artistSearchAdapter.clear();
            if (artists != null){
                for(Artist artist : artists){
                    artistSearchAdapter.add(artist);
                }
            }
        }
    }
}
