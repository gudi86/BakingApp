package br.com.gustavo.bakingapp.data.source;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.gustavo.bakingapp.BuildConfig;
import br.com.gustavo.bakingapp.data.model.Recipe;
import br.com.gustavo.bakingapp.data.source.remote.BakingRemote;
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

    private static BakingDataSourceImpl dataSource;

    private BakingDataSourceImpl(@NonNull Context context) {

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

                fetchRecipe.onRecipeLoad(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                fetchRecipe.onFailFetch(t);
            }
        });
    }
}
