package com.example.sofimoore.password;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.CRC32;

public class MainActivity extends AppCompatActivity {
    EditText str1, str2;
    Button but1, but2;
    TextView text;
    MyTask mt;


    public MainActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        str1 = (EditText) findViewById(R.id.editText);
        str2 = (EditText) findViewById(R.id.editText2);
        text = (TextView) findViewById(R.id.textView);
        but1 = (Button) findViewById(R.id.button);
        but2 = (Button) findViewById(R.id.button2);


    }

    ArrayList<String> words;

    public void onButtonClick(View v) throws FileNotFoundException {
        File file = new File(str1.getText().toString());
        words = new ArrayList<>();
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) words.add(sc.nextLine());
        Toast.makeText(this, "Список слов не загружен! Общее количество: " + words.size() + " слов.", Toast.LENGTH_LONG).show();
    }

    class MyTask extends AsyncTask<Long, String, String> {

        @Override
        protected String doInBackground(Long... params) {

            CRC32 crc32 = new CRC32();

            for (String word : words) {
                publishProgress(word);
                for (int i = 1; i < 10000; i++) {
                    String pass = word + Integer.toString(i);
                    crc32.update(pass.getBytes());
                    long a = crc32.getValue();
                    if (a == params[0]) {
                        return pass;
                    }
                    crc32.reset();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            text.setText("Teкущее слово: " + values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null)
                text.setText("Найден " + s);
            else
                text.setText("Пароль не найден!");
        }
    public void onButton2Click(View v) {
        mt = new MyTask();
        mt.execute(Long.decode(str2.getText().toString()));
    }

    }

}

