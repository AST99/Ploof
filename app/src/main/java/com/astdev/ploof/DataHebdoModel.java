package com.astdev.ploof;

public class DataHebdoModel {

    String date;
    double conso, min,max;

    public DataHebdoModel() {
    }

    public DataHebdoModel(String date, double conso, double min, double max) {
        this.date = date;
        this.conso = conso;
        this.min = min;
        this.max = max;
    }
}
