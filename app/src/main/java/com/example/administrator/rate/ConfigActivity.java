package com.example.administrator.rate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ConfigActivity";
    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);
        Log.i(TAG, "onCreate: dollar2=" + dollar2);
        Log.i(TAG, "onCreate: euro2=" + euro2);
        Log.i(TAG, "onCreate: won2=" + won2);

        dollarText = (EditText)findViewById(R.id.dollar_rate);
        euroText = (EditText)findViewById(R.id.euro_rate);
        wonText = (EditText)findViewById(R.id.won_rate);
        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

        //保存数据
        Button save = (Button)findViewById(R.id.btn_save);
        //save.setOnClickListener(this);
        //onClick属性：save

    }


    @Override
    public void onClick(View v) {

    }

    public void save(View v) {
        Log.i(TAG, "save: ");
//获取新的值
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuroi = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());
        Log.i(TAG, "save: 获取到新的值");
        Log.i(TAG, "save: newDollar=" + newDollar);
        Log.i(TAG, "save: newEuroi=" + newEuroi);
        Log.i(TAG, "save: newWon=" + newWon);

        //保存到Bundle或放入到Extra
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuroi);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);
        //返回到调用页⾯
        finish();
    }
}
