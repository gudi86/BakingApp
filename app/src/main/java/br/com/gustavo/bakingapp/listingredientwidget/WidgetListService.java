package br.com.gustavo.bakingapp.listingredientwidget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.BakingDataSourceImpl;

/**
 * Created by gustavomagalhaes on 1/3/18.
 */

public class WidgetListService extends RemoteViewsService {
    private static final String LOG_TAG = WidgetListService.class.getName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(LOG_TAG, "Chamou o metodo onGetViewFactory");
        return new ListIngredientRemoteViewfactory(
                getApplicationContext(),
                intent
        );
    }

    static class ListIngredientRemoteViewfactory implements RemoteViewsService.RemoteViewsFactory, WidgetListContract.View {

        private static final String LOG_TAG = ListIngredientRemoteViewfactory.class.getName();
        private Context context;
        private List<Ingredient> ingredients = null;
        private WidgetListContract.Presenter presenter;

        public ListIngredientRemoteViewfactory(Context context, Intent intent) {
            Log.d(LOG_TAG, "Chamou o metodo ListIngredientRemoteViewfactory");
            this.context = context;

            new WidgetListPresenter(BakingDataSourceImpl.getInstance(context), this);
        }

        @Override
        public void setPresenter(WidgetListContract.Presenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void showListOf(Recipe favorite) {
            this.ingredients = favorite.getIngredients();
        }

        @Override
        public void showNotListIngredient() {

        }

        @Override
        public void onCreate() {
            Log.d(LOG_TAG, "Executou o onCreate");
        }

        @Override
        public void onDataSetChanged() {
            Log.d(LOG_TAG, "Executou o onDataSetChanged");
            this.presenter.start();
        }

        @Override
        public void onDestroy() {
            Log.d(LOG_TAG, "Executou o onDestroy");
        }

        @Override
        public int getCount() {
            Log.d(LOG_TAG, "Executou o getCount");
            return ingredients == null?0:ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            Log.d(LOG_TAG, "Executou o getViewAt");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);

            Ingredient ingredient = ingredients.get(i);
            String item = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient();
            views.setTextViewText(R.id.tv_ingredient, item);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            Log.d(LOG_TAG, "Executou o getLoadingView");
            return null;
        }

        @Override
        public int getViewTypeCount() {
            Log.d(LOG_TAG, "Executou o getViewTypeCount");
            return 1;
        }

        @Override
        public long getItemId(int i) {
            Log.d(LOG_TAG, "Executou o getItemId");
            return i;
        }

        @Override
        public boolean hasStableIds() {
            Log.d(LOG_TAG, "Executou o hasStableIds");
            return true;
        }
    }
}

