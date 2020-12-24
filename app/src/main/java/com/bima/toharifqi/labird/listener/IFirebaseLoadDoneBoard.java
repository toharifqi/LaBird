package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.LeaderBoardModel;

import java.util.List;

public interface IFirebaseLoadDoneBoard extends IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<LeaderBoardModel> boardList);
}
