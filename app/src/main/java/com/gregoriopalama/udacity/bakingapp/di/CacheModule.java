package com.gregoriopalama.udacity.bakingapp.di;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.gregoriopalama.udacity.bakingapp.Constants;
import com.gregoriopalama.udacity.bakingapp.ui.image.VideoThumbnailDownloader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for Memory and Disk Cache
 *
 * @author Gregorio Palamà
 */

@Module
public class CacheModule {
    @AppScope
    @Provides
    LruCache providesMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> memoryCache  = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        return memoryCache;
    }

    @AppScope
    @Provides
    DiskLruCache providesDiskCache(Application application) {
        final String cachePath = application.getCacheDir().getPath();

        File cacheDir =
                new File(cachePath + File.separator + Constants.DISK_CACHE_IMAGE_DIR);
        DiskLruCache diskCache = null;
        try {
            diskCache = DiskLruCache
                    .open(cacheDir,1, 1, Constants.DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diskCache;
    }

    @Provides
    VideoThumbnailDownloader providesVideoThumbnailDownloader(LruCache memoryCache,
                                                              DiskLruCache diskCache,
                                                              Application application) {
        return new VideoThumbnailDownloader(memoryCache, diskCache, application);
    }
}
