package com.example.currencycalculator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static Currencies store = null;
    public static String courseDate = null;
    private double toSaveIntoClipboard = 0.0;

    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] helpdata = null;
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        final EditText et = findViewById(R.id.valuteInput);
        et.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        final TextView out = findViewById(R.id.valuteOutput);
        final TextView to = findViewById(R.id.to);
        CalendarView calendar = findViewById(R.id.calendarView);

        Spinner inputValute = findViewById(R.id.spinner);
        Spinner outputValute = findViewById(R.id.spinner2);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);

        final int[] positions = {0, 0}; // выбранные валюты
        final String[] date = {format.format(new Date())};

        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        if (!hasConnection(getApplicationContext()))
        {
            out.setText("Нет интернета");
            out.setTextColor(Color.RED);
            return;
        } else
            try {
                courseDate = format.format(new Date());
                store = new Request().execute(courseDate).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

        helpdata = store.getCurrenciesShortnames();
        final String[] data = helpdata;

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                hideKeyboard();
                String day, month;
                day = Integer.toString(i2);
                month = Integer.toString(i1 + 1);
                if (i2 < 10) day = "0" + i2;
                if (i1 < 10) month = "0" + (i1 + 1);
                date[0] = day + "/" + month + "/" + i;
                courseDate = date[0];
            }
        });

        // первый спиннер
        // адаптер
        ArrayAdapter<String> inputAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputValute.setAdapter(inputAdapter);
        // заголовок
        inputValute.setPrompt("From");
        // выделяем элемент
        inputValute.setSelection(0);
        // устанавливаем обработчик нажатия
        inputValute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // первая валюта
                positions[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // второй спиннер
        // адаптер
        ArrayAdapter<String> outputAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        outputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outputValute.setAdapter(outputAdapter);
        // заголовок
        outputValute.setPrompt("To");
        // выделяем элемент
        outputValute.setSelection(0);
        // устанавливаем обработчик нажатия
        outputValute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // вторая валюта
                positions[1] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // сохранение в буфер обмена
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = Double.toString(toSaveIntoClipboard);
                clipData = ClipData.newPlainText("text", text);
                clipboardManager.setPrimaryClip(clipData);
                showToast("Результат скопирован");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Currencies currencies = null;
                if (!hasConnection(getApplicationContext()))
                {
                    out.setText("0.0");
                    showToast("Проверьте подключение");
                    return;
                } else {
                    try {
                        currencies = new Request().execute(date[0]).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (currencies.isEmpty()) {
                        out.setText("0.0");
                        showToast("Для этих валют нет курса");
                        return;
                    }
                    if (currencies.getByShortname(data[positions[0]]) == null || (currencies.getByShortname(data[positions[1]]) == null)) {
                        out.setText("0.0");
                        showToast("ЦБ не вернул курс");
                        return;
                    }
                    double from = currencies.getByShortname(data[positions[0]]).getExchangeRate();
                    double to = currencies.getByShortname(data[positions[1]]).getExchangeRate();
                    String valuteToConvert = et.getText().toString();
                    if (valuteToConvert.equals("")) valuteToConvert = "0";
                    double count = 0.0;
                    try {
                        count = Double.parseDouble(valuteToConvert);
                    } catch (Exception e) {
                        out.setText("0.0");
                        return;
                    }

                    // вычисление
                    String result = Currency.Calculate(from, to, count);
                    toSaveIntoClipboard = Currency.calculateClear(from, to, count);
                    out.setText(result);
                    Log log = new Log(count, data[positions[0]], data[positions[1]], result, date[0]);
                    LogsHandle.addNewLog(log, getApplicationContext());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.currencies:
                Intent intent = new Intent(getApplicationContext(), CurrenciesActivity.class);
                try {
                    store = new Request().execute(courseDate).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("data", store.infoToString());
                LogsHandle.addNewLog(new Log(0.0, null, null, null, store.getDate()), getApplicationContext());
                startActivity(intent);
                return true;

            case R.id.history:
                Intent intent2 = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
    }

    public void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
