package my.dzeko.timetable.interfaces;

public interface IPresenter {
    void onDestroy();
    void onUserClick();
    void registerView(IView view);
    void registerModel(IModel model);
}
