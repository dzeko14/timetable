package my.dzeko.timetable.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CalendarDayButton extends AppCompatTextView{ // AppCompatButton {
    public CalendarDayButton(Context context) {
        super(context);
    }

    public CalendarDayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarDayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
