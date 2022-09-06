package com.astdev.ploof;

import com.google.android.gms.maps.model.LatLng;

public class UsersModel {

    String nomPrenom, mail, phone, passWrd, latitude, longitude, numeroCompteur, image, atHome, description;
    public LatLng position;
    boolean isConnected;

    public UsersModel() {
    }

    /**********************************Connexion***************************************************/
    public UsersModel(String nomPrenom, String mail, String passWrd) {
        this.nomPrenom = nomPrenom;
        this.mail = mail;
        this.phone = phone;
        this.passWrd = passWrd;
    }
    /***********************************************************************************************/


    public UsersModel(String latitude, String longitude, String atHome, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.atHome = atHome;
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNumeroCompteur() {
        return numeroCompteur;
    }

    public void setNumeroCompteur(String numeroCompteur) {
        this.numeroCompteur = numeroCompteur;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAtHome() {
        return atHome;
    }

    public void setAtHome(String atHome) {
        this.atHome = atHome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
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