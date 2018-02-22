package br.com.gustavo.bakingapp.recipelist;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    private Recipe favorite;

    private OnClickRecipe onClickRecipe;


    public interface OnClickRecipe {
        public void onClickRecipeItem(Recipe recipe);

        public void onClickFavorite(Recipe recipe);
    }



    public AdapterRecipes(List<Recipe> recipes, Recipe favorite, OnClickRecipe onClickRecipe) {
        this.recipes = recipes;
        this.favorite = favorite;
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

        if (!TextUtils.isEmpty(recipe.getImage())) {
            Picasso
                    .with(holder.itemView.getContext())
                    .load(recipe.getImage())
                    .into(holder.ivRecipe);
        } else {
            holder.ivRecipe.setImageResource(R.drawable.default_recipe);
        }

        if (favorite != null && recipe.getId() == favorite.getId()) {
            holder.btnFavorite.setImageResource(R.drawable.icon_favorite_selected);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.icon_favorite);
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton btnFavorite;
        private ImageView ivRecipe;
        private TextView tvName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ivRecipe = itemView.findViewById(R.id.image_recipe);
            tvName = itemView.findViewById(R.id.text_name);
            btnFavorite = itemView.findViewById(R.id.image_favorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRecipe.onClickRecipeItem(recipes.get(getAdapterPosition()));
                }
            });

            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe currentRecipe = recipes.get(getAdapterPosition());
                    if (currentRecipe == favorite) {
                        favorite = null;
                    } else {
                        favorite = recipes.get(getAdapterPosition());
                    }
                    onClickRecipe.onClickFavorite(favorite);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
