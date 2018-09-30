package my.dzeko.timetable.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.entities.Group;

public class RemoveScheduleAdapter extends RecyclerView.Adapter<RemoveScheduleAdapter.ViewHolder> {
    private OnRemoveClickListener mRemoveClickListener;
    private List<Group> mGroups;

    public RemoveScheduleAdapter(List<Group> groups, OnRemoveClickListener mRemoveClickListener) {
        this.mRemoveClickListener = mRemoveClickListener;
        mGroups = groups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int itemLayout = R.layout.remove_schedule_item;
        View view = inflater.inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mGroupTextView.setText(mGroups.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mGroupTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mGroupTextView = itemView.findViewById(R.id.remove_schedule_text_view);
            Button button = itemView.findViewById(R.id.remove_schedule_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String groupName = mGroupTextView.getText().toString();
                    mRemoveClickListener.onRemoveScheduleClick(groupName);
                    mGroups.remove(new Group(groupName));
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface OnRemoveClickListener{
        void onRemoveScheduleClick(String groupName);
    }
}
