package br.com.gustavo.bakingapp.masterrecipe.recipesteplist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.gustavo.bakingapp.R;
import br.com.gustavo.bakingapp.data.model.Ingredient;
import br.com.gustavo.bakingapp.data.model.Step;

/**
 * Created by gustavomagalhaes on 11/28/17.
 */

public class AdapterStepDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HOLDER_INGREDIENT = 0;
    private static final int VIEW_HOLDER_STEP = 1;

    public interface OnClickStep {
        public void onClickStep(Step step);
    }

    private List<Ingredient> ingredients;
    private List<Step> steps;

    private OnClickStep onClickStep;


    public AdapterStepDetail(List<Ingredient> ingredients, List<Step> steps, OnClickStep onClickStep) {
        this.ingredients = ingredients;
        this.steps = steps;
        this.onClickStep = onClickStep;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_HOLDER_INGREDIENT;
        else return VIEW_HOLDER_STEP;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLDER_INGREDIENT) {
            return new ViewHolderIngredient(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false)
            );
        } else if (viewType == VIEW_HOLDER_STEP) {
            return new ViewHolderStep(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false)
            );
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderIngredient) {
            ViewHolderIngredient vhIngredient = (ViewHolderIngredient) holder;

            StringBuffer labelIngredient = new StringBuffer("");
            for (Ingredient i : ingredients) {
                labelIngredient
                        .append(i.getQuantity())
                        .append(" ")
                        .append(i.getMeasure())
                        .append(" ")
                        .append(i.getIngredient())
                        .append("\n");
            }
            vhIngredient.ingredient.setText(labelIngredient.toString());

        } else if (holder instanceof ViewHolderStep) {
            ViewHolderStep vhStep = (ViewHolderStep) holder;
            vhStep.stepDescription.setText(steps.get(position - 1).getShortDescription());
        }

    }

    @Override
    public int getItemCount() {
        return steps.size() + 1;
    }

    private class ViewHolderIngredient extends RecyclerView.ViewHolder {

        TextView ingredient;

        public ViewHolderIngredient(View itemView) {
            super(itemView);
            ingredient = (TextView) itemView.findViewById(R.id.tv_ingredient);
        }
    }

    private class ViewHolderStep extends RecyclerView.ViewHolder {

        TextView stepDescription;

        public ViewHolderStep(View itemView) {
            super(itemView);

            stepDescription = (TextView) itemView.findViewById(R.id.tv_step_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickStep.onClickStep(steps.get(getAdapterPosition() - 1));
                }
            });
        }
    }
}
