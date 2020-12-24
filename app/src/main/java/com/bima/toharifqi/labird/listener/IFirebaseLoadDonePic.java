package com.bima.toharifqi.labird.listener;

import java.util.List;

public interface IFirebaseLoadDonePic extends IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<String> picList);
}
