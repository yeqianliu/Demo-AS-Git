package com.example.administrator.rate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private String name;
    private String rate;
    private Date date;
    private String code;

    public Item(String name, String code,String rate,Date date) {
        this.name = name;
        this.code = code;
        this.rate = rate;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public Date getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }


}
