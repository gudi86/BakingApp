package br.com.gustavo.bakingapp.data.source.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gustavomagalhaes on 1/6/18.
 */

public class BakingContentProvider extends ContentProvider {

    private static final String LOG_TAG = BakingContentProvider.class.getName();

    public static final int INGREDIENTS = 10;
    public static final int RECIPE = 11;

    private static final UriMatcher matcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENT, INGREDIENTS);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_RECIPE, RECIPE);

        return uriMatcher;
    }

    private BakingDataBaseHelper dataBaseHelper;

    @Override
    public boolean onCreate() {
        dataBaseHelper = new BakingDataBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        int matchReturn = matcher.match(uri);

        Cursor cursor = null;
        switch (matchReturn) {
            case INGREDIENTS :
                cursor = db.query(BakingContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPE:
                cursor = db.query(BakingContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

        int match = matcher.match(uri);
        Uri uriReturn = null;
        switch (match) {
            case RECIPE:

                try {
                    db.beginTransaction();

                    long id = db.insert(BakingContract.RecipeEntry.TABLE_NAME,null, contentValues);

                    if (id > 0)
                        uriReturn = ContentUris.withAppendedId(uri, id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] args) {

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int count = 0;
        int match = matcher.match(uri);
        switch (match) {
            case INGREDIENTS:
                try {
                    db.beginTransaction();
                    count = db.delete(BakingContract.IngredientEntry.TABLE_NAME, where, args);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                break;
            case RECIPE:
                try {
                    db.beginTransaction();
                    count = db.delete(BakingContract.RecipeEntry.TABLE_NAME, where, args);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int numberOfRegisterSave = 0;
        int match = matcher.match(uri);
        switch (match) {
            case INGREDIENTS :

                try {
                    db.beginTransaction();
                    for (ContentValues value :
                            values) {
                        long id = db.insert(BakingContract.IngredientEntry.TABLE_NAME, null, value);
                        if (id > 0) {
                            numberOfRegisterSave++;
                        }
                        Log.d(LOG_TAG, "Insert ingredient: " + id);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (numberOfRegisterSave == values.length) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numberOfRegisterSave;
    }
}
