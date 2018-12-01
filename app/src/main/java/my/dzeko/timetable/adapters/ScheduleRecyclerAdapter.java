package my.dzeko.timetable.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Schedule;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.utils.DateUtils;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.DayViewHolder> {
    private final int CURRENT_DATE;
    private List<Day> mSchedule;

    private boolean mIsSingleWeek;

    private int mCurrentDayViewHolderPosition = -1;

    public ScheduleRecyclerAdapter(Schedule schedule) {
        CURRENT_DATE = DateUtils.getCurrentDay();
        mSchedule = schedule.getSchedule();

        mIsSingleWeek = schedule.isSingleWeek();

        findCurrentDayViewHolderPosition();
    }

    private void findCurrentDayViewHolderPosition() {
        int position = 0;
        for (Day day : mSchedule) {
            if (day.getDayOfMonth() == CURRENT_DATE) {
                mCurrentDayViewHolderPosition = position;
                break;
            }
            position++;
        }
    }

    @Override
    @NonNull
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                            int viewType) {
        Context context = parent.getContext();
        int scheduleLayoutID = R.layout.schedule_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(scheduleLayoutID,parent,false);
        return new DayViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return mSchedule.size();
    }

    public int getCurrentDayViewHolderPosition() {
        return mCurrentDayViewHolderPosition != -1 ? mCurrentDayViewHolderPosition : 0;
    }

    public void updateSchedule(Schedule schedule) {
        if(schedule == null) return;
        mSchedule = schedule.getSchedule();
        mIsSingleWeek = schedule.isSingleWeek();
        findCurrentDayViewHolderPosition();
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder class for Schedule RecyclerView Adapter
     */
     class DayViewHolder extends RecyclerView.ViewHolder {

        boolean mIsHeadCurrentDayColor = false;

        int mNotCurrentDayColor;
        int mCurrentDayColor;

        TextView mWeekTextView;

        CardView mHeadCardView;
        TextView mDayTextView;
        TextView mDateTextView;

        CardView[] mBodyCardViews;

        TextView[] mNumberTextViews;

        // Subject Text Views
        TextView[] mSubjectTextViews;

        // Cabinet Text Views
        TextView[] mCabinetTextViews;

        //Teacher Text Views
        TextView[] mTeacherTextViews;

        DayViewHolder(View itemView) {

            super(itemView);

            mWeekTextView = itemView.findViewById(R.id.tv_week);

            mHeadCardView = itemView.findViewById(R.id.head_cardview);

            mDayTextView = itemView.findViewById(R.id.tv_day);
            mDateTextView = itemView.findViewById(R.id.tv_date);

            mBodyCardViews = new CardView[5];
            mBodyCardViews[0] = itemView.findViewById(R.id.body_cardview_1);
            mBodyCardViews[1] = itemView.findViewById(R.id.body_cardview_2);
            mBodyCardViews[2] = itemView.findViewById(R.id.body_cardview_3);
            mBodyCardViews[3] = itemView.findViewById(R.id.body_cardview_4);
            mBodyCardViews[4] = itemView.findViewById(R.id.body_cardview_5);

            mNumberTextViews = new TextView[5];
            mSubjectTextViews = new TextView[5];
            mCabinetTextViews = new TextView[5];
            mTeacherTextViews = new TextView[5];

            int bodyCardViewLength = mBodyCardViews.length;
            for (int i = 0; i < bodyCardViewLength; i++) {
                mNumberTextViews[i] = mBodyCardViews[i].findViewById(R.id.tv_number);
                mSubjectTextViews[i] = mBodyCardViews[i].findViewById(R.id.tv_subject);
                mCabinetTextViews[i] = mBodyCardViews[i].findViewById(R.id.tv_cabinet);
                mTeacherTextViews[i] = mBodyCardViews[i].findViewById(R.id.tv_teacher);
            }

            Resources res = itemView.getResources();
            mNotCurrentDayColor = res.getColor(R.color.colorNotCurrentDay);
            mCurrentDayColor = res.getColor(R.color.colorCurrentDay);
        }

         void update(final int position) {
            final Day day = mSchedule.get(position);

            if(day == null) return;

            if(day.isFirstDayInTheWeek() && !mIsSingleWeek) {
                mWeekTextView.setText(day.getWeek());
                mWeekTextView.setVisibility(View.VISIBLE);
            }
            else {
                mWeekTextView.setVisibility(View.GONE);
            }

            mDateTextView.setText(day.getDate());
            mDayTextView.setText(day.getName());

            List<Subject> subjects = day.getSubjects();
            int subjectAmount = subjects.size();

            for (int i = 0; i < subjectAmount; i++) {
                mBodyCardViews[i].setVisibility(View.VISIBLE);
                mNumberTextViews[i].setText(String.valueOf(subjects.get(i).getPosition()));
                mSubjectTextViews[i].setText(subjects.get(i).getSubjectName());
                String cabinetText = (subjects.get(i).getCabinet() + " " + isNullString(subjects.get(i).getType()))
                        .trim();
                mCabinetTextViews[i].setText(cabinetText);
                mTeacherTextViews[i].setText(subjects.get(i).getTeacher());
            }

            for (int i = subjectAmount; i < 5; i++) {
                mBodyCardViews[i].setVisibility(View.GONE);
            }

            if(CURRENT_DATE == getDate() && !mIsHeadCurrentDayColor) {
                mHeadCardView.setCardBackgroundColor(mCurrentDayColor);
            } else {
                mHeadCardView.setCardBackgroundColor(mNotCurrentDayColor);
            }
        }

        private String isNullString(String str){
            if (str == null) return "";
            return str;
        }

        private int getDate() {
            String dateString = mDateTextView.getText().toString();
            if(dateString.length() < 2) return -1;

            return Integer.parseInt(dateString.substring(0,2));
        }
    }
}
