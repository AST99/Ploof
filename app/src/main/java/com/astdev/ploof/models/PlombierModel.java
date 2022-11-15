package com.astdev.ploof.models;

public class PlombierModel {

    private String nomPrenom, mail, passWrd, phone;

    public PlombierModel() {
    }

    public PlombierModel(String nomPrenom, String mail, String passWrd, String phone) {
        this.nomPrenom = nomPrenom;
        this.mail = mail;
        this.passWrd = passWrd;
        this.phone = phone;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassWrd() {
        return passWrd;
    }

    public void setPassWrd(String passWrd) {
        this.passWrd = passWrd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
