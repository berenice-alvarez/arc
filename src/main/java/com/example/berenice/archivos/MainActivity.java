package com.example.berenice.archivos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button leermemint;
    Button escribirmemint;
    Button leerSD;
    Button escribirSD;
    EditText text;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;


    private void comprobarSD()
    {
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponible = true;
            sdAccesoEscritura = true;
            return;
        }

        if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sdDisponible = true;
            sdAccesoEscritura = false;
            return;
        }

        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
            return;
        }
    }
    public void checkPermissionExternalStorage ( AppCompatActivity thisActivity){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronous
                // ly* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(this,"No hay permiso", Toast.LENGTH_SHORT).show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1001);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            //doThings();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        leermemint = findViewById(R.id.leermemint);
        escribirmemint = findViewById(R.id.escribirmemint);
        leerSD = findViewById(R.id.leersd);
        escribirSD = findViewById(R.id.escribirsd);
        text = findViewById(R.id.texto);

        leermemint.setOnClickListener(this);
        escribirmemint.setOnClickListener(this);
        leerSD.setOnClickListener(this);
        escribirSD.setOnClickListener(this);


        //para comprobar que si exite ls SD y si puede escribir o no





    }

    @Override
    public void onClick(View v) {
        comprobarSD();
        this.checkPermissionExternalStorage(this);
        switch (v.getId()) {
            case (R.id.leermemint):
                try {
                    BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("memoriainterna.txt")));
                    String texto = fin.readLine();
                    text.setText(texto);
                    fin.close();
                } catch (Exception ex) {
                    Log.e("Fichero", "error al leer fichero desde la memoria interna");
                }
                break;

            case (R.id.escribirmemint):

                try {
                    OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("memoriainterna.txt", Context.MODE_PRIVATE));
                    fout.write(text.getText().toString());
                    fout.close();
                } catch (Exception ex) {
                    Log.e("ficheros", "Error al escribir fichero en la memoria interna");
                }
                break;


            case (R.id.leersd):


                    File rutaAppFile = getExternalFilesDir(null);

                    File miArchivo = new File(rutaAppFile, "notita.txt");

                    try {
                        FileInputStream fis = new FileInputStream(miArchivo);

                        InputStreamReader isr =
                                new InputStreamReader(fis);

                        char[] bloque = new char[50];
                        String texto = "";
                        while (isr.read(bloque) != -1) {

                            texto += String.valueOf(bloque);
                            bloque = new char[50];
                        }

                        text.setText(texto);

                        isr.close();
                        fis.close();


                        Toast.makeText(getBaseContext(),
                                "LEIDO EXTERNA",
                                Toast.LENGTH_LONG).show();


                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    break;


            case (R.id.escribirsd):

                File dirAppPath = getExternalFilesDir(null);

                Toast.makeText(getBaseContext(),
                        Environment.getExternalStorageState(),
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(),
                        dirAppPath.getAbsolutePath(),
                        Toast.LENGTH_LONG).show();

                miArchivo = new File(dirAppPath, "notita.txt");
                if (sdAccesoEscritura && sdDisponible) {
                    try {

                        FileOutputStream fos =
                                new FileOutputStream(miArchivo, true);

                        OutputStreamWriter osw = new
                                OutputStreamWriter(fos);

                        osw.write(text.getText().toString());

                        osw.flush();
                        osw.close();

                        Toast.makeText(getBaseContext(),
                                "GUARDADO EXTERNA",
                                Toast.LENGTH_LONG).show();

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;

                }

        }
    }
}
