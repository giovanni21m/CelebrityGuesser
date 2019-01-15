package com.giovannimartinus.celebrityguesser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    final ContentDownload contentDownload = new ContentDownload();
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

    // web content downloading class
    public class ContentDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                //create instance with url and connect to to browser
                URL url = new URL(urls[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                // download the input stream at once and convert to bitmap
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                // check if internet is connected
            } catch (IOException e) {
                e.printStackTrace();
            }
            // if nothing return null
            return null;
        }
    }

    // game play/state class
    private class CelebGuess {

        boolean isActive = false;

        private void downloadedContent() {}

        // enable/disable answer buttons
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

        // start game
        private void startGame() {
            if (isActive == false) {
                isActive = true;
                buttonEnabled(answerButtonLayout);
                gameStartLayout.setVisibility(View.INVISIBLE);
                playButton.setEnabled(false);
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