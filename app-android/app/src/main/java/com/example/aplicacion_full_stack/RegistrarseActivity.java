package com.example.aplicacion_full_stack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText edCedulaReg;
    private EditText edContrasenaReg;
    private EditText edNombreReg;
    private EditText edApellidoReg;
    private EditText edTelefonoReg;
    String ip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edCedulaReg = (EditText) findViewById(R.id.edCedulaReg);
        edContrasenaReg = (EditText) findViewById(R.id.edContrasenaReg);
        edNombreReg = (EditText) findViewById(R.id.edNombreReg);
        edApellidoReg = (EditText) findViewById(R.id.edApellidoReg);
        edTelefonoReg = (EditText) findViewById(R.id.edTelefonoReg);
        ip = getString(R.string.ip);
    }

    public void registrarse(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String cedula = edCedulaReg.getText().toString();
                    String contrasena = edContrasenaReg.getText().toString();
                    String nombre = edNombreReg.getText().toString();
                    String apellido = edApellidoReg.getText().toString();
                    String telefono = edTelefonoReg.getText().toString();

                    URL url = new URL(String.format("http://%s:3001", ip));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String charset = "UTF-8";
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Accept-Charset", charset);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

                    Context context = getApplicationContext();

                    String query = String.format("cedula=%s&" +
                            "contrasena=%s&" +
                            "nombre=%s&" +
                            "apellido=%s&" +
                            "telefono=%s&",
                            URLEncoder.encode(cedula, charset),
                            URLEncoder.encode(contrasena, charset),
                            URLEncoder.encode(nombre, charset),
                            URLEncoder.encode(apellido, charset),
                            URLEncoder.encode(telefono, charset));

                    OutputStream out = urlConnection.getOutputStream();
                    out.write(query.getBytes());

                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String jsonString = rd.readLine();
                    JSONObject jsonValue = new JSONObject(jsonString);

                    int code = jsonValue.getInt("code");

                    if (code == 200) {
                        RegistrarseActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context, "Se registrado el usuario con éxito!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        // After sing up we move to the login screen
                        startActivity(new Intent(RegistrarseActivity.this, MainActivity.class));
                    } else {
                        RegistrarseActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context, "No se logró crear el usuario", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("Error", "Ocurrió un error mientras se intentaba registrar el usuario");
                }
            }
        });

        // Check if the thread have been already created
        if (thread.isAlive()) {
            thread.interrupt();
        }

        thread.start();
    }

    // To get back to login activity
    public void cancelar(View v) {
        startActivity(new Intent(RegistrarseActivity.this, MainActivity.class));
    }
}

