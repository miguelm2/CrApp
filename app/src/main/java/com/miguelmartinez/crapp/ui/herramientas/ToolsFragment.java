package com.miguelmartinez.crapp.ui.herramientas;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.miguelmartinez.crapp.clases.Cifrados;
import com.miguelmartinez.crapp.R;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    Button cifrarCesar, descifrarCesar, cifrarMD5, cifrarXor, descifrarXor;
    Button btnCopiarCesar, btnCopiarHash;
    TextView textoCesar, cipherCesar, corrimiento, entradaMD5, salidaMD5, textoXor, cipherXor;
    View view;
    ToggleButton mostrarCesar, mostrarMD5, mostrarXor;
    LinearLayout contenedorCesar, contenedorMD5, contenedorXOR;
    Spinner spinerHash;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        view  = inflater.inflate(R.layout.fragment_herramientas, container, false);

        mostrarCesar = (ToggleButton) view.findViewById(R.id.btnCesar);
        mostrarMD5 = (ToggleButton) view.findViewById(R.id.btnMostrarMd5);
        mostrarXor = (ToggleButton) view.findViewById(R.id.btnMostarXor);


        contenedorCesar = (LinearLayout) view.findViewById(R.id.contenedorCesar);
        contenedorMD5 = (LinearLayout) view.findViewById(R.id.contenedorMD5);
        contenedorXOR = (LinearLayout) view.findViewById(R.id.contenedorXOR);

        contenedorCesar.setVisibility(View.GONE);
        contenedorMD5.setVisibility(View.GONE);
        contenedorXOR.setVisibility(View.GONE);

        cifrarMD5 = (Button) view.findViewById(R.id.btnCifrarMD5);
        cifrarCesar = (Button) view.findViewById(R.id.cCesar);
        descifrarCesar = (Button) view.findViewById(R.id.dCesar);
        cifrarXor = (Button) view.findViewById(R.id.btnCifrarXOR);
        descifrarXor = (Button) view.findViewById(R.id.btnDescifrarXor);
        btnCopiarCesar = (Button) view.findViewById(R.id.btnCopiarCesar);
        btnCopiarHash = (Button) view.findViewById(R.id.btnCopiarHash);

        textoCesar = (TextView) view.findViewById(R.id.textoCesar);
        cipherCesar = (TextView) view.findViewById(R.id.cipherCesar);
        corrimiento = (TextView) view.findViewById(R.id.corrimiento);
        entradaMD5 = (TextView) view.findViewById(R.id.entradaMD5);
        salidaMD5 = (TextView) view.findViewById(R.id.salidaMD5);
        textoXor = (TextView) view.findViewById(R.id.textoXor);
        cipherXor = (TextView) view.findViewById(R.id.cipherXOR);

        cipherCesar.setKeyListener(null);
        salidaMD5.setKeyListener(null);
        cipherXor.setKeyListener(null);
        spinerHash = (Spinner) view.findViewById(R.id.spinnerHash);

        String[] algoritmos = {"MD5", "SHA-1", "SHA-2"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, algoritmos);
        spinerHash.setAdapter(adaptador);






        mostrarCesar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    mostrarCesar.setBackgroundResource(R.color.colorLogin);
                    contenedorCesar.setVisibility(View.VISIBLE);
                    mostrarMD5.setBackgroundResource(R.color.rosado);
                    mostrarMD5.setChecked(false);
                    mostrarXor.setChecked(false);
                    mostrarCesar.setChecked(true);
                    contenedorXOR.setVisibility(View.GONE);
                    contenedorMD5.setVisibility(View.GONE);


                } else {
                    mostrarCesar.setBackgroundResource(R.color.rosado);
                    contenedorCesar.setVisibility(View.GONE);
                    mostrarMD5.setChecked(false);
                    mostrarXor.setChecked(false);
                    mostrarCesar.setChecked(false);


                }
            }
        });


        //--------------------------------

        mostrarMD5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    mostrarMD5.setBackgroundResource(R.color.colorLogin);
                    contenedorMD5.setVisibility(View.VISIBLE);
                    mostrarCesar.setChecked(false);
                    mostrarXor.setChecked(false);
                    mostrarMD5.setChecked(true);
                    contenedorXOR.setVisibility(View.GONE);
                    contenedorCesar.setVisibility(View.GONE);


                } else {
                    mostrarMD5.setBackgroundResource(R.color.rosado);
                    mostrarCesar.setChecked(false);
                    mostrarXor.setChecked(false);
                    contenedorMD5.setVisibility(View.GONE);
                }
            }
        });


        //----------------------------------
        mostrarXor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    mostrarXor.setBackgroundResource(R.color.colorLogin);
                    mostrarCesar.setChecked(false);
                    mostrarMD5.setChecked(false);
                    mostrarXor.setChecked(true);
                    contenedorXOR.setVisibility(View.VISIBLE);
                    contenedorCesar.setVisibility(View.GONE);
                    contenedorMD5.setVisibility(View.GONE);

                } else {
                    mostrarXor.setBackgroundResource(R.color.rosado);
                    mostrarCesar.setChecked(false);
                    mostrarMD5.setChecked(false);
                    mostrarXor.setChecked(false);
                    contenedorXOR.setVisibility(View.GONE);


                }
            }
        });

        //--------------------------------

        cifrarCesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String textoPlano = textoCesar.getText().toString();
                    int correr = Integer.parseInt(corrimiento.getText().toString());

                    if (textoPlano.length() > 0) {

                        if (correr > 0) {

                            Cifrados cifrar = new Cifrados();

                            String cipher = cifrar.Cifrar_Cesar(textoPlano, correr);

                            cipherCesar.setText(cipher);


                        } else {

                            Snackbar.make(view, "Corrimiento Vacio!", Snackbar.LENGTH_LONG)
                                    .setAction("Alerta", null).show();
                        }

                    } else {
                        Snackbar.make(view, "Texto Vacio!", Snackbar.LENGTH_LONG)
                                .setAction("Alerta", null).show();
                    }
                } catch (Exception e) {
                    System.out.println("Error :" + e.toString());
                }

            }
        });

        //---------------------------------------

        descifrarCesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String textoPlano = textoCesar.getText().toString();
                    int correr = Integer.parseInt(corrimiento.getText().toString());

                    if (textoPlano.length() > 0) {

                        if (correr > 0) {

                            Cifrados cifrar = new Cifrados();

                            String cipher = cifrar.Descifrar_Cesar(textoPlano, correr);

                            cipherCesar.setText(cipher);


                        } else {

                            Snackbar.make(view, "Corrimiento Vacio!", Snackbar.LENGTH_LONG)
                                    .setAction("Alerta", null).show();
                        }

                    } else {
                        Snackbar.make(view, "Texto Vacio!", Snackbar.LENGTH_LONG)
                                .setAction("Alerta", null).show();
                    }
                } catch (Exception e) {
                    System.out.println("Error :" + e.toString());
                }
            }
        });


        //----------------------------------


        cifrarMD5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String entrada = entradaMD5.getText().toString();
                    if (entrada.length() > 0) {
                        Cifrados cifrador = new Cifrados();

                        String salida = cifrador.MD5(entrada);

                        salidaMD5.setText(salida);
                    } else {
                        Snackbar.make(view, "Texto Vacio!", Snackbar.LENGTH_LONG)
                                .setAction("Alerta", null).show();

                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.toString());
                }
            }
        });


        //------------------------

        btnCopiarCesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cipherCesar.getText().toString().length() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Clave", cipherCesar.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(view, "Texto cifrado copiado en el portapapeles!", Snackbar.LENGTH_LONG)
                            .setAction("Alerta", null).show();
                } else {
                    Snackbar.make(view, "Texto Vacio!", Snackbar.LENGTH_LONG)
                            .setAction("Alerta", null).show();
                }

            }
        });


        //-------------------------------------

        btnCopiarHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (salidaMD5.getText().toString().length() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Clave", salidaMD5.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(view, "Texto cifrado copiado en el portapapeles!", Snackbar.LENGTH_LONG)
                            .setAction("Alerta", null).show();
                } else {
                    Snackbar.make(view, "Texto Vacio!", Snackbar.LENGTH_LONG)
                            .setAction("Alerta", null).show();
                }
            }
        });

        return view;
    }
}