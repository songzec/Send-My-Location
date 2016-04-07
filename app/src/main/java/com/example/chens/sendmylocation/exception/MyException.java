package com.example.chens.sendmylocation.exception;


import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chens on 2016/4/6.
 */
public class MyException {
    public MyException (String s, File dir) {
        Log.e(null, s);
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            long time = System.currentTimeMillis();
            String logName = "log-" + time + ".txt";
            File log = new File(dir, logName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(log));
            writer.write("Error time: " + sdf.format(cal.getTime()));
            writer.write(s);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
