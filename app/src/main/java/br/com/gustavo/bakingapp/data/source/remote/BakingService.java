package br.com.gustavo.bakingapp.data.source.remote;

import java.util.List;

import br.com.gustavo.bakingapp.data.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gustavomagalhaes on 11/25/17.
 */

public interface BakingService {

    @GET("baking.json")
    Call<List<Recipe>> fetchRecipes();
}
