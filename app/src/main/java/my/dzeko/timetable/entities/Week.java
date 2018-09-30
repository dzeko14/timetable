package my.dzeko.timetable.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Week implements Parcelable {
    private List<Day> mDaysList;
    private int mId;

    public Week(List<Day> mDaysList, int mId) {
        this.mDaysList = mDaysList;
        this.mId = mId;
    }

    protected Week(Parcel in) {
        mDaysList = in.createTypedArrayList(Day.CREATOR);
        mId = in.readInt();
    }

    public static final Creator<Week> CREATOR = new Creator<Week>() {
        @Override
        public Week createFromParcel(Parcel in) {
            return new Week(in);
        }

        @Override
        public Week[] newArray(int size) {
            return new Week[size];
        }
    };

    public List<Day> getDaysList() {
        return mDaysList;
    }

    public int getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mDaysList);
        dest.writeInt(mId);
    }
}
