package com.gregoriopalama.udacity.bakingapp.ui.image;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.gregoriopalama.udacity.bakingapp.util.ConnectivityUtils;
import com.jakewharton.disklrucache.DiskLruCache;
import com.squareup.picasso.Downloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.inject.Inject;


/**
 * OkHttp Downloader for a video frame. It uses 2 levels of cache for the frame to avoid
 * high levels of traffic usage
 *
 * @see com.gregoriopalama.udacity.bakingapp.ui.bindingadapters.RecipeThumbBindingAdapter
 * @author Gregorio Palamà
 */

public class VideoThumbnailDownloader implements Downloader {
    public final String TAG = "VideoThumbDownloader";
    private Context context;
    private DiskLruCache diskCache = null;
    private LruCache<String, Bitmap> memoryCache = null;

    MediaMetadataRetriever metadataRetriever;

    @Inject
    public VideoThumbnailDownloader(LruCache memoryCache, DiskLruCache diskCache, Application application) {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
        this.context = application;
    }


    @Override
    public Response load(Uri uri, int networkPolicy) throws IOException {
        Bitmap frame;
        boolean loadedFromCache = false;

        String key = getHashKey(uri.toString());
        Log.d(TAG, "Trying to load image from memory cache with key " + key);
        frame = getBitmapFromMemCache(key);

        if (frame != null) {
            Log.d(TAG, "Memory cache hit with key " + key);
            loadedFromCache = true;
            return new Response(bitmap2Stream(frame), loadedFromCache, -1);
        }

        Log.d(TAG, "Memory cache miss, trying with disk cache");
        frame = getBitmapFromDiskCache(key);

        if (frame == null) {
            Log.d(TAG, "Disk cache miss with key " + key);
            if (!ConnectivityUtils.isConnectedThroughWifi(this.context)) {
                /* This will stop the loading, preventing unwanted network usage */
                throw new IOException("Cannot load video thumbnail without a wifi connection");
            }

            Log.d(TAG, "Performing network fetch");
            try {
                metadataRetriever = new MediaMetadataRetriever();
                metadataRetriever.setDataSource(uri.toString(), new HashMap<>());

                frame = metadataRetriever.getFrameAtTime();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                metadataRetriever.release();
            }

            if (frame != null) {
                Log.d(TAG, "Image loaded from network, adding it to disk cache");
                addBitmapToDiskCache(key, frame);
            } else {
                throw new IOException("Error while retrieving thumbnail from video");
            }
        } else {
            Log.d(TAG, "Disk cache hit with key " + key);
            loadedFromCache = true;
        }

        Log.d(TAG, "Adding image to memory cache");
        addBitmapToMemoryCache(key, frame);

        return new Response(bitmap2Stream(frame), loadedFromCache, -1);

    }

    @Override
    public void shutdown() {

    }

    private void addBitmapToDiskCache(String key, Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        /* compressing the image, so it will not take too much space on disk */
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

        byte[] bitmapData = outputStream.toByteArray();
        DiskLruCache.Editor editor;
        editor = diskCache.edit(key);

        OutputStream out = editor.newOutputStream(0);
        out.write(bitmapData);
        out.close();

        editor.commit();
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    private Bitmap getBitmapFromDiskCache(String key) throws IOException {
        DiskLruCache.Snapshot snapshot = diskCache.get(key);
        if (snapshot == null)
            return null;

        InputStream in = snapshot.getInputStream(0);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buff = new byte[8000];
        while((bytesRead = in.read(buff)) != -1) {
            outputStream.write(buff, 0, bytesRead);
        }

        byte[] data = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return BitmapFactory.decodeStream(inputStream);
    }


    public String getHashKey(String key) {
        String hashKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            hashKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            hashKey = String.valueOf(key.hashCode());
        }
        return hashKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private InputStream bitmap2Stream(Bitmap bitmap) {
        ByteArrayInputStream inputStream;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] bitmapData = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(bitmapData);
        return inputStream;
    }
}

