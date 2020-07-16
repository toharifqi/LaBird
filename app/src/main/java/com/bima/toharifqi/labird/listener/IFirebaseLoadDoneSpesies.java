package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.SpesiesModel;

import java.util.List;

public interface IFirebaseLoadDoneSpesies {
    void onFirebaseLoadSuccess(List<SpesiesModel> spesiesList);
    void onFirebaseLoadFailed(String message);
}
