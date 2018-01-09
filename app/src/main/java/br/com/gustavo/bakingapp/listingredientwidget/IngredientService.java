package br.com.gustavo.bakingapp.listingredientwidget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.listrecipes.MainActivity;

/**
 * Created by gustavomagalhaes on 1/2/18.
 */

public class IngredientService extends IntentService {

    public static final String ACTION_SET_RECIPE_FOR_WIDGET = "ACTION_SET_RECIPE_FOR_WIDGET";
    public static final String REFRESH_INGREDIENT = "REFRESH_INGREDIENT";

    public IngredientService() {
        super(IngredientService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getAction() != null &&
                intent.getAction().equals(IngredientService.ACTION_SET_RECIPE_FOR_WIDGET)) {
            IngredientWidget.setRecipeIngredient(getBaseContext(),
                    intent.getBooleanExtra(REFRESH_INGREDIENT, false));
        }
    }
}
