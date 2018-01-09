package br.com.gustavo.bakingapp.data.source.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gustavomagalhaes on 1/6/18.
 */

public class BakingDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "baking";
    private static final int NUMBER_VERSION_DB = 1;

    public BakingDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, NUMBER_VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the plants data
        final String SQL_CREATE_PLANTS_TABLE = "CREATE TABLE " + BakingContract.IngredientEntry.TABLE_NAME + " (" +
                BakingContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BakingContract.IngredientEntry.COLUMN_QUATITY + " NUMERIC NOT NULL, " +
                BakingContract.IngredientEntry.COLUMN_MEASURE + " VARCHAR(100) NOT NULL, " +
                BakingContract.IngredientEntry.COLUMN_INGREDIENT+ " VARCHAR(100) NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_PLANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BakingContract.IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
