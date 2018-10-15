package com.example.administrator.rate;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ListActivity extends AppCompatActivity implements Runnable {
    String TAG = "List";
    int msgWhat = 3;
    private ArrayList<HashMap<String, String>> listItems;
    private SimpleAdapter listItemAdapter; // 适配器
    Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_list);


        handler = new Handler(){
            public void handleMessage(Message msg) {
                if(msg.what==msgWhat){
                    //initListView();
                    List<Item> itemlist = (List<Item>) msg.obj;
                    String[] itemtitle = new String[itemlist.size()];
                    String[] itemdetail = new String[itemlist.size()];
                    Log.i(TAG, "handleMessage: size"+itemlist.size());

                    for (int i = 0; i<itemlist.size(); i++){
                        Item item = itemlist.get(i);
                        String detail = "Code: "+item.getCode()+"\tRate: "+item.getRate();
                                        //+"\nTime: "+item.getDate();
                        Log.i(TAG, "handleMessage: "+detail);
                        String title = item.getName();
                        itemdetail[i] = detail;
                        itemtitle[i] = title;
                    }
                    //Log.i(TAG, "handleMessage: "+itemdetail);
                    initListView(itemtitle,itemdetail);
                }
                super.handleMessage(msg);
            }
        };
        //开启⼦线程
        Thread t = new Thread(this);
        t.start();
    }


    @Override//解析网页数据并返回list(自定义item对象列表)
    public void run() {
        List<Item> list = new ArrayList<>();
        boolean marker = false;
        try {
            String url="http://www.zou114.com/agiotage/huilv.asp?bi=CNY";
            Document document = Jsoup.connect(url).get();
            Elements trs= document.getElementsByTag("table").get(0).getElementsByTag("tr");
            trs.remove(0);
            for(Element tr : trs) {
                Elements tds=tr.getElementsByTag("td");
                String name = String.format(tds.get(0).text());
                String code = String.format(tds.get(1).text());
                String rate = String.format(tds.get(2).text());
                Date date = StrToDate(tds.get(3).text());

                Item item = new Item(name,code,rate,date);
                list.add(item);
            }
            Log.i(TAG, "run: 结束！！！");
            Log.i(TAG, "run: "+list.size());
            marker = true;
        } catch (IOException e) {
            Log.e("www", e.toString());
            e.printStackTrace();
        }

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage();
        msg.what = msgWhat;
        msg.obj = list;
        handler.sendMessage(msg);

        if(marker){
            msg.arg1 = 1;
        }else{
            msg.arg1 = 0;
        }
    }
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    private  void initListView(String[] itemtitle, String[] itemdetail) {
        Log.i(TAG, "initListView: 1次");
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i<itemdetail.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle",itemtitle[i]); // 标题文字
            map.put("ItemDetail", itemdetail[i]); // 详情描述
            listItems.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.list_columns, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
        ListView listview=(ListView)findViewById(R.id.listview);
        //实例化ArrayAdapter
        listview.setAdapter(listItemAdapter);//设置适配器
    }
}



