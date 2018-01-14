package br.com.gustavo.bakingapp.data.source.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gustavomagalhaes on 1/6/18.
 */

public class BakingDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "baking";
    private static final int NUMBER_VERSION_DB = 1;
    private static final String LOG_TAG = BakingDataBaseHelper.class.getName();

    public BakingDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, NUMBER_VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(LOG_TAG, "onCreate");

        // Create a table to hold the plants data
        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + BakingContract.IngredientEntry.TABLE_NAME + " (" +
                BakingContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BakingContract.IngredientEntry.COLUMN_QUATITY + " NUMERIC NOT NULL, " +
                BakingContract.IngredientEntry.COLUMN_MEASURE + " VARCHAR(100) NOT NULL, " +
                BakingContract.IngredientEntry.COLUMN_INGREDIENT+ " VARCHAR(100) NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);

        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + BakingContract.RecipeEntry.TABLE_NAME + " (" +
                BakingContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BakingContract.RecipeEntry.COLUMN_RECIPE_ID + " NUMERIC NOT NULL, " +
                BakingContract.RecipeEntry.COLUMN_NAME + " VARCHAR(100) NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.IngredientEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        Log.d(LOG_TAG, "open");
    }
}
