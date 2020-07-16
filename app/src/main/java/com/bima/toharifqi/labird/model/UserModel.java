package com.bima.toharifqi.labird.model;

public class UserModel {
    private String email, nama, password, userName, waNumber;

    public UserModel() {
    }

    public UserModel(String email, String nama, String password, String userName, String waNumber) {
        this.email = email;
        this.nama = nama;
        this.password = password;
        this.userName = userName;
        this.waNumber = waNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWaNumber() {
        return waNumber;
    }

    public void setWaNumber(String waNumber) {
        this.waNumber = waNumber;
    }
}
