package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.SpesiesModel;

import java.util.List;

public interface IFirebaseLoadDoneSpesies extends IFirebaseLoadDone{
    void onFirebaseLoadSuccessSpesies(List<SpesiesModel> spesiesList);
}
