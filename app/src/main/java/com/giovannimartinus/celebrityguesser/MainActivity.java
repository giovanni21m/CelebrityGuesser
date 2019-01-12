package com.giovannimartinus.celebrityguesser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final CelebGuess celebGuess = new CelebGuess();

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
    }
}
