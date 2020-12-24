package com.bima.toharifqi.labird.listener;

import com.bima.toharifqi.labird.model.QuizModel;

import java.util.List;

public interface IFirebaseLoadDoneQuiz extends IFirebaseLoadDone{
    void onFirebaseLoadSuccessQuiz(List<QuizModel> quizList);
}
