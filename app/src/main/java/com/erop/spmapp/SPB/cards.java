package com.erop.spmapp.SPB;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.erop.spmapp.R;
import com.erop.spmapp.wallet.Card;
import com.erop.spmapp.SPB.CardDetails;
import com.erop.spmapp.wallet.CardUtils;
import com.erop.spmapp.wallet.SelectListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cards extends Fragment implements SelectListener {
    private RecyclerView cardsRV;
    private ArrayList<Card> cardArrayList = new ArrayList<>();
    private TextView addCard;
    private CardAdapter cardAdapter;

    public static cards newInstance() {
        return new cards();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cards, container, false);

        cardAdapter = new CardAdapter(requireContext(), cardArrayList, this);
        cardsRV = view.findViewById(R.id.cardsRV);
        cardsRV.setLayoutManager(new LinearLayoutManager(requireContext()));
        cardsRV.setAdapter(cardAdapter);

        Map<String, Map<String, String>> allCredentials = loadAllCredentialsFromDirectory();
        for (Map.Entry<String, Map<String, String>> entry : allCredentials.entrySet()) {
            Map<String, String> credentials = entry.getValue();
            if (!credentials.isEmpty()) {
                String id = credentials.get("ID");
                String token = credentials.get("TOKEN");
                String name = credentials.get("NAME");
                String number = credentials.get("NUMBER");
                int colorInt = 0;
                try {
                    colorInt = Integer.parseInt(credentials.get("COLOR"));
                } catch (NumberFormatException e) {
                    Log.e("TAG", "ERROR: " + e.getMessage());
                }

                int finalColor = colorInt;
                CardUtils.getCardInfo(id, token, new CardUtils.CardInfoCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onCardInfoReceived(int balance) {
                        Card newCard = new Card(name, number, balance, finalColor);
                        cardArrayList.add(newCard);
                        getActivity().runOnUiThread(() -> cardAdapter.notifyDataSetChanged());
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("CardError", "Error retrieving card info: " + error);
                    }
                });
            }
        }

        addCard = view.findViewById(R.id.addButton);
        addCard.setOnClickListener(v -> {
            CreateCardDialogFragment dialogFragment = new CreateCardDialogFragment();

            dialogFragment.show(getParentFragmentManager(), "createCardDialog");
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void initializeData(String name, String number, int balance, int color) {
        cardArrayList.add(new Card(name, number, balance, color));
        Log.d("CardsFragment", "Adding card: Name=" + name + ", Number=" + number + ", Balance=" + balance + ", Color=" + color);
        cardAdapter.notifyDataSetChanged();
    }
    @Override
    public void OnItemClicked(Card card) {
        Intent intent = new Intent(getContext(), CardDetails.class);
        intent.putExtra("EXTRA_CARD_NAME", card.getName());
        intent.putExtra("EXTRA_CARD_NUMBER", card.getNumber());
        intent.putExtra("EXTRA_CARD_BALANCE", String.valueOf(card.getBalance()));
        intent.putExtra("EXTRA_CARD_COLOR", String.valueOf(card.getColor()));
        getContext().startActivity(intent);
    }

    private Map<String, Map<String, String>> loadAllCredentialsFromDirectory() {
        Map<String, Map<String, String>> allCredentials = new HashMap<>();
        File directory = new File(getContext().getFilesDir(), "SPB") ;

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try {
                        Map<String, String> credentials = loadCredentialsFromFile(file);
                        allCredentials.put(file.getName(), credentials);
                    } catch (Exception e) {
                        Log.e("LoadCredentials", "Error loading credentials from file: " + file.getName(), e);
                    }
                }
            }
        }
        return allCredentials;
    }

    private Map<String, String> loadCredentialsFromFile(File file) throws IOException {
        Map<String, String> credentials = new HashMap<>();

        // Читаем данные из файла
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);
                }
            }
        }

        return credentials;
    }

}
