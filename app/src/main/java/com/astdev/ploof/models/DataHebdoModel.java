package com.astdev.ploof.models;

public class DataHebdoModel {

    String date;
    double conso, min, max;

    public DataHebdoModel() {
    }

    public DataHebdoModel(String date, double conso){
        this.date=date;
        this.conso=conso;
    }

    public DataHebdoModel(String date, double conso, double min, double max) {
        this.date = date;
        this.conso = conso;
        this.min = min;
        this.max = max;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getConso() {
        return conso;
    }

    public void setConso(double conso) {
        this.conso = conso;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
