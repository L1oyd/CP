package com.erop.spmapp.SP;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.erop.spmapp.R;

import java.io.File;

public class DeleteCardDialogFragment extends DialogFragment {

    public interface DeleteCardDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    DeleteCardDialogListener listener;

    public static DeleteCardDialogFragment newInstance(String name, String number) {
        DeleteCardDialogFragment fragment = new DeleteCardDialogFragment();
        Bundle args = new Bundle();
        args.putString("EXTRA_CARD_NAME", name);
        args.putString("EXTRA_CARD_NUMBER", number);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_card_dialog, null);
        builder.setView(view);

        Bundle args = getArguments();
        String name = args != null ? args.getString("EXTRA_CARD_NAME") : "Unknown";
        String number = args != null ? args.getString("EXTRA_CARD_NUMBER") : "Unknown";

        Button OK = view.findViewById(R.id.ok);
        Button CANCEL = view.findViewById(R.id.cancel);

        OK.setOnClickListener(v -> {
            deleteCard(name, number);
            listener.onDialogPositiveClick(this); // Уведомляем активность
            dismiss();
        });

        CANCEL.setOnClickListener(v -> {
            listener.onDialogNegativeClick(this); // Уведомляем активность
            dismiss();
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Попытка привязать обратный вызов к активности
        try {
            listener = (DeleteCardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DeleteCardDialogListener");
        }
    }

    private void deleteCard(String name, String number) {
        String subNumber = number.substring(0, Math.min(number.length(), 5));
        String filename = "card_" + name + "_" + subNumber + ".txt";
        File directory = new File(getActivity().getFilesDir(), "SP");
        File fileToDelete = new File(directory, filename);
        if (fileToDelete.exists() && fileToDelete.delete()) {
            Log.d("DeleteCard", "Card file deleted successfully.");
        } else {
            Log.e("DeleteCard", "Failed to delete the card file or no file found.");
        }
    }
}
