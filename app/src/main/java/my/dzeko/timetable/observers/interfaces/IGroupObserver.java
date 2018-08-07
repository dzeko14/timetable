package my.dzeko.timetable.observers.interfaces;

public interface IGroupObserver extends IObserver {
    void onGroupAdded(String groupName);
    void onGroupRemoved(String groupName, String newSelectedGroupName);
}
