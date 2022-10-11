package com.example.aplicacion_full_stack;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText edCedula;
    private EditText edContrasena;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = getString(R.string.ip);
    }

    public void iniciarSesion(View v) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String charset = "UTF-8";

                    edCedula = (EditText) findViewById(R.id.edCedula);
                    edContrasena = (EditText) findViewById(R.id.edContrasena);

                    String cedula = edCedula.getText().toString();
                    String contrasena = edContrasena.getText().toString();

                    String query = String.format("?cedula=%s&contrasena=%s",
                            URLEncoder.encode(cedula, charset),
                            URLEncoder.encode(contrasena, charset));

                    Context context = getApplicationContext();

                    URL url = new URL(String.format("http://%s:3001/%s", ip, query));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Accept-Charset", charset);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

                    String jsonResponse = rd.readLine();

                    JSONObject jsonValue = new JSONObject(jsonResponse);
                    int code = jsonValue.getInt("code");

                    if (code == 200) {
                        JSONObject data = jsonValue.getJSONObject("data");
                        String nombre = data.getString("nombre");
                        String apellido = data.getString("apellido");
                        String telefono = data.getString("telefono");

                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        // Passing data to another Activity
                        Intent i = new Intent(MainActivity.this, UserActivity.class);
                        i.putExtra("cedula", cedula);
                        i.putExtra("contrasena", contrasena);
                        i.putExtra("nombre", nombre);
                        i.putExtra("apellido", apellido);
                        i.putExtra("telefono", telefono);
                        startActivity(i);
                    } else {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(context, "Wrong credentials!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }

                    Log.i("data", jsonResponse);
                } catch (Exception e) {
                    Log.d("Error on sign up", "Ocurrió un error al intentar iniciar sesión.");
                    Log.d("Error on sign up", e.toString());
                }
            }
        });

        if (thread.isAlive()) {
            // Ending thread after there was a successful login
            thread.interrupt();
        }

        thread.start();
    }

    public void irRegistrarse(View v) {
        // We travel the user to the next activity
        startActivity(new Intent(MainActivity.this, RegistrarseActivity.class));
    }
}