package com.erop.spmapp.wallet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountInfo {
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("minecraftUUID")
    private String minecraftUUID;

    @SerializedName("status")
    private String status;

    @SerializedName("roles")
    private List<String> roles;

    @SerializedName("city")
    private City city;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("cards")
    private List<Card> cards;


    public AccountInfo(String id, String username, String minecraftUUID, String status) {
        this.id = id;
        this.username = username;
        this.minecraftUUID = minecraftUUID;
        this.status = status;
    }


    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMinecraftUUID() {
        return minecraftUUID;
    }

    public void setMinecraftUUID(String minecraftUUID) {
        this.minecraftUUID = minecraftUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public static class City {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        @SerializedName("x")
        private double x;

        @SerializedName("z")
        private double z;

        @SerializedName("isMayor")
        private boolean isMayor;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public boolean isMayor() {
            return isMayor;
        }

        public void setMayor(boolean mayor) {
            isMayor = mayor;
        }
    }

    public class Card {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("number")
        private String number;

        @SerializedName("color")
        private int color;

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

    }
}

