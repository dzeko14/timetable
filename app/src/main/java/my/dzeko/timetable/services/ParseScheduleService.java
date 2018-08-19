package my.dzeko.timetable.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import my.dzeko.timetable.interfaces.IModel;
import my.dzeko.timetable.models.MockModel;

public class ParseScheduleService extends Service {
    public static final String PARSING_GROUP_NAME = "group_name";

    public ParseScheduleService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String groupName = intent.getStringExtra(PARSING_GROUP_NAME);
        IModel model = MockModel.getInstance();

        model.parseSchedule(groupName);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
