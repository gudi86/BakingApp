package br.com.gustavo.bakingapp.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.gustavo.bakingapp.BuildConfig;
import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.database.BakingContract;
import br.com.gustavo.bakingapp.data.source.database.BakingDataBase;
import br.com.gustavo.bakingapp.data.source.remote.BakingService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gustavomagalhaes on 11/25/17.
 */

public class BakingDataSourceImpl implements BakingDataSource {

    private static final String LOG_TAG = BakingDataSourceImpl.class.getName();
    private Context context;

    private List<Recipe> recipes = null;

    private static BakingDataSourceImpl dataSource;

    private BakingDataSourceImpl(@NonNull Context context) {
        this.context = context;
    }

    public static BakingDataSource getInstance(@NonNull Context context) {
        if (dataSource == null) {
            dataSource = new BakingDataSourceImpl(context);
        }
        return dataSource;
    }

    @Override
    public void fetchRecipes(final OnFetchRecipe fetchRecipe) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(
                        GsonConverterFactory.create()
                )
                .client(
                        builder.build()
                )
                .build();

        retrofit.create(BakingService.class).fetchRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.body() == null) {
                    fetchRecipe.onFailFetch(new Throwable("Body is empyt"));
                }

                recipes = response.body();
                fetchRecipe.onRecipeLoad(recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                fetchRecipe.onFailFetch(t);
            }
        });
    }

    @Override
    public void addFavorite(Recipe recipe, final OnSaveIngredient callback) {

        context.getContentResolver().delete(BakingContract.IngredientEntry.CONTENT_URI, null, null);
        context.getContentResolver().delete(BakingContract.RecipeEntry.CONTENT_URI, null, null);

        ContentValues recipeValue = new ContentValues();
        recipeValue.put(BakingContract.RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
        recipeValue.put(BakingContract.RecipeEntry.COLUMN_NAME, recipe.getName());

        context.getContentResolver().insert(BakingContract.RecipeEntry.CONTENT_URI, recipeValue);

        ContentValues[] contentValues = new ContentValues[recipe.getIngredients().size()];

        for (int i=0; i < contentValues.length; i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(BakingContract.IngredientEntry.COLUMN_QUATITY, ingredient.getQuantity());
            contentValue.put(BakingContract.IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
            contentValue.put(BakingContract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
            contentValues[i] = contentValue;
        }

        ContentObserver observer = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                context.getContentResolver().unregisterContentObserver(this);
                callback.onSave();
            }
        };

        context.getContentResolver().registerContentObserver(
                BakingContract.IngredientEntry.CONTENT_URI,
                false,
                observer);


        int size = context.getContentResolver().bulkInsert(
                BakingContract.IngredientEntry.CONTENT_URI,
                contentValues
        );

        if (contentValues.length != size) {
            context.getContentResolver().unregisterContentObserver(observer);

            observer = new ContentObserver(new Handler(Looper.getMainLooper())) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    context.getContentResolver().unregisterContentObserver(this);
                    callback.onFail();
                }
            };

            context.getContentResolver().registerContentObserver(
                    BakingContract.IngredientEntry.CONTENT_URI,
                    false,
                    observer);

            context.getContentResolver().delete(BakingContract.IngredientEntry.CONTENT_URI, null, null);
        } else
            Log.d(LOG_TAG, "It adds new favorite recipe.");
    }

    @Override
    public void removeFavorite(final OnRemoveIngredient callback) {

        ContentObserver observer = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                context.getContentResolver().unregisterContentObserver(this);
                callback.onRemove();
            }
        };

        context.getContentResolver().registerContentObserver(
                BakingContract.RecipeEntry.CONTENT_URI,
                false,
                observer);

        context.getContentResolver().delete(BakingContract.IngredientEntry.CONTENT_URI, null, null);
        context.getContentResolver().delete(BakingContract.RecipeEntry.CONTENT_URI, null, null);
    }

    @Override
    public void fetchFavorite(final OnFetchRecipeFavorite onFetchIngredient) {

        Cursor cursor = context.getContentResolver().query(
                BakingContract.RecipeEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        Recipe recipe = null;
        if (cursor != null && cursor.moveToFirst()) {
            recipe = new Recipe();
            recipe.setId(cursor.getInt(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_RECIPE_ID)));
            recipe.setName(cursor.getString(cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_NAME)));

            cursor = context.getContentResolver().query(
                    BakingContract.IngredientEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            List<Ingredient> ingredients = new ArrayList<>();
            if (cursor != null) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_QUATITY)));
                    ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_INGREDIENT)));
                    ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_MEASURE)));
                    ingredients.add(ingredient);
                }

                if (!cursor.isClosed()) {
                    cursor.close();
                }
                recipe.setIngredients(ingredients);
            }
        }
        onFetchIngredient.onfetchFavorite(recipe);
    }

    @Override
    public void fetchRecipeBy(int id, IFetchRecipe iFetchRecipe) {
        Recipe fetchRecipe = null;
        for (Recipe recipe:recipes) {
            if (recipe.getId() == id) {
                fetchRecipe = recipe; break;
            }
        }
        iFetchRecipe.getRecipe(fetchRecipe);
    }
}
