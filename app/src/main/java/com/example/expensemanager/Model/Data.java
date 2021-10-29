package com.example.expensemanager.Model;

public class Data {

    private int amount;
    private String note;
    private String category;
    private String id;
    private String mode;
    private String date;
    private String currency;

    public Data(int amount, String note, String category, String id, String mode, String date, String currency) {
        this.amount = amount;
        this.note = note;
        this.category = category;
        this.id = id;
        this.mode = mode;
        this.date = date;
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Data(){

    }


}
