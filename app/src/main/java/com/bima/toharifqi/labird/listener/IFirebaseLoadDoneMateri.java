package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.MateriModel;

import java.util.ArrayList;
import java.util.List;

public interface IFirebaseLoadDoneMateri extends IFirebaseLoadDone{
    void onFirebaseLoadSuccessMateri(ArrayList<MateriModel> materiList);

}
