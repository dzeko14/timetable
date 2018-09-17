package my.dzeko.timetable.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.entities.Day;
import my.dzeko.timetable.entities.Subject;

public class EditWeekExpandableListAdapter extends BaseExpandableListAdapter {
    private List<Day> mWeek;
    private LayoutInflater mLayoutInflater;

    private OnRemoveExpandableListViewChildItemListener mRemoveChildItemListener;
    private OnEditExpandableListViewChildItemListener mEditChildItemListener;
    private OnRemoveExpandableListViewGroupItemListener mRemoveGroupItemListener;

    public EditWeekExpandableListAdapter(Context context, List<Day> week) {
        mWeek = week;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRemoveChildItemListener(OnRemoveExpandableListViewChildItemListener removeChildItemListener) {
        this.mRemoveChildItemListener = removeChildItemListener;
    }

    public void setEditChildItemListener(OnEditExpandableListViewChildItemListener editChildItemListener) {
        this.mEditChildItemListener = editChildItemListener;
    }

    public void setRemoveGroupItemListener(OnRemoveExpandableListViewGroupItemListener removeGroupItemListener) {
        this.mRemoveGroupItemListener = removeGroupItemListener;
    }

    @Override
    public int getGroupCount() {
        return mWeek.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mWeek.get(groupPosition).getSubjects().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mWeek.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mWeek.get(groupPosition).getSubjects().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mWeek.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mWeek.get(groupPosition).getSubjects().get(childPosition).getId();
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
        final Button removeButton = view.findViewById(R.id.editing_expandable_list_group_item_remove_button);

        final Day day = mWeek.get(groupPosition);
        String dayName = day.getName();
        dayNameTV.setText(dayName);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemoveGroupItemListener.onRemoveGroupItemClick(day);
            }
        });

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
        Button editButton = view.findViewById(R.id.editing_expandable_list_child_item_edit_button);
        Button removeButton = view.findViewById(R.id.editing_expandable_list_child_item_delete_button);

        final Subject subject = mWeek.get(groupPosition).getSubjects().get(childPosition);

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
                mRemoveChildItemListener.onRemoveChildItemClick(subject);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void updateData(List<Day> week){
        mWeek = week;
        notifyDataSetChanged();
    }

    //Listeners
    public interface OnRemoveExpandableListViewChildItemListener {
        void onRemoveChildItemClick(Subject subject);
    }

    public interface OnRemoveExpandableListViewGroupItemListener {
        void onRemoveGroupItemClick(Day day);
    }

    public interface OnEditExpandableListViewChildItemListener {
        void onEditChildItemClick(Subject subject);
    }
}
