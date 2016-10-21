package com.example.lily_chen.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lily_chen.flickster.R;
import com.example.lily_chen.flickster.models.Movie;
import com.example.lily_chen.flickster.utils.BitmapScaler;
import com.example.lily_chen.flickster.utils.DeviceDimensionsHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by lily_chen on 10/18/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    Drawable moviePlaceholder;
    Drawable moviePlaceholderLand;
    int screenWidth;

    private static class ViewHolder {
        ImageView image;
        ImageView backdropImage;
        TextView overview;
        TextView originalTitle;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie, movies);
        screenWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());
        scalePlaceholders();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getDisplayType().ordinal();
    }

    // Total number of types is the number of enum values
    @Override
    public int getViewTypeCount() {
        return Movie.DisplayType.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for the position
        Movie movie = getItem(position);

        ViewHolder viewHolder;
        // check the existing view being reused
        if (convertView == null) {
            viewHolder = new ViewHolder();
            int type = getItemViewType(position);
            convertView = getInflatedLayoutForType(type);
            viewHolder.backdropImage = (ImageView) convertView.findViewById(R.id.ivBackdropOnly);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.originalTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // if movie is popular, we only show backdrop image
        if (viewHolder.backdropImage != null ) {
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .placeholder(moviePlaceholderLand)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .resize((int) (screenWidth*.7), 0)
                    .into(viewHolder.backdropImage);
        } else {
            // otherwise, populate data
            viewHolder.originalTitle.setText(movie.getOriginalTitle());
            viewHolder.overview.setText(movie.getOverview());
            viewHolder.image.setImageResource(0);

            int orientation = getContext().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.with(getContext()).load(movie.getBackdropPath())
                        .placeholder(moviePlaceholderLand)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .resize((int) (screenWidth*.5), 0)
                        .into(viewHolder.image);
            } else {
                Picasso.with(getContext()).load(movie.getPosterPath())
                        .placeholder(moviePlaceholder)
                        .transform(new RoundedCornersTransformation(10, 10))
                        .resize((int) (screenWidth*.4), 0)
                        .into(viewHolder.image);
            }
        }
        return convertView;
    }

    private View getInflatedLayoutForType(int type) {
        if (type == Movie.DisplayType.BACKDROP_ONLY.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_backdrop_only, null);
        } else if (type == Movie.DisplayType.NORMAL.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }

    // (BUG) Placeholders are not scaled to the same that picasso scales the images
    private void scalePlaceholders() {
        Bitmap bmMoviePlaceholder = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.movie_placeholder);
        Bitmap bmMoviePlaceholderLand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.movie_placeholder_land);
        moviePlaceholder = new BitmapDrawable(BitmapScaler.scaleToFitWidth(bmMoviePlaceholder, (int) (screenWidth*.5)));
        moviePlaceholderLand = new BitmapDrawable(BitmapScaler.scaleToFitWidth(bmMoviePlaceholderLand, (int) (screenWidth*.4)));
    }
}
