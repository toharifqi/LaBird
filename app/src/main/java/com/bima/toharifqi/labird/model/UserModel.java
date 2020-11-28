package com.bima.toharifqi.labird.model;

public class UserModel {
    private String email;
    private String nama;
    private String password;
    private String uId;
    private String userName;
    private String waNumber;

    public UserModel() {
    }

    public UserModel(String email2, String nama2, String userName2, String waNumber2, String uId2) {
        this.email = email2;
        this.nama = nama2;
        this.userName = userName2;
        this.waNumber = waNumber2;
        this.uId = uId2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama2) {
        this.nama = nama2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName2) {
        this.userName = userName2;
    }

    public String getWaNumber() {
        return this.waNumber;
    }

    public void setWaNumber(String waNumber2) {
        this.waNumber = waNumber2;
    }

    public String getuId() {
        return this.uId;
    }

    public void setuId(String uId2) {
        this.uId = uId2;
    }
}
