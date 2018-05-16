package com.gregoriopalama.udacity.bakingapp;

/**
 * Holds constant values for all the application
 *
 * @author Gregorio Palamà
 */

public class Constants {
    public final static String RECIPE_SERVICE_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public final static String EXTRA_RECIPE = "recipe";
    public final static String EXTRA_STEPS = "steps";
    public final static String EXTRA_STEP_POSITION = "step_position";
    public final static String EXTRA_RECIPE_NAME = "recipe_name";
    public final static String CURRENT_STEP_POSITION = "position";

    public final static String EXTRA_WIDGET_REQUESTED_RECIPE = "requested_recipe";
    public final static String EXTRA_WIDGET_INGREDIENTS = "ingredients";
    public final static String EXTRA_WIDGET_ID = "widget_id";

    public static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    public static final String DISK_CACHE_IMAGE_DIR = "recipes_thumbnails";

    public static final String DISK_CACHE_VIDEO_DIR = "recipes_videos";
}
