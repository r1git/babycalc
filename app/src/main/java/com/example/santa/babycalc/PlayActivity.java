package com.example.santa.babycalc;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private static String res = "";
    private static int level = 1;
    private static int time = 10;
    private static int played = 0;
    private static int timeGame = 10;
    private static int score = 0;
    private static CountDownTimer ct = null;
    private static TextView counter = null;
    private static TextView resBox = null;
    private static TextView textCalc = null;
    private static TextView textLog = null;
    private static TextView textScore = null;
    private static int result = -1;
    private static Random rand = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        level = intent.getIntExtra(LevelActivity.EXTRA_LEVEL, 1);
        time = intent.getIntExtra(LevelActivity.EXTRA_TIME, 10);
        played = 0;
        counter = ((TextView) findViewById(R.id.textCounter));
        resBox  = ((TextView) findViewById(R.id.textRes));
        textCalc  = ((TextView) findViewById(R.id.textCalc));
        textLog  = ((TextView) findViewById(R.id.textLog));
        textScore  = ((TextView) findViewById(R.id.textScore));
        rand = new Random();
        initCT();
    }

    public void clickNum(View view) {
        String num = (String) ((Button)view).getText();
        if (res.length() < 2 && (res.length() == 1 || num != "0")) {
            res += num;
            resBox.setText(res);
        }
    }

    public void clickDel(View view) {
        if(res.length() > 0)
            res = res.substring(0,res.length()-1);
        resBox.setText(res);
    }

    public void clickOK(View view) {
        ct.cancel();
        if(res.length()>0 && Integer.parseInt(res) == result)
        {
            score += 10 - time + level;
            textLog.setText("Good !!! ");
            textScore.setText(Integer.toString(score));
        } else {
            textLog.setText("Wrong !!! ");
        }
        res = "";
        resBox.setText(res);
        initCT();
    }

    private void initCT() {
        int a,b;
        played += 1;

        if(played<=10) {
            a = rand.nextInt(10);
            b = rand.nextInt(10);
            result = a + b;
            textCalc.setText(a+" + "+b+" = ?");
            ct = new CountDownTimer(timeGame*1000, 1000) {
                public void onTick(long untilFinished) {
                    counter.setText(Long.toString(untilFinished / 1000));
                }

                public void onFinish() {
                    textLog.setText("Too late");
                    initCT();
                }
            };

            ct.start();
        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("score", score);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}
