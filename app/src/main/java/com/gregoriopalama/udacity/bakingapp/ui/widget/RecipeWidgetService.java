package com.gregoriopalama.udacity.bakingapp.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Service that creates the remote view for the list of ingredients in the widget
 *
 * @author Gregorio Palamà
 */

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetDataProvider(this,intent);
    }
}