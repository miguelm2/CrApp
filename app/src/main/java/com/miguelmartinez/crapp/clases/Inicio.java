package com.miguelmartinez.crapp.clases;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.miguelmartinez.crapp.R;

import java.io.File;

public class Inicio extends AppCompatActivity {
    Button ingresar;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        )  {

            //Si no tenemos permisos, creare una funcion para pedirlos
            PedirPermisos();

        }


        ingresar = (Button) findViewById(R.id.ingresar);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File carpeta = new File(Environment.getExternalStorageDirectory() + "/CrApp");
                if (!carpeta.exists()) {
                    //carpeta.mkdir() creará la carpeta en la ruta indicada al inicializar el objeto File
                    if (carpeta.mkdir())
                        System.out.println("Carpeta creada");
                } else {
                    //la carpeta ya existe
                    System.out.println("Carpeta existe");
                }

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void PedirPermisos() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Se necesitan permisos", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                MY_PERMISSIONS_REQUEST);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "SIN PERMISOS NO SE PUEDE EJECUTAR LA APP", Toast.LENGTH_SHORT).show();
                PedirPermisos();

            }

        }

    }

}
