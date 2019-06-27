package com.example.currencycalculator;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Request extends AsyncTask<String, Integer, Currencies> {

    @Override
    protected Currencies doInBackground(String... arg) {
        try {
            URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + arg[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            int a = connection.getResponseCode();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "windows-1251"));
                String answer = "";
                String line = null;

                while ((line = reader.readLine()) != null) {
                    if (line != null) answer += line;
                }

                reader.close();
                answer = answer.replace(" encoding=\"windows-1251\"", " encoding=\"utf-8\"");
                Charset cset = Charset.forName("UTF-8");
                ByteBuffer buf = cset.encode(answer);
                byte[] b = buf.array();
                String str = new String(b);

                return parse(str, arg[0]);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Currencies s) {
        super.onPostExecute(s);

    }

    public static Currencies parse(String a, String date) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<Currency> currencyList = new ArrayList<Currency>();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(a)));
            NodeList valutes = doc.getFirstChild().getChildNodes();
            currencyList.add(new Currency("000", "RUB", 1, "Рубли", 1.0));
            for (int i = 0; i < valutes.getLength(); i++) {

                NodeList children = valutes.item(i).getChildNodes();
                Currency current = new Currency(children.item(0).getFirstChild().getNodeValue()
                        , children.item(1).getFirstChild().getNodeValue()
                        , Integer.parseInt(children.item(2).getFirstChild().getNodeValue())
                        , children.item(3).getFirstChild().getNodeValue()
                        , Double.parseDouble(children.item(4).getFirstChild().getNodeValue().replace(",", ".")));

                currencyList.add(current);
            }

            return new Currencies(currencyList, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}