package com.example.yagamizero.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Adapter for the top 10 tracks
 */
public class TopTracksAdapter extends ArrayAdapter<Track>{
    public TopTracksAdapter(Activity context, List<Track> tracks){
        super(context, R.layout.listview_tracks, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.listview_tracks, parent, false);

        ImageView trackImage = (ImageView) rootView.findViewById(R.id.trackImage);
        String url;
        if (track.album.images.isEmpty()){
            url = "https://s3-eu-west-1.amazonaws.com/assets.thebiggive.org.uk/static/images/image-not-found.png";
        } else {
            url = track.album.images.get(0).url;
        }

        Glide.with(getContext())
                .load(url)
                .override(150, 150)
                .centerCrop()
                .into(trackImage);

        TextView trackName = (TextView) rootView.findViewById(R.id.trackName);
        trackName.setText(track.name);

        TextView albumName = (TextView) rootView.findViewById(R.id.albumName);
        albumName.setText(track.album.name);

        return rootView;
    }
}
