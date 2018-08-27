package my.dzeko.timetable.entities;

import com.google.gson.annotations.SerializedName;

public class WeekApiRespond {
    @SerializedName("data")
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
