package my.dzeko.timetable.models;

import my.dzeko.timetable.interfaces.IModel;

public abstract class AbstractModel {
    public static IModel getModel(){
        return Model.getInstance();
    }
}
