package my.dzeko.timetable.models;

import java.util.ArrayList;
import java.util.List;

import my.dzeko.timetable.interfaces.IModel;

// A mock model
public class Model implements IModel{
//  --------------------------------------------------------
    private static Model mInstance;
    private Model() { }
    public static Model getInstance() {
        if(mInstance == null) {
            mInstance = new Model();
        }
        return mInstance;
    }
//  --------------------------------------------------------

    private Schedule genereteFakeData() {
        List<Day> days = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Day d = new Day(i,1,"Понедылок", "22.05", "Перший тиждень", false);
            days.add(d);
            for (int j = 0; j < 5; j++) {
                d.attachSubject(new Subject(i, 1, "IT", "Test Subject", "Test Subject", "121", "Teacher", j));
            }
        }

        days.get(0).setIsFirstDayInTheWeek(true);

        for (int i = 0; i < 6; i++) {
            Day d = new Day(i,2,"Понедылок", "22.05", "Другий тиждень", false);
            days.add(d);
            for (int j = 0; j < 5; j++) {
                d.attachSubject(new Subject(i, 2, "IT", "Test Subject", "Test Subject", "121", "Teacher", j));
            }
        }
        days.get(6).setIsFirstDayInTheWeek(true);

        return new Schedule("IT", days);
    }

    private List<String> generateGroupList() {
        List<String> groups = new ArrayList<>();

        groups.add("It");
        groups.add("Ip");
        groups.add("Iq");
//        groups.add("Iw");
//        groups.add("Ie");
//        groups.add("Ir");
//        groups.add("Iy");
//        groups.add("Iu");
//        groups.add("Ii");
//        groups.add("Io");
//        groups.add("I[");
//        groups.add("Ia");
//        groups.add("Is");
//        groups.add("Id");
//        groups.add("If");
//        groups.add("Ig");
//        groups.add("Ih");
//        groups.add("Ij");
//        groups.add("Ik");
//        groups.add("Il");
//        groups.add("I;");
//        groups.add("I'");
//        groups.add("Iz");
//        groups.add("Ix");
//        groups.add("Ic");
//        groups.add("Iv");
//        groups.add("Ib");
//        groups.add("In");
//        groups.add("Im");
//        groups.add("I,");
//        groups.add("I.");
//        groups.add("I/");
        return groups;
    }

    @Override
    public List<String> getGroupList() {
        return generateGroupList();
    }

    @Override
    public Schedule getSelectedSchedule() {
        return genereteFakeData();
    }


    @Override
    public void selectSchedule(String groupName) {

    }

    @Override
    public String getSelectedScheduleGroupName() {
        return "It";
    }


}
