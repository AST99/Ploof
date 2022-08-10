package com.astdev.ploof;

public class UsersModel {

    String nomPrenom, mail, phone, passWrd;
    boolean isConnected;

    public UsersModel() {
    }

    public UsersModel(String nomPrenom, String mail, String phone, String passWrd) {
        this.nomPrenom = nomPrenom;
        this.mail = mail;
        this.phone = phone;
        this.passWrd = passWrd;
    }

    public UsersModel(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassWrd() {
        return passWrd;
    }

    public void setPassWrd(String passWrd) {
        this.passWrd = passWrd;
    }
}
