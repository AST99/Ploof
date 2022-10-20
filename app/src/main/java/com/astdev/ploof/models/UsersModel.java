package com.astdev.ploof.models;

public class UsersModel {

    String nomPrenom, mail, phone, passWrd, numeroCompteur, image, atHome, description, quartier,
            nbrePersonne,position, conso, consoMoyenne;

    public UsersModel() {
    }

    public UsersModel(String phone){
        this.phone = phone;
    }

    /********************************Refonte page d'inscription*************************************/
    public UsersModel(String nomPrenom, String mail, String numeroTel, String passWrd, String position,
                      String atHome, String description, String quartier, String nbrePersonne, String conso,
                      String consoMoyene){
        this.nomPrenom = nomPrenom;
        this.mail = mail;
        this.passWrd = passWrd;
        this.phone = numeroTel;
        this.position = position;
        this.atHome = atHome;
        this.description = description;
        this.quartier = quartier;
        this.nbrePersonne = nbrePersonne;
        this.conso=conso;
        this.consoMoyenne = consoMoyene;
    }

    public String getConso() {
        return conso;
    }

    public String getConsoMoyenne() {
        return consoMoyenne;
    }

    public void setConsoMoyenne(String consoMoyenne) {
        this.consoMoyenne = consoMoyenne;
    }

    public void setConso(String conso) {
        this.conso = conso;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
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
