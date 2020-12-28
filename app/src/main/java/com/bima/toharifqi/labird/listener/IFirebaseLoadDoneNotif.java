package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.NotifModel;

import java.util.List;

public interface IFirebaseLoadDoneNotif extends IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<NotifModel> notifList);
}
