package com.erop.spmapp.wallet;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardUtils {

    public interface CardInfoCallback {
        void onCardInfoReceived(int balance);
        void onError(String error);
    }

    public interface CardsCallback {
        void onCardsReceived(List<String> cardNumbers);
        void onError(String error);
    }

    public static void getCardInfo(String ID, String TOKEN, CardInfoCallback callback) {
        String authValue = ID + ":" + TOKEN;
        String encodedAuthValue = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encodedAuthValue = Base64.getEncoder().encodeToString(authValue.getBytes(StandardCharsets.UTF_8));
        }
        String authorizationHeader = "Bearer " + encodedAuthValue;

        CardApi service = RetrofitClient.getClient().create(CardApi.class);
        Call<CardInfo> call = service.getCardInfo(authorizationHeader);
        call.enqueue(new Callback<CardInfo>() {
            @Override
            public void onResponse(Call<CardInfo> call, Response<CardInfo> response) {
                if (response.isSuccessful()) {
                    CardInfo cardInfo = response.body();
                    if (cardInfo != null) {
                        int balance = cardInfo.getBalance();
                        String webhook = cardInfo.getWebhook();
                        callback.onCardInfoReceived(balance);
                    } else {
                        callback.onError("No card info available");
                    }
                } else {
                    callback.onError("Request Error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<CardInfo> call, Throwable t) {
                callback.onError("Network Error: " + t.getMessage());
            }
        });
    }

    public interface AccountInfoCallback {
        void onAccountInfoReceived(String username);
        void onError(String error);
    }

    public static void getAccountInfo(String ID, String TOKEN, AccountInfoCallback callback) {
        String authValue = ID + ":" + TOKEN;
        String encodedAuthValue = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encodedAuthValue = Base64.getEncoder().encodeToString(authValue.getBytes(StandardCharsets.UTF_8));
        }
        String authorizationHeader = "Bearer " + encodedAuthValue;

        AccountApi service = RetrofitClient.getClient().create(AccountApi.class);

        Call<AccountInfo> call = service.getCurrentAccountInfo(authorizationHeader);
        call.enqueue(new Callback<AccountInfo>() {
            @Override
            public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                if (response.isSuccessful()) {
                    AccountInfo accountInfo = response.body();
                    if (accountInfo != null) {
                        String username = accountInfo.getUsername();
                        callback.onAccountInfoReceived(username);
                    } else {
                        callback.onError("No account info available");
                    }
                } else {
                    callback.onError("Request Error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                callback.onError("Network Error: " + t.getMessage());
            }
        });
    }
}
