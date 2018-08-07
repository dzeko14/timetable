package my.dzeko.timetable.observers.interfaces;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T extends IObserver> {
    private List<T> mObservers = new ArrayList<>();

    public void registerObserver(T observer) {
        mObservers.add(observer);
    }

    public void unregisterObserver(T observer) {
        mObservers.remove(observer);
    }

    protected List<T> getObservers() {
        return mObservers;
    }
}
