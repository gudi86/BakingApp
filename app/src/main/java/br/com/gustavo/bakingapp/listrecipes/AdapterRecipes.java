package br.com.gustavo.bakingapp.listrecipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Recipe;

/**
 * Created by gustavomagalhaes on 11/25/17.
 */

public class AdapterRecipes extends RecyclerView.Adapter<AdapterRecipes.RecipeViewHolder> {

    private List<Recipe> recipes;

    private OnClickRecipe onClickRecipe;


    public interface OnClickRecipe {
        public void onClickRecipeItem(Recipe recipe);
    }



    public AdapterRecipes(List<Recipe> recipes, OnClickRecipe onClickRecipe) {
        this.recipes = recipes;
        this.onClickRecipe = onClickRecipe;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        Recipe recipe = recipes.get(position);

        holder.tvName.setText(recipe.getName());

        if (recipe.getImage() != null && !recipe.getImage().equals("")) {
            Picasso
                    .with(holder.itemView.getContext())
                    .load(recipe.getImage())
                    .into(holder.ivRecipe);
        } else {
            holder.ivRecipe.setImageResource(R.drawable.default_recipe);
        }


    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivRecipe;
        private TextView tvName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ivRecipe = (ImageView) itemView.findViewById(R.id.ivRecipe);
            tvName = (TextView) itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRecipe.onClickRecipeItem(recipes.get(getAdapterPosition()));
                }
            });
        }
    }
}
