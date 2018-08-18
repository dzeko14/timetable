package my.dzeko.timetable.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiRespond {
    @SerializedName("data")
    private List<Subject> data;

    public List<Subject> getData() {
        return data;
    }

    public void setData(List<Subject> data) {
        this.data = data;
    }
}
