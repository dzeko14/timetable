package my.dzeko.timetable.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleApiRespond {
    @SerializedName("data")
    private List<Subject> data;

    public List<Subject> getData() {
        return data;
    }

    public void setData(List<Subject> data) {
        this.data = data;
    }
}
