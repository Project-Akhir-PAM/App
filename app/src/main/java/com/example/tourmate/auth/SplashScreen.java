package com.example.tourmate.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tourmate.R;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    CardView splashCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashCard = findViewById(R.id.splashCard);
        splashCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.splashCard) {
            splashCard.animate().alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            });
        }
    }
}
