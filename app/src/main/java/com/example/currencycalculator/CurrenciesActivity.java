package com.example.currencycalculator;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CurrenciesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

        String[] data = getIntent().getStringArrayExtra("data");
        TextView view = findViewById(R.id.infoView);
        view.setMovementMethod(new ScrollingMovementMethod());
        String info = "";
        for (int i = 0; i < data.length; i++) {
            info += data[i] + "\n";
        }
        view.setText(info);
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

            case R.id.history:
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                finish();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
