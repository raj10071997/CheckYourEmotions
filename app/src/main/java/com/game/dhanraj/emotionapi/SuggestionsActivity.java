package com.game.dhanraj.emotionapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SuggestionsActivity extends AppCompatActivity {

    private Button btn;
    private TextView first,second,third,fourth;
    private String emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        first = (TextView) findViewById(R.id.firstsug);
        second = (TextView) findViewById(R.id.secondsug);
        third = (TextView) findViewById(R.id.thirdsug);
        fourth = (TextView) findViewById(R.id.fourrthsug);
        btn = (Button) findViewById(R.id.tryagain);

        Bundle i = getIntent().getExtras();


        if(i!=null)
        {
            emotion = i.getString("result1");

        }


        Toast.makeText(this, emotion, Toast.LENGTH_SHORT).show();

        if(emotion.equals("Anger"))
        {
            first.setText("1. take deep breath");
            second.setText("2. be cool and calm");
            third.setText("3. go outside in natural environment");
            fourth.setText("4. meditate");
        }
        else if(emotion.equals("Fear"))
        {
            first.setText("1. take deep breath");
            second.setText("2. have confidence");
            third.setText("3.be courageous");
            fourth.setText("4. take risks");
        }
        else if(emotion.equals("Disgust"))
        {
            first.setText("1.be stable");
            second.setText("2.listen to music");
            third.setText("3.try to forgive");
            fourth.setText("4.enjoy life");
        }
        else if(emotion.equals("Happiness"))
        {
            first.setText("1.share it with others");
            second.setText("2.keep it up");
            third.setText("3.enjoy life");
            fourth.setText("4.think good");
        }
        else if(emotion.equals("Sadness"))
        {
            first.setText("1. listen to music");
            second.setText("2. meet some friend");
            third.setText("3. go outside in natural environment");
            fourth.setText("4. watch some funny videos");
        }
        else if(emotion.equals("Contempt"))
        {
            first.setText("1. do some useful work");
            second.setText("2. meet some friend");
            third.setText("3. go outside in natural environment");
            fourth.setText("4. watch some funny videos");
        }
        else if(emotion.equals("Surprise"))
        {
            first.setText("1. be stable");
            second.setText("2. believe in reality");
            third.setText("3.be cool and calm");
            fourth.setText("4. take deep breath");
        }
        else if(emotion.equals("Neutral"))
        {
            first.setText("1. try to be happy");
            second.setText("2. meet some friend");
            third.setText("3. go outside in natural environment");
            fourth.setText("4. enjoy life");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
