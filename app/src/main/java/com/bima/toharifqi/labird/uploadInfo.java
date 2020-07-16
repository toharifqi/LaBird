package com.bima.toharifqi.labird;

public class uploadInfo {
    public String imageName, imageUrl;

    public uploadInfo() {
    }

    public uploadInfo(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
