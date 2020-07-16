package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.LeaderBoardModel;

import java.util.List;

public interface IFirebaseLoadDoneBoard {
    void onFirebaseLoadSuccess(List<LeaderBoardModel> boardList);
    void onFirebaseLoadFailed(String message);
}
