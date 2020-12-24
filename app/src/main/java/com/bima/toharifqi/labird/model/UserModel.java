package com.bima.toharifqi.labird.model;

public class UserModel extends UserClass {


    public String userName;

    public UserModel() {
    }

    public UserModel(String email2, String nama2, String userName2, String waNumber2, String uId2) {
        this.email = email2;
        this.nama = nama2;
        this.userName = userName2;
        this.waNumber = waNumber2;
        this.uId = uId2;
    }

    public String getUserName() {
        return userName;
    }
}
