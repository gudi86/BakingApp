package br.com.gustavo.bakingapp.listingredientwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.listrecipes.MainActivity;

/**
 * Created by gustavomagalhaes on 12/28/17.
 */

public class IngredientWidget extends AppWidgetProvider {

    private static final String LOG_TAG = IngredientWidget.class.getName();

    public static final String REFRESH_LIST_INGREDIENTS = "REFRESH_LIST_INGREDIENTS";

    public static void setRecipeIngredient(Context context, boolean isUpdateIngredients) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientWidget.class));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list);

        if (isUpdateIngredients) {
            Intent intent = new Intent(context, ListWidgetService.class);
            // Add the app widget ID to the intent extras.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setViewVisibility(R.id.widget_lst_ingredient, View.VISIBLE);
            views.setRemoteAdapter(R.id.widget_lst_ingredient, intent);
            appWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.widget_lst_ingredient);
        } else {
            views.setViewVisibility(R.id.widget_lst_ingredient, View.GONE);
        }
        appWidgetManager.updateAppWidget(appIds, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(IngredientService.ACTION_SET_RECIPE_FOR_WIDGET);
        context.startService(intent);

    }


}
