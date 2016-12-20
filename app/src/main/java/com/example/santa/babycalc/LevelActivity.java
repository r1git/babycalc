package com.example.santa.babycalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LevelActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL = "level";
    public static final String EXTRA_TIME = "time";
    private static final int time = 10;
    private static int player = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        Intent intent = getIntent();
        String playName = intent.getStringExtra(NameActivity.EXTRA_PLAYER);
        player = intent.getIntExtra(NameActivity.EXTRA_PLAYER_ID, 0);
        TextView textView = (TextView) findViewById(R.id.textPlayer);
        textView.setText(playName);
    }

    public void clickLevel(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        Button butt = (Button) view;
        intent.putExtra(EXTRA_LEVEL, Integer.parseInt(view.getTag().toString()));
        intent.putExtra(EXTRA_TIME, time);
        startActivityForResult(intent, player);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            int score = data.getIntExtra("score", 0);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("score", score);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}
