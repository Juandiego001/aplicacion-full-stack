package com.example.aplicacion_full_stack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciarSesion(View v) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url = new URL("http://172.16.52.18:3001");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Accept-Encoding", "identity");
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);

                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    String jsonInputString = "{'name': 'Upendra', 'job': 'Programmer'}";
                    byte[] input = jsonInputString.getBytes("utf-8");
                    out.write(input, 0, input.length);
                    out.flush();
                    out.close();

                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

                    String allData = "";
                    String line;
                    while ((line = rd.readLine()) != null) {
                        Log.i("data", line);
                    }


                } catch (Exception e) {
                    Log.d("Error on sign up", "Ocurrió un error al intentar iniciar sesión.");
                    Log.d("Error on sign up", e.toString());
                }
            }
        });

        thread.start();
    }
}