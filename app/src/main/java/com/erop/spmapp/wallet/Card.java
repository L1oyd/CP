package com.erop.spmapp.wallet;

public class Card {
    private String id;
    private String name;
    private String number;
    private int color;
    private int balance;

    public Card(String name, String number, int balance, int color) {
        this.name = name;
        this.number = number;
        this.balance = balance;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
