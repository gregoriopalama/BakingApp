package com.gregoriopalama.udacity.bakingapp.ui.bindingadapters;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.model.Recipe;
import com.gregoriopalama.udacity.bakingapp.ui.image.VideoThumbnailDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Binding adapter for the recipe's thumbnail.
 * If the recipe has a thumbnail, it is shown. Otherwise, if one of the recipe's steps has a
 * thumbnail, that is shown. In the last, if there are no thumbnails, a single frame from a
 * video is taken.
 *
 * This binding adapter uses 2 levels of caches. If the thumbnail is taken from the videos,
 * then this may be traffic consuming. The frame from the video is taken only if there is
 * a wifi connection or if the frame has already been downloaded and put in the cache.
 *
 * @see VideoThumbnailDownloader
 * @author Gregorio Palamà
 */

public class RecipeThumbBindingAdapter {
    private final VideoThumbnailDownloader videoThumbnailDownloader;

    @Inject
    public RecipeThumbBindingAdapter(VideoThumbnailDownloader videoThumbnailDownloader) {
        this.videoThumbnailDownloader = videoThumbnailDownloader;
    }

    @BindingAdapter({"recipe"})
    public void loadRecipeThumb(ImageView imageView, Recipe recipe) {

        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(imageView.getContext())
                    .load(recipe.getImage())
                    .into(imageView);
            return;
        }

        for (int i = recipe.getSteps().size() - 1; i >= 0; i--) {
            if (!TextUtils.isEmpty(recipe.getSteps().get(i).getThumbnailUrl())) {
                Picasso.with(imageView.getContext())
                        .load(recipe.getSteps().get(i).getThumbnailUrl())
                        .error(imageView.getContext()
                                .getResources().getDrawable(R.drawable.ic_cake))
                        .into(imageView);
                return;
            }

            if (!TextUtils.isEmpty(recipe.getSteps().get(i).getVideoUrl())) {
                Picasso picassoInstance = new Picasso.Builder(imageView.getContext())
                        .downloader(videoThumbnailDownloader)
                        .build();
                picassoInstance
                        .load(recipe.getSteps().get(i).getVideoUrl())
                        .placeholder(imageView.getContext()
                                .getResources().getDrawable(R.drawable.ic_cake))
                        .error(imageView.getContext()
                                .getResources().getDrawable(R.drawable.ic_cake))
                        .fit()
                        .into(imageView);
                return;
            }
        }

    }
}
