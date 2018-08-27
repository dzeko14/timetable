package my.dzeko.timetable.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CalendarDayTextView extends AppCompatTextView {
    public CalendarDayTextView(Context context) {
        super(context);
    }

    public CalendarDayTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarDayTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
