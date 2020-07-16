package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.MateriModel;

import java.util.List;

public interface IFirebaseLoadDoneMateri {
    void onFirebaseLoadMateriSuccess(List<MateriModel> materiList);
    void onFirebaseLoadMateriFailed(String message);

}
