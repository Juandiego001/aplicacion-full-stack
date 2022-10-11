package com.example.aplicacion_full_stack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UserActivity extends AppCompatActivity {

    EditText edCedulaUser;
    EditText edContrasenaUser;
    EditText edNombreUser;
    EditText edApellidoUser;
    EditText edTelefonoUser;
    String cedulaOriginal;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ip = getString(R.string.ip);
        setDatosUsuario();
    }

    public void setDatosUsuario() {
        edCedulaUser = (EditText) findViewById(R.id.edCedulaUser);
        edContrasenaUser = (EditText) findViewById(R.id.edContrasenaUser);
        edNombreUser = (EditText) findViewById(R.id.edNombreUser);
        edApellidoUser = (EditText) findViewById(R.id.edApellidoUser);
        edTelefonoUser = (EditText) findViewById(R.id.edTelefonoUser);

        Bundle extras = getIntent().getExtras();

        String cedula = extras.getString("cedula");
        cedulaOriginal = cedula;
        String contrasena = extras.getString("contrasena");
        String nombre = extras.getString("nombre");
        String apellido = extras.getString("apellido");
        String telefono = extras.getString("telefono");

        edCedulaUser.setText(cedula);
        edContrasenaUser.setText(contrasena);
        edNombreUser.setText(nombre);
        edApellidoUser.setText(apellido);
        edTelefonoUser.setText(telefono);
    }

    public void regresar(View v) {
        startActivity(new Intent(UserActivity.this, MainActivity.class));
    }

    public void actualizar(View v) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    edCedulaUser = (EditText) findViewById(R.id.edCedulaUser);
                    edContrasenaUser = (EditText) findViewById(R.id.edContrasenaUser);
                    edNombreUser = (EditText) findViewById(R.id.edNombreUser);
                    edApellidoUser = (EditText) findViewById(R.id.edApellidoUser);
                    edTelefonoUser = (EditText) findViewById(R.id.edTelefonoUser);

                    String nuevaCedula = edCedulaUser.getText().toString();
                    String nuevaContrasena = edContrasenaUser.getText().toString();
                    String nuevoNombre = edNombreUser.getText().toString();
                    String nuevoApellido = edApellidoUser.getText().toString();
                    String nuevoTelefono = edTelefonoUser.getText().toString();

                    URL url = new URL(String.format("http://%s:3001", ip));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String charset = "UTF-8";
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("PUT");
                    urlConnection.setRequestProperty("Accept-Charset", charset);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

                    Context context = getApplicationContext();

                    String query = String.format("cedulaOriginal=%s&" +
                            "nuevaCedula=%s&" +
                            "nuevaContrasena=%s&" +
                            "nuevoNombre=%s&" +
                            "nuevoApellido=%s&" +
                            "nuevoTelefono=%s&",
                            URLEncoder.encode(cedulaOriginal, charset),
                            URLEncoder.encode(nuevaCedula, charset),
                            URLEncoder.encode(nuevaContrasena, charset),
                            URLEncoder.encode(nuevoNombre, charset),
                            URLEncoder.encode(nuevoApellido, charset),
                            URLEncoder.encode(nuevoTelefono, charset));

                    OutputStream output = urlConnection.getOutputStream();
                    output.write(query.getBytes(charset));

                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

                    String jsonResponse = rd.readLine();

                    JSONObject jsonValue = new JSONObject(jsonResponse);
                    int code = jsonValue.getInt("code");

                    if (code == 200) {
                        // We have to change the values of the editText
                        edCedulaUser.setText(nuevaCedula);
                        cedulaOriginal = nuevaCedula;
                        edContrasenaUser.setText(nuevaContrasena);
                        edNombreUser.setText(nuevoNombre);
                        edApellidoUser.setText(nuevoApellido);
                        edTelefonoUser.setText(nuevoTelefono);

                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(context, "Se han efectuado los cambios con éxito!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    } else {
                        // Rollback the data to the original
                        edCedulaUser.setText(cedulaOriginal);
                        edContrasenaUser.setText(edContrasenaUser.getText().toString());
                        edNombreUser.setText(edNombreUser.getText().toString());
                        edApellidoUser.setText(edApellidoUser.getText().toString());
                        edTelefonoUser.setText(edTelefonoUser.getText().toString());

                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(context, "No se lograron efectuar los cambios.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("Error", "Ocurrió un error al intentar actualizar los datos del usuario");
                    Log.d("Error", e.toString());
                }
            }
        });

        if (thread.isAlive()) {
            // Ending thread after there was a successful login
            thread.interrupt();
        }

        thread.start();
    }

    public void eliminar(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(String.format("http://%s:3001", ip));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    String charset = "UTF-8";
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("DELETE");
                    urlConnection.setRequestProperty("Accept-Charset", charset);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

                    Context context = getApplicationContext();

                    String query = String.format("cedula=%s", cedulaOriginal);

                    OutputStream out = urlConnection.getOutputStream();
                    out.write(query.getBytes());

                    BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String jsonString = rd.readLine();

                    JSONObject jsonValue = new JSONObject(jsonString);
                    int code = jsonValue.getInt("code");

                    if (code == 200) {
                        UserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context, "La cuenta fue eliminada con éxito!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        // We have to go back to the login screen
                        startActivity(new Intent(UserActivity.this, MainActivity.class));
                    } else {
                        UserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context, "No se puedo eliminar la cuenta!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }

                } catch (Exception e) {
                    Log.d("Error", "Ocurrió un error al intentar eliminar la cuenta.");
                    Log.d("Error", e.toString());
                }
            }
        });

        if (thread.isAlive()) {
            thread.interrupt();
        }

        thread.start();
    }
}


