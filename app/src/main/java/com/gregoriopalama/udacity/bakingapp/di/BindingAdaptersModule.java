package com.gregoriopalama.udacity.bakingapp.di;

import com.gregoriopalama.udacity.bakingapp.ui.bindingadapters.RecipeThumbBindingAdapter;
import com.gregoriopalama.udacity.bakingapp.ui.image.VideoThumbnailDownloader;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for Thumbnail Binding Adapter
 *
 * @author Gregorio Palamà
 */

@Module(includes = {CacheModule.class})
public class BindingAdaptersModule {
    @Provides
    RecipeThumbBindingAdapter provideRecipeThumbBindingAdapter(VideoThumbnailDownloader videoThumbnailDownloader) {
        return new RecipeThumbBindingAdapter(videoThumbnailDownloader);
    }
}
