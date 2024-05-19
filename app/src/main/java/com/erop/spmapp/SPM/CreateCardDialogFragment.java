package com.erop.spmapp.SPM;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.security.crypto.EncryptedFile;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;

import com.erop.spmapp.MainActivity;
import com.erop.spmapp.R;
import com.erop.spmapp.wallet.CardUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

public class CreateCardDialogFragment extends DialogFragment {

    private Button OK;
    private Button CANCEL;
    private Spinner colorSpinner;
    private int color;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Используйте Builder для создания AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_card_dialog, null);
        OK = view.findViewById(R.id.ok);
        CANCEL = view.findViewById(R.id.cancel);

        colorSpinner = view.findViewById(R.id.cardColor);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.example_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapter);

        EditText id = view.findViewById(R.id.id);
        EditText token = view.findViewById(R.id.token);
        EditText name = view.findViewById(R.id.name);
        EditText number = view.findViewById(R.id.number);
        builder.setView(view);


        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        OK.setOnClickListener(v -> {
            String ID = id.getText().toString();
            String TOKEN = token.getText().toString();
            String NAME = name.getText().toString();
            String NUMBER = number.getText().toString();

            CardUtils.getCardInfo(ID, TOKEN, new CardUtils.CardInfoCallback() {
                @Override
                public void onCardInfoReceived(int balance) {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).initializeData(NAME, NUMBER, balance, color);
                        saveCredentials(ID, TOKEN, NAME, NUMBER, String.valueOf(color));
                    }
                    dismiss();
                }

                @Override
                public void onError(String error) {
                    Log.e("CreateCardDialog", "Error: " + error);
                }
            });
        });

        CANCEL.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    private void saveCredentials(String id, String token, String name, String number, String color) {



        // Создание объекта File, представляющего вложенную папку
        File spmFolder = new File(requireContext().getFilesDir(), "SPM");

        // Проверка существования папки и создание ее при необходимости
        if (!spmFolder.exists()) {
            boolean created = spmFolder.mkdir();
            if (!created) {
                Log.e("File Directory", "Failed to create directory: " + spmFolder.getPath());
                return;
            }
        }

        String filename = "card_" + name + "_" + number + ".txt";  // Используем расширение .txt для обычных текстовых файлов


        try {

            File file = new File(spmFolder, filename);
            if (!file.exists()) {
                file.createNewFile();  // Убедимся, что файл создан
            }

            // Сохраняем данные в виде строки
            String dataToSave = "ID=" + id + "\nTOKEN=" + token + "\nNAME=" + name + "\nNUMBER=" + number + "\nCOLOR=" + color;

            // Записываем данные в файл
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(dataToSave.getBytes(StandardCharsets.UTF_8));
                Log.d("SaveData", "Data saved successfully in plain text.");
            }
        } catch (IOException e) {
            Log.e("SaveDataError", "Failed to save data in plain text", e);
        }
    }

}
