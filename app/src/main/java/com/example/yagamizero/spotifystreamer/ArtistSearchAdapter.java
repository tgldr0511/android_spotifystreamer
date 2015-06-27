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

import kaaes.spotify.webapi.android.models.Artist;

public class ArtistSearchAdapter extends ArrayAdapter<Artist>{

    public ArtistSearchAdapter(Activity context, List<Artist> artists){
        super(context, R.layout.listview_artists, artists);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Artist artist = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.listview_artists, parent, false);

        //inflating the image
        ImageView artistImage = (ImageView) rootView.findViewById(R.id.artistImage);
        String url;
        if (artist.images.isEmpty()){
            url = "https://s3-eu-west-1.amazonaws.com/assets.thebiggive.org.uk/static/images/image-not-found.png";
        } else {
            url = artist.images.get(0).url;
        }
        Glide.with(getContext())
                .load(url)
                .override(150, 150)
                .centerCrop()
                .into(artistImage);

        //setting the artist name
        TextView artistName = (TextView) rootView.findViewById(R.id.artistName);
        artistName.setText(artist.name);

        return rootView;
    }
}
