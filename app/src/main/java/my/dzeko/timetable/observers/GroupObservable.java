package my.dzeko.timetable.observers;

import java.util.List;

import my.dzeko.timetable.observers.interfaces.IGroupObserver;
import my.dzeko.timetable.observers.interfaces.Observable;

public class GroupObservable extends Observable<IGroupObserver> {
    private static GroupObservable mInstance;
    private GroupObservable() {}
    public static GroupObservable getInstance() {
        if(mInstance == null){
            mInstance = new GroupObservable();
        }
        return mInstance;
    }

    public void notifyGroupAdded(String groupName) {
        List<IGroupObserver> observers = getObservers();
        for (IGroupObserver observer: observers) {
            observer.onGroupAdded(groupName);
        }
    }

    public void notifyGroupRemoved(String deletedGroupName, String newSelectedGroupName) {
        List<IGroupObserver> observers = getObservers();
        for (IGroupObserver observer: observers) {
            observer.onGroupRemoved(deletedGroupName, newSelectedGroupName);
        }
    }
}
