package com.miguelmartinez.crapp.ui.cifrar;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.miguelmartinez.crapp.clases.Cifrados;
import com.miguelmartinez.crapp.clases.FilePath;
import com.miguelmartinez.crapp.R;
import com.miguelmartinez.crapp.clases.obtenerDirectorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CifrarFragment extends Fragment {

    private CifrarViewModel cifrarViewModel;
    Boolean cifro = false, corrompio = false, elimino = false;
    View view;
    String claves = "";
    Button encriptar,desencriptari;
    Button copiarClave,PegarClave;
    Button generarC;
    Button seleccionar;
    EditText clave;
    Button ver;
    Button nover;
    String id;
    File archivoSelecionado;
    Cifrados cifrador = new Cifrados();
    Spinner algoritmo;
    Boolean selecciono = false;
    String algoritmoSeleccionado;
    AlertDialog dialogoConfirmacion = null;
    AlertDialog.Builder contructorDialog = null;
    View dialog = null;
    private int VALOR_RETORNO = 1;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                            final ViewGroup container, Bundle savedInstanceState) {
        cifrarViewModel = ViewModelProviders.of(this).get(CifrarViewModel.class);
         view = inflater.inflate(R.layout.fragment_cifrar, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);





        clave = (EditText) view.findViewById(R.id.claves);
        encriptar = (Button) view.findViewById(R.id.enc);
        desencriptari = (Button) view.findViewById(R.id.descen);
        copiarClave = (Button) view.findViewById(R.id.Btn_CopiarClave);
        generarC = (Button) view.findViewById(R.id.bGenerar);
        ver = (Button) view.findViewById(R.id.ver);
        nover = (Button) view.findViewById(R.id.nover);
        seleccionar = (Button) view.findViewById(R.id.seleccionar);
        algoritmo = (Spinner) view.findViewById(R.id.spinner);
        PegarClave = (Button) view.findViewById(R.id.btn_PegarClave);


        String[] algoritmos = {"AES 256", "3DES"};

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, algoritmos);
        algoritmo.setAdapter(adaptador);


        contructorDialog = new AlertDialog.Builder(getContext());
        dialog = inflater.inflate(R.layout.dialog_confirmar, null);


        System.out.println("Se puede escribir en la sd: " + Environment.getExternalStorageState());
        System.out.println("Si puede escribir en la sd: " + Environment.MEDIA_MOUNTED);





        encriptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selecciono) {

                    claves = clave.getText().toString();

                    if(claves.isEmpty() ||  claves.length()!=16 ){
                        Snackbar.make(view, "La clave debe tener  16 o 24 caracteres!", Snackbar.LENGTH_LONG)
                                .setAction("Error", null).show();

                    }else {

                        dialog = inflater.inflate(R.layout.dialog_confirmar, container, false);
                        contructorDialog.setView(dialog);
                        dialogoConfirmacion = contructorDialog.create();
                        dialogoConfirmacion.setButton("Cifrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogoConfirmacion.dismiss();

                                algoritmoSeleccionado = algoritmo.getSelectedItem().toString();
                                new AsyncTask_Encriptar().execute();
                            }
                        });

                        dialogoConfirmacion.show();
                    }

                } else {
                    Snackbar.make(view, "Seleccione un archivo primero!", Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }

            }
        });


        //---------------------------------------



        desencriptari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selecciono) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Esta seguro que desea descifrar este archivo?");
                    alert.setMessage("Si esta seguro marque OK, de lo contrario marque cancelar.");
                    alert.setCancelable(true);


                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            algoritmoSeleccionado = algoritmo.getSelectedItem().toString();
                            new AsyncTask_Desencriptar().execute();

                        }
                    });

                    alert.setNegativeButton("Cancelar", null);
                    alert.show();


                } else {
                    Snackbar.make(view, "Seleccione un archivo primero!", Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }
            }
        });






        //---------------------------------------




        seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Escoger Archivo"), VALOR_RETORNO);
            }
        });

        //---------------------------------------


        generarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String generada = cifrador.GenerarClave();
                clave.setText(generada);
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Clave", generada);
                clipboard.setPrimaryClip(clip);


                Snackbar.make(view, "Clave generada copiada en el portapapeles!", Snackbar.LENGTH_LONG)
                        .setAction("Exito", null).show();

            }
        });




        //---------------------------------------

        copiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clave.getText().toString().isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Clave", clave.getText().toString());
                    clipboard.setPrimaryClip(clip);


                    Snackbar.make(view, "Clave copiada en el portapapeles!", Snackbar.LENGTH_LONG)
                            .setAction("Exito", null).show();

                }else{
                    Snackbar.make(view, "Ingrese una clave primero!", Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                }
            }
        });

        //------------------------------------

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clave.setTransformationMethod(null);

                ver.setVisibility(View.GONE);
                nover.setVisibility(View.VISIBLE);
            }
        });

        //------------------------------------

        nover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clave.setTransformationMethod(PasswordTransformationMethod.getInstance());
                nover.setVisibility(View.GONE);
                ver.setVisibility(View.VISIBLE);
            }
        });


        //----------------------


        PegarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                String texto = clipboard.getText().toString();
                System.out.println("Clipboard: "+texto);
                clave.setText(texto);

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            //Cancelado por el usuario
        }
        if ((requestCode == VALOR_RETORNO)) {

            try {
                //Procesar el resultado

                Uri uri = data.getData(); //obtener el uri content
                System.out.println("------------------------------");

                System.out.println("Uri.toString: " + uri.toString());
                System.out.println("Uri.getData.toString: " + data.getData().toString());
                System.out.println("data.getDataString: " + data.getDataString());
                System.out.println("Uri: " + uri.getPath());
                System.out.println("File to uri: " + uri.getEncodedPath());
                String selectedFilePath = FilePath.getPath(getActivity(), uri);
                System.out.println("Path: " + selectedFilePath);
                obtenerDirectorio od = new obtenerDirectorio();
                System.out.println("Path2: " + od.getRealPath(getContext(), data.getData()));


                if (selectedFilePath == null) {
                    selectedFilePath = obtenerDirectorio(uri.getPath());
                    System.out.println("------------------------------");

                    System.out.println(selectedFilePath);

                    System.out.println("------------------------------");
                }
                selecciono = true;
                archivoSelecionado = new File(selectedFilePath);
                seleccionar.setText(archivoSelecionado.getName());

            } catch (Exception e) {
                Snackbar.make(view, e.getMessage().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Error", null).show();
                System.out.println("Error : " + e.toString());


            }finally {
                obtenerDirectorio od = new obtenerDirectorio();
                String selectedFilePath = od.getRealPath(getContext(), data.getData());
                System.out.println("Path2: " +selectedFilePath);

               // selecciono = true;
            //    archivoSelecionado = new File(selectedFilePath);
              //  seleccionar.setText(archivoSelecionado.getName());

            }
        }

    }

    public String obtenerDirectorio(String uriData) {
        System.out.println("------------------------------");
        System.out.println("obteniendo directorio");


        String directorio = "";
        String ES = Environment.getExternalStorageDirectory().toString();

        System.out.println("------------------------------");
        int partidura = uriData.indexOf(':');
        char[] archivoChar = uriData.toCharArray();
        for (int i = partidura + 1; i < uriData.length(); i++) {
            directorio += archivoChar[i];
        }

        return ES + "/" + directorio;
    }




    public boolean CorromperArchivo(String pathInName, String pathOutName) {

        try {
            ArrayList<Integer> corruptHash = corrupt(getBytes(pathInName));
            writeBytes(corruptHash, pathOutName);
            //   new MimetypesFileTypeMap().getContentType(new File(pathInName));
            //  "/home/ephraim/Desktop/testfile"

            return true;
        } catch (Exception e) {
            System.out.println("Error al corromper archivo");
            return false;
        }

    }

    public ArrayList<Integer> getBytes(String filePath) {
        ArrayList<Integer> fileBytes = new ArrayList<Integer>();
        try {
            FileInputStream myInputStream = new FileInputStream(new File(filePath));
            do {
                int currentByte = myInputStream.read();
                if (currentByte == -1) {
                    System.out.println("broke loop");
                    break;
                }
                fileBytes.add(currentByte);
            } while (true);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(fileBytes);
        return fileBytes;
    }

    public void writeBytes(ArrayList<Integer> hash, String pathName) {
        try {
            OutputStream myOutputStream = new FileOutputStream(new File(pathName));
            for (int currentHash : hash) {
                myOutputStream.write(currentHash);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(hash);
    }

    //-----------------------
    // CORROMPER UN ARCHIVO
    //-----------------------

    public ArrayList<Integer> corrupt(ArrayList<Integer> hash) {
        ArrayList<Integer> corruptHash = new ArrayList<Integer>();
        ArrayList<Integer> keywordCodeArray = new ArrayList<Integer>();
        Integer keywordIndex = 0;
        String keyword = "coruptthisfilex2";

        for (int i = 0; i < keyword.length(); i++) {
            keywordCodeArray.add(keyword.codePointAt(i));
        }

        for (Integer currentByte : hash) {


            //Integer currentByteProduct = (keywordCodeArray.get(keywordIndex) + currentByte) / 2;
            Integer currentByteProduct = currentByte - keywordCodeArray.get(keywordIndex);
            if (currentByteProduct < 0) currentByteProduct += 255;
            corruptHash.add(currentByteProduct);

            if (keywordIndex == (keyword.length() - 1)) {
                keywordIndex = 0;
            } else keywordIndex++;
        }

        //System.out.println(corruptHash);
        return corruptHash;
    }





    public class AsyncTask_Encriptar extends AsyncTask<Void, Integer, Void> {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        protected void onPreExecute() {
            dialogoConfirmacion.dismiss();
            dialog = inflater.inflate(R.layout.dialog_barra, null);
            contructorDialog.setView(dialog);
            dialogoConfirmacion = contructorDialog.create();
            dialogoConfirmacion.setCancelable(false);
            dialogoConfirmacion.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                File lista = archivoSelecionado;

                String nombre = lista.getName();
                String rutaCompleta = lista.getAbsolutePath();
                int index = rutaCompleta.indexOf(nombre);
                Log.e("----- DATOS --------- ", lista.getPath());
                char[] ruta = rutaCompleta.toCharArray();
                String directorio = "";
                for (int i = 0; i < index; i++) {
                    directorio += ruta[i];

                }
                Log.e("Datos-", "directorio: " + directorio);
                Log.e("Datos-", "nombre: " + lista.getName());
                Log.e("Algoritmo-", " : " + algoritmoSeleccionado);
                File carpeta = new File(Environment.getExternalStorageDirectory() + "/CrApp/Cifrados");
                if (!carpeta.exists()) {
                    //carpeta.mkdir() creará la carpeta en la ruta indicada al inicializar el objeto File
                    if (carpeta.mkdir())
                        System.out.println("Carpeta creada");
                } else {
                    //la carpeta ya existe
                    System.out.println("Carpeta existe");
                }


                if (algoritmoSeleccionado.equals("AES 256")) {

                    cifrador.AESencriptar(claves, directorio, lista.getName(), carpeta.getPath());
                    Log.e("Algoritmo-", " : " + algoritmoSeleccionado);

                } else {

                    Log.e("Algoritmo-", " : " + algoritmoSeleccionado);
                    cifrador.TripleDesEncrypt(claves, directorio, lista.getName(), carpeta.getPath());

                }


                File actualizar = new File(directorio + "cipher_" + nombre);

                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(actualizar)));

                cifro = true;
                String rutas = directorio + lista.getName();
                corrompio = CorromperArchivo(rutas, rutas);


            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException k) {
                cifro = false;
                System.out.println("Error: " + k.toString());
                k.printStackTrace();


            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Void result) {


            dialogoConfirmacion.dismiss();
            selecciono = false;
            seleccionar.setText("Seleccione el Archivo");
            dialog = inflater.inflate(R.layout.dialog_cifrados, null);

            contructorDialog.setView(dialog);
            final AlertDialog dialogShow = contructorDialog.create();
            dialogShow.setCancelable(false);

            TextView direccionText = (TextView) dialog.findViewById(R.id.direccionGuardadoDialog);
            direccionText.setText(  "/CrApp/" + archivoSelecionado.getName());

            TextView corrompioText = (TextView) dialog.findViewById(R.id.corrompioVista);
            if (corrompio) {
                corrompioText.setText("Exitoso!");
                corrompioText.setTextColor(Color.GREEN);
            } else {
                corrompioText.setText("No Realizado!");
                corrompioText.setTextColor(Color.BLACK);
            }

            TextView cifroText = (TextView) dialog.findViewById(R.id.cifroVista);
            if (cifro) {
                cifroText.setText("Exitoso!");
                cifroText.setTextColor(Color.GREEN);
            } else {
                cifroText.setText("No Realizado!");
                cifroText.setTextColor(Color.BLACK);

            }

            dialogShow.setButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogShow.dismiss();

                }
            });
            dialogShow.show();


        }

    }





    public class AsyncTask_Desencriptar extends AsyncTask<Void, Integer, Void> {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        protected void onPreExecute() {


            dialog = inflater.inflate(R.layout.dialog_barra, null);

            contructorDialog.setView(dialog);
            dialogoConfirmacion = contructorDialog.create();
            dialogoConfirmacion.setCancelable(false);
            dialogoConfirmacion.show();


        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                File lista = archivoSelecionado;
                String nombre = lista.getName();
                String rutaCompleta = lista.getAbsolutePath();
                int index = rutaCompleta.indexOf(nombre);

                char[] ruta = rutaCompleta.toCharArray();
                String directorio = "";
                for (int i = 0; i < index; i++) {
                    directorio += ruta[i];

                }

                Log.e("Datos-", "directorio: " + directorio);
                Log.e("Datos-", "nombre: " + lista.getName());

                File carpeta = new File(Environment.getExternalStorageDirectory() + "/CrApp/Descifrados");
                if (!carpeta.exists()) {
                    //carpeta.mkdir() creará la carpeta en la ruta indicada al inicializar el objeto File
                    if (carpeta.mkdir())
                        System.out.println("Carpeta creada");
                } else {
                    //la carpeta ya existe
                    System.out.println("Carpeta existe");
                }

                if (algoritmoSeleccionado.equals("AES 256")) {
                    cifrador.AESdesencriptar(claves, directorio, lista.getName(), carpeta.getPath());
                    Log.e("Algoritmo-", " : " + algoritmoSeleccionado);

                } else {

                    Log.e("Algoritmo-", " : " + algoritmoSeleccionado);

                    cifrador.TipleDesDecrypt(claves, directorio, lista.getName(), carpeta.getPath());

                }


                File actualizar = new File(directorio + "cipher_" + nombre);

                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(actualizar)));


            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                    | BadPaddingException | IllegalBlockSizeException k) {

                k.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {


        }

        @Override
        protected void onPostExecute(Void result) {
            dialogoConfirmacion.dismiss();

            Snackbar.make(view, "Archivos Desencriptados", Snackbar.LENGTH_LONG)
                    .setAction("Exito", null).show();
            selecciono = false;
            seleccionar.setText("Seleccione el Archivo");
        }


    }


}