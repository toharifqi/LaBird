package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.ExpertModel;

import java.util.List;

public interface IFirebaseLoadDoneExpert extends IFirebaseLoadDone{
    void onFirebaseLoadSuccess(List<ExpertModel> expertModels);
}
