package com.example.santa.babycalc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NameActivity extends AppCompatActivity {

    public static final String EXTRA_PLAYER = "player";
    public static final String EXTRA_PLAYER_ID = "player_id";
    public int[] score;
    private static SoundPoolHelper sph = null;
    private int soundOva = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        sph = new SoundPoolHelper(1, this);
        soundOva = sph.load(this, R.raw.ovation, 1);
        score = new int[4];
        refreshScore();

    }

    @Override
    protected void onDestroy() {
        if(sph != null)
            sph.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        TextView record = ((TextView) findViewById(R.id.textRecord));
        record.setText("");
        super.onPause();
    }

    private void refreshScore() {
        SharedPreferences settings = getPreferences(0);
        for(int i=0; i<4;i++) {
            int resID = getResources().getIdentifier("textScore"+(i+1), "id", getPackageName());
            TextView textView = (TextView) findViewById(resID);
            score[i] = settings.getInt("score"+i, 0);
            textView.setText(Integer.toString(score[i]));
        }
    }

    public void launchPlayer(View view) {
        Intent intent = new Intent(this, LevelActivity.class);
        Button butt = (Button) view;
        String message = (String)((Button)view).getText();
        intent.putExtra(EXTRA_PLAYER, message);
        intent.putExtra(EXTRA_PLAYER_ID, Integer.parseInt(view.getTag().toString()));
        startActivityForResult(intent, Integer.parseInt(view.getTag().toString()));
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            int pscore = data.getIntExtra("score", 0);
            if (pscore > score[requestCode]) {
                TextView record = ((TextView) findViewById(R.id.textRecord));
                record.setText("Nouveau Record !!!");

                score[requestCode] = pscore;
                SharedPreferences settings = getPreferences(0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("score"+requestCode, pscore);
                editor.commit();
                refreshScore();
            }
            if (pscore >= score[requestCode]) {
                sph.play(soundOva);
            }
        }
    }
}
