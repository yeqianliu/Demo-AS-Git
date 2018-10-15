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


public class ListActivity_copy extends AppCompatActivity implements Runnable {
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


        handler = new android.os.Handler(){
            public void handleMessage(Message msg) {
                if(msg.what==3){
                    List<Item> list = (List<Item>) msg.obj;
                    String[] itemtitle = new String[list.size()];
                    String[] itemdetail = new String[list.size()];
                    Log.i(TAG, "handleMessage: size"+list.size());

                    for (int i = 0; i<list.size(); i++){
                        Item item = list.get(i);
                        String detail = "Code: "+item.getCode()+"\tRate: "+item.getRate()+
                                "\nTime: "+item.getDate();
                        String title = item.getName();
                        itemdetail[i] = detail;
                        itemtitle[i] = title;
                    }
                    Log.i(TAG, "handleMessage: "+itemdetail);
                    initListView(itemtitle,itemdetail);
                }
                super.handleMessage(msg);
            }
        };
        //开启⼦线程
        Thread t = new Thread(this);
        t.start();




        ListAdapter adapter =  new SimpleAdapter(this, listItems, // listItems
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail });
        ListView listview=(ListView)findViewById(R.id.listview);
        //实例化ArrayAdapter
        listview.setAdapter(adapter);//设置适配器
    }


    @Override//解析网页数据并返回list(自定义item对象列表)
    public void run() {
        List<Item> list = new ArrayList<>();
        try {
            String url="http://www.zou114.com/agiotage/huilv.asp?bi=CNY";
            Document document = Jsoup.connect(url).get();
            Elements trs= document.getElementsByTag("table").get(0).getElementsByTag("tr");
            trs.remove(0);
            for(Element tr : trs) {
                Elements tds=tr.getElementsByTag("td");
                //Elements tar = tds.get(0).getElementsByTag("target");
                String name = String.format(tds.get(0).text());
                Log.i(TAG, "run: "+name);
                String code = String.format(tds.get(1).text());
                Log.i(TAG, "run: "+code);
                String rate = String.format(tds.get(2).text());
                Date date = StrToDate(tds.get(3).text());
                Log.i(TAG, "run: "+date);

                Item item = new Item(name,code,rate,date);
                list.add(item);
            }
            Log.i(TAG, "run: 结束！！！");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取Msg对象，用于返回主线程
        Message msg = handler.obtainMessage();
        msg.what = 3;
        msg.obj = list;
        handler.sendMessage(msg);

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
        for (int i = 0; i<10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle","AA"+i); // 标题文字
            map.put("ItemDetail", "detail" + i); // 详情描述
            listItems.add(map);
        }
// 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // listItems
                R.layout.list_item, // ListItem的XML布局实现
                new String[] { "ItemTitle", "ItemDetail" },
                new int[] { R.id.itemTitle, R.id.itemDetail }
        );
    }
}



