package com.giovannimartinus.celebrityguesser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public class ContentDownload extends AsyncTask<String, Void, String> {

        // download specific content from the returned result
        private void downloadedContent() {
            String result = null;
            try {
                result = contentDownload.execute("http://www.posh24.se/kandisar").get();

                String[] splitResult = result.split("<div class=\"sidebarContainer\">");

                // get image urls
                Pattern p = Pattern.compile("<img src=\"(.*?)\"");
                Matcher m = p.matcher(splitResult[0]);

                while (m.find()) {
                    System.out.println("Image URL: " + m.group(1));
                }

                // get celeb name
                p = Pattern.compile("alt=\"(.*?)\"");
                m = p.matcher(splitResult[0]);

                while (m.find()) {
                    System.out.println("Celeb Name: " + m.group(1));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // get url, connect to browser, and get data
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                // create instance with url and connect to to browser
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                // download the input stream at once
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(inputStream);

                // read data from stream and write to string
                int data = streamReader.read();
                while (data != -1) {
                    char currentChar = (char) data;
                    result += currentChar;
                    data = streamReader.read();
                }

                return result;
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

        contentDownload.downloadedContent();
    }
}