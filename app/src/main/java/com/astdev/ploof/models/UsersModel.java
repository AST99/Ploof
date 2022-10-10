package com.astdev.ploof.models;

import com.google.android.gms.maps.model.LatLng;

public class UsersModel {

    String nomPrenom, mail, phone, passWrd, latitude, longitude,
            numeroCompteur, image, atHome, description, quartier, nbrePersonne;
    public LatLng position;

    public UsersModel() {
    }

    public UsersModel(String phone){
        this.phone = phone;
    }

    /********************************Refonte page d'inscription*************************************/
    public UsersModel(String nomPrenom, String mail, String numeroTel, String passWrd, String latitude,
                      String longitude, String atHome, String description, String quartier, String nbrePersonne){
        this.nomPrenom = nomPrenom;
        this.mail = mail;
        this.passWrd = passWrd;
        this.phone = numeroTel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.atHome = atHome;
        this.description = description;
        this.quartier = quartier;
        this.nbrePersonne = nbrePersonne;
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

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getNbrePersonne() {
        return nbrePersonne;
    }

    public void setNbrePersonne(String nbrePersonne) {
        this.nbrePersonne = nbrePersonne;
    }
}
