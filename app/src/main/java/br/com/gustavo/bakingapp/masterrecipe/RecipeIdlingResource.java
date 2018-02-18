package br.com.gustavo.bakingapp.masterrecipe;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gustavomagalhaes on 2/3/18.
 */

public class RecipeIdlingResource implements IdlingResource {

    private AtomicBoolean isIdle = new AtomicBoolean(false);
    private ResourceCallback myCallback;

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        myCallback = callback;
    }

    public void setIdleNow(boolean isIdleNow) {
        this.isIdle.set(isIdleNow);
        if (isIdleNow && myCallback != null) {
            myCallback.onTransitionToIdle();
        }
    }
}
