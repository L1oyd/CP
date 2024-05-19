package com.erop.spmapp.wallet;

import com.google.gson.annotations.SerializedName;

public class CardInfo {

    @SerializedName("balance")
    private int balance;

    @SerializedName("webhook")
    private String webhook;


    public int getBalance() {
        return balance;
    }

    public String getWebhook() {
        return webhook;
    }
}
