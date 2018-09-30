package my.dzeko.timetable.contracts;

import java.util.List;

import my.dzeko.timetable.adapters.RemoveScheduleAdapter;
import my.dzeko.timetable.entities.Group;
import my.dzeko.timetable.interfaces.IPresenter;
import my.dzeko.timetable.interfaces.IView;

public class RemoveScheduleContract {
    public interface View extends IView {
        void setGroupList(List<Group> groups);
    }

    public interface Presenter extends IPresenter, RemoveScheduleAdapter.OnRemoveClickListener {
        void onGroupListRequest();
    }
}
