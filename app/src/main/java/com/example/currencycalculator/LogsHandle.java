package com.example.currencycalculator;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

// from stackoverflow
public class LogsHandle {

    private static void mCreateAndSaveFile(String mJsonResponse, Context context) {
        try {
            FileWriter file = new FileWriter("/data/data/" + context.getPackageName() + "/logs.json");
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String mReadJsonData(Context context) {
        try {
            File f = new File("/data/data/" + context.getPackageName() + "/logs.json");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
            return mResponse;
        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    public static void addNewLog(Log newLog, Context context) {
        Logs logs = getLogs(context);
        logs.addLog(newLog);
        saveLogsAsJSON(logs, context);
    }

    private static void saveLogsAsJSON(Logs logs, Context context) {
        mCreateAndSaveFile(JSON.toJSONString(logs), context);
    }

    public static Logs getLogs(Context context) {
        String jsonData = mReadJsonData(context);
        Logs logs = new Logs();
        if (jsonData != null) logs = JSON.parseObject(jsonData, Logs.class);
        return logs;
    }
}

