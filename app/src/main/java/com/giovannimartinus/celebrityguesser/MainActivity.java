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
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    final ContentDownload contentDownload = new ContentDownload();
    final CelebGuess celebGuess = new CelebGuess();

    ArrayList<String> imageUrls = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();

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

                // split the result at this line
                String[] splitResult = result.split("<div class=\"sidebarContainer\">");

                // get image urls
                Pattern p = Pattern.compile("<img src=\"(.*?)\"");
                Matcher m = p.matcher(splitResult[0]);

                // add to ArrayList
                while (m.find()) {
                    imageUrls.add(m.group(1));
                }

                // get celeb name
                p = Pattern.compile("alt=\"(.*?)\"");
                m = p.matcher(splitResult[0]);

                // add to ArrayList
                while (m.find()) {
                    celebNames.add(m.group(1));
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

    // download image from image url
    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap myBitmap;
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                // create instance with url and connect to browser
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                // download input stream at once and convert to Bitmap
                InputStream inputStream = urlConnection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    // game play/state class
    private class CelebGuess {

        boolean isActive = false;

        int numberOfRounds = 0;
        int chosenCeleb = 0;
        int locationOfCorrectAnswer;

        String[] answers = new String[4];

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

        private void createQuestion() {
            if (isActive == true) {
                Random random = new Random();
                chosenCeleb = random.nextInt(imageUrls.size());

                DownloadImage downloadImage = new DownloadImage();
                Bitmap celebImage;

                try {
                    // set the ImageView to the randomly downloaded image
                    celebImage = downloadImage.execute(imageUrls.get(chosenCeleb)).get();
                    celebImageView.setImageBitmap(celebImage);

                    locationOfCorrectAnswer = random.nextInt(4);

                    int incorrectAnswerLocation;

                    // add (in)correct answers to list
                    for (int i = 0; i < 4; i++) {
                        if (i == locationOfCorrectAnswer) {
                            answers[i] = celebNames.get(chosenCeleb);
                        } else {
                            incorrectAnswerLocation = random.nextInt(imageUrls.size());

                            // prevent multiple correct answers from occurring
                            while (incorrectAnswerLocation == chosenCeleb) {
                                incorrectAnswerLocation = random.nextInt(imageUrls.size());
                            }

                            answers[i] = celebNames.get(incorrectAnswerLocation);
                        }
                    }

                    // set answers to TextViews
                    answerOne.setText(answers[0]);
                    answerTwo.setText(answers[1]);
                    answerThree.setText(answers[2]);
                    answerFour.setText(answers[3]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                celebImageView.setImageResource(R.drawable.celebivbackground);
                answerOne.setText("Answer 1");
                answerTwo.setText("Answer 2");
                answerThree.setText("Answer 3");
                answerFour.setText("Answer 4");
            }
        }

        private void answerSelection(View view) {
            if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
                Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Wrong! It was " + celebNames.get(chosenCeleb), Toast.LENGTH_SHORT).show();
            }

            numberOfRounds += 1;

            if (numberOfRounds < 10) {
                createQuestion();
            } else {
                startGame();
            }
        }

        // start game
        private void startGame() {
            if (isActive == false) {
                isActive = true;
                buttonEnabled(answerButtonLayout);
                gameStartLayout.setVisibility(View.INVISIBLE);
                playButton.setEnabled(false);
                createQuestion();
            } else {
                isActive = false;
                buttonEnabled(answerButtonLayout);
                gameStartLayout.setVisibility(View.VISIBLE);
                playButton.setEnabled(true);
                createQuestion();
                numberOfRounds = 0;
            }
        }

    }

    public void answerButton(View view) {
        celebGuess.answerSelection(view);
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