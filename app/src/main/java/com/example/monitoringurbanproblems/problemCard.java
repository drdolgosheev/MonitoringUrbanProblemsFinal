package com.example.monitoringurbanproblems;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class problemCard extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


        View v = new View(problemCard.this);

        v.setClipToOutline(true);

        setContentView(R.layout.activity_problem_card);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {

    }
}
