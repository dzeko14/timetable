package my.dzeko.timetable.interfaces;

import android.content.Context;

import java.util.List;

import my.dzeko.timetable.entities.Day;

public interface IView {
    void showLoading();
    void hideLoading();
    Context getContext();


}
