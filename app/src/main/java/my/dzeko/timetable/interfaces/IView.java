package my.dzeko.timetable.interfaces;

import android.content.Context;

public interface IView {
    void showLoading();
    void hideLoading();
    Context getContext();
}
