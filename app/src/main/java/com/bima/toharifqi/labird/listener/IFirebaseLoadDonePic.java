package com.bima.toharifqi.labird.listener;

import java.util.List;

public interface IFirebaseLoadDonePic {
    void onFirebaseLoadSuccess(List<String> picList);
    void onFirebaseLoadFailed(String message);
}
