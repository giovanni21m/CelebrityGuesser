package com.giovannimartinus.celebrityguesser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    final CelebGuess celebGuess = new CelebGuess();

    Button answerOne;
    Button answerTwo;
    Button answerThree;
    Button answerFour;
    ImageView celebImageView;
    RelativeLayout gamePlayLayout;
    RelativeLayout gameStartLayout;

    class CelebGuess {

        private void answerSelection() {}

    }

    public void answerButton(View view) {
        celebGuess.answerSelection();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answerOne = (Button) findViewById(R.id.answerOne);
        answerTwo = (Button) findViewById(R.id.answerTwo);
        answerThree = (Button) findViewById(R.id.answerThree);
        answerFour = (Button) findViewById(R.id.answerFour);
        celebImageView = (ImageView) findViewById(R.id.celebImageView);
        gamePlayLayout = (RelativeLayout) findViewById(R.id.gamePlayLayout);
        gameStartLayout = (RelativeLayout) findViewById(R.id.gameStartLayout);
    }
}