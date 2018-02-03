package br.com.gustavo.bakingapp.listingredientwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.source.database.BakingContract;
import br.com.gustavo.bakingapp.recipelist.MainActivity;

/**
 * Created by gustavomagalhaes on 12/28/17.
 */

public class IngredientWidget extends AppWidgetProvider {

    private static final String LOG_TAG = IngredientWidget.class.getName();

    public static final String ADD_LIST_INGREDIENTS_FAVORITE = "ADD_LIST_INGREDIENTS_FAVORITE";
    public static final String LOAD_RECIPE_FAVORITE = "LOAD_RECIPE_FAVORITE";

    public IngredientWidget() {
        Log.d(LOG_TAG, "Teste");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive");
        if (intent.getAction().equals(ADD_LIST_INGREDIENTS_FAVORITE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), IngredientWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(LOG_TAG, "onUpdate");
        for (int i = 0; i < appWidgetIds.length; i++) {
            Log.d(LOG_TAG, "appWidgetId: " + appWidgetIds[i]);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list);

            Cursor cursor = context.getContentResolver().query(BakingContract.RecipeEntry.CONTENT_URI, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                views.setViewVisibility(R.id.layout_recipe, View.VISIBLE);
                Intent intent = new Intent(context, WidgetListService.class);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                views.setViewVisibility(R.id.widget_lst_ingredient, View.VISIBLE);

                // Set button click
                Intent intentClick = new Intent(context, MainActivity.class);
                intentClick.setAction(LOAD_RECIPE_FAVORITE);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.button_go_recipe, pendingIntent);

                views.setTextViewText(R.id.text_title_recipe, cursor.getString(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_NAME)));

                views.setRemoteAdapter(R.id.widget_lst_ingredient, intent);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_lst_ingredient);
            } else {
                views.setViewVisibility(R.id.layout_recipe, View.GONE);
            }
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


}
