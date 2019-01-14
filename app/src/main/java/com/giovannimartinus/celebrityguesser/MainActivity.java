package com.giovannimartinus.celebrityguesser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    final CelebGuess celebGuess = new CelebGuess();

    Button answerOne;
    Button answerTwo;
    Button answerThree;
    Button answerFour;
    Button playButton;

    ImageView celebImageView;

    GridLayout answerButtonLayout;

    RelativeLayout gamePlayLayout;
    RelativeLayout gameStartLayout;

    class CelebGuess {

        boolean isActive = false;

        private void buttonEnabled(GridLayout gridLayout) {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                View child = gridLayout.getChildAt(i);
                if (child.isEnabled()) {
                    child.setEnabled(false);
                } else {
                    child.setEnabled(true);
                }
            }
        }

        private void answerSelection() {}

        private void startGame() {
            if (isActive == false) {
                isActive = true;
                buttonEnabled(answerButtonLayout);
                gameStartLayout.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void answerButton(View view) {
        celebGuess.answerSelection();
    }

    public void startButton(View view) {
        celebGuess.startGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answerOne = (Button) findViewById(R.id.answerOne);
        answerTwo = (Button) findViewById(R.id.answerTwo);
        answerThree = (Button) findViewById(R.id.answerThree);
        answerFour = (Button) findViewById(R.id.answerFour);
        playButton = (Button) findViewById(R.id.playButton);

        celebImageView = (ImageView) findViewById(R.id.celebImageView);

        answerButtonLayout = (GridLayout) findViewById(R.id.answerButtonLayout);

        gamePlayLayout = (RelativeLayout) findViewById(R.id.gamePlayLayout);
        gameStartLayout = (RelativeLayout) findViewById(R.id.gameStartLayout);
    }
}