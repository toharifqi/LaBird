package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.QuizModel;

import java.util.List;

public interface IFirebaseLoadDoneQuiz {
    void onFirebaseLoadSuccess(List<QuizModel> quizList);
    void onFirebaseLoadFailed(String message);
}
