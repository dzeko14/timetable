package my.dzeko.timetable.utils;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

// TODO: Create save RecyclerView's state mechanism
public abstract class AdapterUtils {
    private static final String ADAPTER_STATE = "adapter_state";

    public static void saveState(RecyclerView rv) {
        RecyclerView.LayoutManager manager = rv.getLayoutManager();
        Parcel parcel = Parcel.obtain();
        Parcelable state = manager.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ADAPTER_STATE, state);
        parcel.writeBundle(bundle);
        parcel.recycle();
    }

    public static void restoreState(RecyclerView rv) {
        Parcel parcel = Parcel.obtain();
        //parcel.setDataPosition(0);
        Bundle bundle = parcel.readBundle(Bundle.class.getClassLoader());
        if(bundle == null) {
            parcel.recycle();
            return;
        }
        Parcelable state = bundle.getParcelable(ADAPTER_STATE);
        rv.getLayoutManager().onRestoreInstanceState(state);
        parcel.recycle();
    }
}
