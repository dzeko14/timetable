package my.dzeko.timetable.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.Model;

public class ParseScheduleService extends IntentService {
    public static final String PARSING_GROUP_NAME = "group_name";

    public ParseScheduleService() {
        super("parsing_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null) return;
        String groupName = intent.getStringExtra(PARSING_GROUP_NAME);
        IModel model = Model.getInstance();
        model.parseSchedule(groupName);
    }
}
