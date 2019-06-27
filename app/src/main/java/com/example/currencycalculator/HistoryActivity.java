package com.example.currencycalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    int maxCountLogs = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        List<TextView> logViews = new ArrayList<>();

        TextView log0 = findViewById(R.id.textView);
        TextView log1 = findViewById(R.id.textView2);
        TextView log2 = findViewById(R.id.textView3);
        TextView log3 = findViewById(R.id.textView4);
        TextView log4 = findViewById(R.id.textView5);
        TextView log5 = findViewById(R.id.textView6);
        TextView log6 = findViewById(R.id.textView7);
        TextView log7 = findViewById(R.id.textView8);
        TextView log8 = findViewById(R.id.textView9);
        TextView log9 = findViewById(R.id.textView10);

        logViews.add(log0);
        logViews.add(log1);
        logViews.add(log2);
        logViews.add(log3);
        logViews.add(log4);
        logViews.add(log5);
        logViews.add(log6);
        logViews.add(log7);
        logViews.add(log8);
        logViews.add(log9);

        List<Log> logs = LogsHandle.getLogs(getApplicationContext()).getLogs();
        if (logs.size() != 0) {
            int count = logs.size();
            if (logs.size() > maxCountLogs) count = maxCountLogs;
            for (int i = 0; i < count; i++) {
                logViews.get(i).setText(logs.get(i).toString());
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.converter:
                finish();
                return true;

            case R.id.currencies:
                Intent intent = new Intent(getApplicationContext(), CurrenciesActivity.class);
                try {
                    MainActivity.store = new Request().execute(MainActivity.courseDate).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("data", MainActivity.store.infoToString());
                LogsHandle.addNewLog(new Log(0.0, null, null, null, MainActivity.store.getDate()), getApplicationContext());
                finish();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}