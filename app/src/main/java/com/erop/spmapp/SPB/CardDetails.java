package com.erop.spmapp.SPB;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.erop.spmapp.R;
import com.erop.spmapp.SPM.DeleteCardDialogFragment;
import com.erop.spmapp.wallet.CardUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CardDetails extends AppCompatActivity implements com.erop.spmapp.SPM.DeleteCardDialogFragment.DeleteCardDialogListener {
    EditText numberEditText;
    EditText sumFieldEditText;
    EditText commentEditText;
    ImageView cardImage;
    TextView cardName;
    TextView cardNumber;
    TextView cardBalance;


    Button deleteCard;
    Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);

        numberEditText = findViewById(R.id.number);
        sumFieldEditText = findViewById(R.id.sum);
        commentEditText = findViewById(R.id.comment);
        cardName = findViewById(R.id.cardName);
        cardNumber = findViewById(R.id.cardNumber);
        cardBalance = findViewById(R.id.cardBalance);
        deleteCard = findViewById(R.id.delete);
        send = findViewById(R.id.send);
        cardImage = findViewById(R.id.cardImage);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String name = extras.getString("EXTRA_CARD_NAME");
            String number = extras.getString("EXTRA_CARD_NUMBER");
            String balance = extras.getString("EXTRA_CARD_BALANCE");
            int colorInt = 0;
            try {
                colorInt = Integer.parseInt(extras.getString("EXTRA_CARD_COLOR"));
            } catch (NumberFormatException e) {
                Log.e("TAG", "ERROR: " + e.getMessage());
            }
            int finalColor = colorInt;

            String subNumber = number.substring(0, Math.min(number.length(), 5));
            Log.d("namenum", "onCreate: " + name + "  " + subNumber);

            String filename = "card_" + name + "_" + subNumber + ".txt";
            File directory = new File(this.getFilesDir(), "SPB") ;
            File file = new File(directory, filename);

            deleteCard.setOnClickListener(v -> {
                com.erop.spmapp.SPM.DeleteCardDialogFragment dialogFragment = DeleteCardDialogFragment.newInstance(name, number);
                dialogFragment.show(getSupportFragmentManager(), "delete_card_dialog");
            });



            try {
                Map<String, String> credentials = loadCredentialsFromFile(file);
                String id = credentials.get("ID");
                String token = credentials.get("TOKEN");
                numberEditText = findViewById(R.id.number);
                sumFieldEditText = findViewById(R.id.sum);
                commentEditText = findViewById(R.id.comment);

                CardUtils.getAccountInfo(id, token, new CardUtils.AccountInfoCallback() {
                            @Override
                            public void onAccountInfoReceived(String username) {
                                TextView Username = findViewById(R.id.username);
                                Username.setText(username + ": ");
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String receiver = numberEditText.getText().toString();
                        String strAmount = sumFieldEditText.getText().toString();
                        String comment = commentEditText.getText().toString();
                        int amount;
                        try {
                            amount = Integer.parseInt(strAmount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            amount = 0;
                        }

                        int finalAmount = amount;
                        receive(id, token, receiver, finalAmount, comment);
                        finish();
                    }
                });

            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                Log.e("TAG", "onCreate: ", e);
            }

            cardName.setText(name);
            cardNumber.setText(number);
            cardBalance.setText(balance);

            switch (finalColor){
                case 0: cardImage.setImageResource(R.drawable.card_0);
                    break;
                case 1: cardImage.setImageResource(R.drawable.card_1);
                    break;
                case 2: cardImage.setImageResource(R.drawable.card_2);
                    break;
                case 3: cardImage.setImageResource(R.drawable.card_3);
                    break;
                case 4: cardImage.setImageResource(R.drawable.card_4);
                    break;
                case 5: cardImage.setImageResource(R.drawable.card_5);
                    break;
                case 6: cardImage.setImageResource(R.drawable.card_6);
                    break;
                case 7: cardImage.setImageResource(R.drawable.card_7);
                    break;

            }
        }
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        // Пользователь подтвердил удаление, закрываем активность
        finish();
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
    }


    private Map<String, String> loadCredentialsFromFile(File file) throws IOException, GeneralSecurityException {
        Map<String, String> credentials = new HashMap<>();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    credentials.put(parts[0], parts[1]);

                    String data = outputStream.toString("UTF-8");
                    parseCredentials(data, credentials);
                }
            }
        }

        return credentials;
    }

    private void parseCredentials(String data, Map<String, String> credentials) {
        String[] lines = data.split("\n");
        for (String line : lines) {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                if ("ID".equals(key) || "TOKEN".equals(key)) {
                    credentials.put(key, value);
                }
            }
        }
    }

    public void receive(String ID, String TOKEN, String receiver, int amount, String comment) {
        OkHttpClient client = new OkHttpClient();

        if (Objects.equals(comment, ""))
            comment = "Нет комментария";

        String finalComment = comment;
        CardUtils.getAccountInfo(ID, TOKEN, new CardUtils.AccountInfoCallback() {
            @Override
            public void onAccountInfoReceived(String username) {
                String finalfinalcomment = username + ": " +finalComment;

                String json = "{\"receiver\":\""+receiver+"\",\"amount\":"+amount+",\"comment\":\""+ finalfinalcomment +"\"}";

                String authValue = ID + ":" + TOKEN;
                String encodedAuthValue = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    encodedAuthValue = Base64.getEncoder().encodeToString(authValue.getBytes(StandardCharsets.UTF_8));
                }
                String authorizationHeader = encodedAuthValue;

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

                Request request = new Request.Builder()
                        .url("https://spworlds.ru/api/public/transactions")
                        .post(requestBody)
                        .addHeader("Authorization", "Bearer " + authorizationHeader)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }

                        String responseBody = response.body().string();
                        System.out.println(responseBody);
                    }
                });
            }
            @Override
            public void onError(String error) {
            }
        });
    }
}
