package com.gregoriopalama.udacity.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.gregoriopalama.udacity.bakingapp.R;
import com.gregoriopalama.udacity.bakingapp.ui.list.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_ID;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_INGREDIENTS;
import static com.gregoriopalama.udacity.bakingapp.Constants.EXTRA_WIDGET_REQUESTED_RECIPE;

/**
 * WidgeData provider for the list of ingredients in the widget
 *
 * @author Gregorio Palamà
 */

public class RecipeWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int appWidgetId;
    private List<String> ingredients;

    public RecipeWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(EXTRA_WIDGET_ID, 0);
        this.ingredients = (ArrayList<String>) intent.getSerializableExtra(EXTRA_WIDGET_INGREDIENTS);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null)
            return 0;

        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_ingredient);
        remoteViews.setTextViewText(R.id.ingredient,
                ingredients.get(position));
        CharSequence recipeName = RecipeIngredientsWidgetConfigureActivity
                .loadRecipePreference(context, appWidgetId);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_WIDGET_REQUESTED_RECIPE, recipeName);
        remoteViews.setOnClickFillInIntent(R.id.widget_ingredient_layout, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.widget_ingredient);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
