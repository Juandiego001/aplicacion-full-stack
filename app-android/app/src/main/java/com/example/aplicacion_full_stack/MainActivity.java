package com.example.aplicacion_full_stack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText edCedula;
    private EditText edContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciarSesion(View v) {
        edCedula = (EditText) findViewById(R.id.edCedula);
        edContrasena = (EditText) findViewById(R.id.edContrasena);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    String charset = "UTF-8";

                    String cedula = edCedula.getText().toString();
                    String contrasena = edContrasena.getText().toString();

                    String query = String.format("cedula=%s&contrasena=%s",
                            URLEncoder.encode(cedula, charset),
                            URLEncoder.encode(contrasena, charset));

                    URL url = new URL("http://172.16.52.18:3001");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Accept-Charset", charset);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

                    try (OutputStream output = urlConnection.getOutputStream()) {
                        output.write(query.getBytes(charset));
                    }

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