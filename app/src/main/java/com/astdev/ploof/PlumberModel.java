package com.astdev.ploof;

public class PlumberModel {

    String nomPrenoms, numeroTel;
    int imgId, note;

    public PlumberModel(String nomPrenoms, String numeroTel, int imgId) {
       this.nomPrenoms = nomPrenoms;
       this.numeroTel = numeroTel;
       this.imgId = imgId;
    }

    public String getNomPrenoms() {
        return nomPrenoms;
    }

    public void setNomPrenoms(String nomPrenoms) {
        this.nomPrenoms = nomPrenoms;
    }

    public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }
}
