package com.example.santa.babycalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private static String res = "";
    private static int level = 1;
    private static int time = 10;
    private static int played = 0;
    private static int score = 0;
    private static CountDownTimer ct = null;
    private static TextView counter = null;
    private static TextView resBox = null;
    private static TextView textCalc = null;
    private static TextView textLog = null;
    private static TextView textScore = null;
    private static int result = -1;
    private static Random rand = null;
    private static SoundPoolHelper sph = null;
    private static int soundClick = 0;
    private static int soundAlert = 0;
    private static int soundOK = 0;
    private static int soundFail = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        level = intent.getIntExtra(LevelActivity.EXTRA_LEVEL, 1);
        time = intent.getIntExtra(LevelActivity.EXTRA_TIME, 10);
        played = 0;
        score = 0;
        counter = ((TextView) findViewById(R.id.textCounter));
        counter.setText(Integer.toString(time));
        resBox  = ((TextView) findViewById(R.id.textRes));
        resBox.setText("");
        textCalc  = ((TextView) findViewById(R.id.textCalc));
        textLog  = ((TextView) findViewById(R.id.textLog));
        textLog.setText("C'est parti !!");
        textScore  = ((TextView) findViewById(R.id.textScore));
        textScore.setText("Score: 0");
        rand = new Random();

        sph = new SoundPoolHelper(1, this);
        soundClick = sph.load(this, R.raw.tick, 1);
        soundAlert = sph.load(this, R.raw.alert, 1);
        soundOK = sph.load(this, R.raw.click2, 1);
        soundFail = sph.load(this, R.raw.fail, 1);
        time += 1;
        sph.play(soundAlert);

        initCT();
    }

    @Override
    protected void onDestroy() {
        if(ct != null)
            ct.cancel();
        if(sph != null)
            sph.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    public void clickNum(View view) {
        sph.play(soundClick);
        String num = (String) ((Button)view).getText();
        if (res.length() < 2 && (res.length() == 1 || num != "0")) {
            res += num;
            resBox.setText(res);
            resBox.setText(res);
        }
    }

    public void clickDel(View view) {
        if(res.length() > 0) {
            sph.play(soundClick);
            res = res.substring(0, res.length() - 1);
            resBox.setText(res);
        }
    }

    public void clickOK(View view) {
        if (res.length()>0) {
            ct.cancel();
            if (Integer.parseInt(res) == result) {
                sph.play(soundOK);
                score += 11 - time + level;
                textLog.setText("Bravo !! ");
                textScore.setText("Score: " + score);
            } else {
                sph.play(soundFail);
                textLog.setText("Faux !! C'était " + result);
            }
            res = "";
            resBox.setText(res);
            initCT();
        } else {
            //TODO: play sound fail
        }
    }

    private void initCT() {
        int a,b;
        played += 1;

        if(ct!=null)
            ct.cancel();
        if(played<=10) {
            if (level == 1) {
                a = rand.nextInt(4)+1;
                b = rand.nextInt(4)+1;
            } else if (level == 2) {
                a = rand.nextInt(9)+1;
                b = rand.nextInt(4)+1;
            } else {
                a = rand.nextInt(9)+1;
                b = rand.nextInt(9)+1;
            }
            result = a + b;
            textCalc.setText(a+" + "+b+" = ");
            ct = new CountDownTimer(time*1000, 1000) {
                public void onTick(long untilFinished) {
                    counter.setText(Long.toString(untilFinished / 1000));
                }

                public void onFinish() {
                    sph.play(soundFail);
                    counter.setText("0");
                    textLog.setText("Trop tard !");
                    res = "";
                    resBox.setText(res);
                    initCT();
                }
            };

            ct.start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Nouveau score: "+score).setTitle("Fin").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("score", score);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
