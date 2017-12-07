package br.com.gustavo.bakingapp.stepvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import br.com.gustavo.bakingapp.R;

/**
 * Created by gustavomagalhaes on 12/2/17.
 */

public class StepVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_step_recipe);
    }
}
