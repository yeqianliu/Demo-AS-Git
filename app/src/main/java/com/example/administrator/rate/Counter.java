package com.example.administrator.rate;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Counter extends AppCompatActivity implements View.OnClickListener{
    TextView scoreA;
    TextView scoreB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        scoreA = (TextView)findViewById(R.id.ScoreA);
        scoreB = (TextView)findViewById(R.id.ScoreB);

        Button btnA3 = (Button)findViewById(R.id.Aplus3);
        Button btnA2 = (Button)findViewById(R.id.Aplus2);
        Button btnA1 = (Button)findViewById(R.id.Afreethrow);
        Button btnB3 = (Button)findViewById(R.id.Bplus3);
        Button btnB2 = (Button)findViewById(R.id.Bplus2);
        Button btnB1 = (Button)findViewById(R.id.Bfreethrow);
        btnA1.setOnClickListener(this);
        btnA2.setOnClickListener(this);
        btnA3.setOnClickListener(this);
        btnB1.setOnClickListener(this);
        btnB2.setOnClickListener(this);
        btnB3.setOnClickListener(this);

        Button btnRESET = (Button)findViewById(R.id.RESET);
        btnRESET.setOnClickListener(this);
    }
    public void addpoints(View view,TextView score,int bonus){
        int num = Integer.parseInt(score.getText().toString());
        num += bonus;
        score.setText(Integer.toString(num));
    }

    public void reset(View view){
        scoreA.setText("0");
        scoreB.setText("0");
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.Aplus3:
                addpoints(view,scoreA,3);
                break;
            case R.id.Aplus2:
                addpoints(view,scoreA,2);
                break;
            case R.id.Afreethrow:
                addpoints(view,scoreA,1);
                break;
            case R.id.Bplus3:
                addpoints(view,scoreB,3);
                break;
            case R.id.Bplus2:
                addpoints(view,scoreB,2);
                break;
            case R.id.Bfreethrow:
                addpoints(view,scoreB,1);
                break;
            case R.id.RESET:
                reset(view);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.ScoreA)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.ScoreB)).getText().toString();
        Log.i("Counter", "onSaveInstanceState: ");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");
        Log.i("Counter", "onRestoreInstanceState: ");
        ((TextView)findViewById(R.id.ScoreA)).setText(scorea);
        ((TextView)findViewById(R.id.ScoreB)).setText(scoreb);
    }

}
