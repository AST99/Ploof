package com.astdev.ploof;

public class ListFuiteModel {
    String date, statut;

    public ListFuiteModel() {
    }

    public ListFuiteModel(String date, String statut) {
        this.date = date;
        this.statut = statut;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
