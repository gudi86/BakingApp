package br.com.gustavo.bakingapp.data.source.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gustavomagalhaes on 1/6/18.
 */

public class BakingContract {

    public static final String AUTHORITY = "br.com.gustavo.baking";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_INGREDIENT = "ingredient";

    public static final long INVALID_ID = -1;

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_QUATITY = "quatity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";


    }
}
