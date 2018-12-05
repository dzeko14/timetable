package my.dzeko.timetable.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import my.dzeko.timetable.R;
import my.dzeko.timetable.entities.Group;

public class RemoveScheduleAdapter extends RecyclerView.Adapter<RemoveScheduleAdapter.ViewHolder> {
    private List<Group> mGroups;

    public RemoveScheduleAdapter(List<Group> groups) {
        mGroups = groups;
        setHasStableIds(true);
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public String getGroupName(long position){
        return mGroups.get((int) position).getName();
    }

    public void removeItemById(long itemId) {
        mGroups.remove((int) itemId);
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mGroupTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mGroupTextView = itemView.findViewById(R.id.remove_schedule_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(
                            AnimationUtils.loadAnimation(v.getContext(), R.anim.partial_moving)
                    );
                }
            });
        }
    }

    public interface OnRemoveClickListener{
        void onRemoveScheduleClick(String groupName);
    }
}
