package my.dzeko.timetable.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import my.dzeko.timetable.R;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Subject;
import my.dzeko.timetable.entities.Week;
import my.dzeko.timetable.utils.WeekUtils;

public class EditWeekExpandableListAdapter extends BaseExpandableListAdapter {
    private Week mWeek;
    private LayoutInflater mLayoutInflater;

    private OnRemoveExpandableListViewChildItemListener mRemoveChildItemListener;
    private OnEditExpandableListViewChildItemListener mEditChildItemListener;
    private OnAddExpandableListViewGroupItemListener mAddGroupItemListener;

    private final static int GROUP_ITEMS_COUNT = 6;

    public EditWeekExpandableListAdapter(Context context, Week week) {
        mWeek = WeekUtils.createWeek(context, week.getId());

        for (Day day : week.getDaysList()) {
            mWeek.getDaysList().set(day.getId() - 1, day);
        }

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRemoveChildItemListener(OnRemoveExpandableListViewChildItemListener removeChildItemListener) {
        this.mRemoveChildItemListener = removeChildItemListener;
    }

    public void setEditChildItemListener(OnEditExpandableListViewChildItemListener editChildItemListener) {
        this.mEditChildItemListener = editChildItemListener;
    }

    public void setAddGroupItemListener(OnAddExpandableListViewGroupItemListener onAddGroupItemListener) {
        this.mAddGroupItemListener = onAddGroupItemListener;
    }

    @Override
    public int getGroupCount() {
        return GROUP_ITEMS_COUNT;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mWeek.getDaysList().get(groupPosition).getSubjects().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mWeek.getDaysList().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mWeek.getDaysList().get(groupPosition).getSubjects().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mWeek.getDaysList().get(groupPosition).getSubjects().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.editing_expandable_list_group_item, parent,
                    false);
        }

        TextView dayNameTV = view.findViewById(R.id.editing_expandable_list_group_item_day_name_text_view);
        ImageView addButton = view.findViewById(R.id.editing_expandable_list_group_item_add_button);

        final Day day = mWeek.getDaysList().get(groupPosition);
        String dayName = day.getName();
        dayNameTV.setText(dayName);

        if (day.getSubjects().size() == 5) {
            addButton.setEnabled(false);
        } else {
            addButton.setEnabled(true);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAddGroupItemListener.onAddGroupItemClick(day);
                }
            });
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.editing_expandable_list_child_item,
                    parent, false);
        }

        TextView subjectNumberTV = view.findViewById(R.id.editing_expandable_list_child_item_subject_number_text_view);
        TextView subjectNameTV = view.findViewById(R.id.editing_expandable_list_child_item_subject_name_text_view);
        ImageView editButton = view.findViewById(R.id.editing_expandable_list_child_item_edit_button);
        ImageView removeButton = view.findViewById(R.id.editing_expandable_list_child_item_delete_button);

        final Subject subject = mWeek.getDaysList().get(groupPosition).getSubjects().get(childPosition);

        subjectNameTV.setText(subject.getSubjectName());
        subjectNumberTV.setText(String.valueOf(subject.getPosition()));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditChildItemListener.onEditChildItemClick(subject);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRemovingAnimation((View) v.getParent(), subject);
            }
        });

        return view;
    }

    private void performRemovingAnimation(final View view, final Subject subject){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.removing_item);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRemoveChildItemListener.onRemoveChildItemClick(subject);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void updateData(@NonNull Week newWeek){
        WeekUtils.updateWeek(mWeek, newWeek);
        notifyDataSetChanged();
    }

    //Listeners
    public interface OnRemoveExpandableListViewChildItemListener {
        void onRemoveChildItemClick(Subject subject);
    }

    public interface OnEditExpandableListViewChildItemListener {
        void onEditChildItemClick(Subject subject);
    }

    public interface OnAddExpandableListViewGroupItemListener{
        void onAddGroupItemClick(Day day);
    }
}
