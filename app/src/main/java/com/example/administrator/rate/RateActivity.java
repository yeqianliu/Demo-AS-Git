package com.example.administrator.rate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateActivity extends AppCompatActivity implements View.OnClickListener,Runnable {
    private final String TAG = "Rate";
    private float dollarRate;
    private float euroRate;
    private float wonRate;
    EditText rmb;
    TextView show;
    Handler handler;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.show);
        Button dollar = (Button)findViewById(R.id.dollar);
        dollar.setOnClickListener(this);
        Button euro = (Button)findViewById(R.id.euro);
        euro.setOnClickListener(this);
        Button won = (Button)findViewById(R.id.won);
        won.setOnClickListener(this);
        Button opencfg = (Button)findViewById(R.id.btn_opencfg);
        //opencfg.setOnClickListener(this);
        //onClick属性：openOne

        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Context.MODE_PRIVATE);

        handler = new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    List<Float> list= (List<Float>) msg.obj;
                    dollarRate=list.get(0);
                    euroRate=list.get(1);
                    wonRate=list.get(2);
                }
                super.handleMessage(msg);
            }
        };
        //开启⼦线程
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        String str = rmb.getText().toString();
        Log.i(TAG, "onClick: get str=" + str);
        float r = 0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            //用户没有输入内容
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "onClick: r=" + r);
        //计算
        if(v.getId()==R.id.dollar){
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(v.getId()==R.id.euro){
            show.setText(String.format("%.2f",r*euroRate));
        }else{
            show.setText(String.format("%.2f",r*wonRate));
        }
    }

    public void openOne(View btn){
        Intent config = new Intent(this,ConfigActivity.class);
        config.putExtra("dollar_rate_key",dollarRate);
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);
        Log.i(TAG, "openOne: dollarRate=" + dollarRate);
        Log.i(TAG, "openOne: euroRate=" + euroRate);
        Log.i(TAG, "openOne: wonRate=" + wonRate);
        //startActivity(config);
        //调用页面，接收数据 期望在活动销毁的时候能够返回一个结果给上一个活动
        startActivityForResult(config,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG, "onActivityResult: dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult: euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult: wonRate=" + wonRate);
        }
        super.onActivityResult(requestCode, resultCode, data);

        //将新设置的汇率写到SP⾥
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("dollar_rate",dollarRate);
        editor.putFloat("euro_rate",euroRate);
        editor.putFloat("won_rate",wonRate);
        editor.commit();
        Log.i(TAG, "onActivityResult: 数据已保存到sharedPreferences");
    }

    @Override
    public void run() {
        Log.i(TAG, "run: run()......");
        List<Float> list=new ArrayList<>();
        try {
            //Log.i("AAA", String.valueOf(getRateFromWeb("CNY/USD")));
            list.add(getRateFromWeb("CNY/USD"));
            list.add(getRateFromWeb("CNY/EUR"));
            list.add(getRateFromWeb("CNY/KRW"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = list;
        handler.sendMessage(msg);
    }

    private float getRateFromWeb(String name)throws IOException {
        String url="http://www.zou114.com/agiotage/huilv.asp?bi=CNY";
        Document document = Jsoup.connect(url).get();
        Elements trs= document.getElementsByTag("table").get(0).getElementsByTag("tr");
        for(Element tr : trs) {
            Elements tds=tr.getElementsByTag("td");
            if(name.equals(tds.get(1).text())){
                return  Float.parseFloat(tds.get(2).text());
            }
        }
        return 0.0f;
    }
}
