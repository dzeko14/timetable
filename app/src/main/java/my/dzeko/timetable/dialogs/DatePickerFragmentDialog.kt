package my.dzeko.timetable.dialogs


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import my.dzeko.timetable.utils.DateUtils

class DatePickerFragmentDialog() : DialogFragment() {
    private var dateSetListener : DatePickerDialog.OnDateSetListener = DatePickerDialog
            .OnDateSetListener { _, _, _, _ -> }

    private var mYear = DateUtils.getCurrentYear()
    private var mMonth = DateUtils.getCurrentMonth()
    private var mDay = DateUtils.getCurrentDay()

    var listener : DatePickerDialog.OnDateSetListener
    get() = dateSetListener
    set(value) { dateSetListener = value }

    fun updateDayAndMonth(day :Int, month :Int){
        mDay = day
        mMonth = month
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = DatePickerDialog(activity, dateSetListener, mYear, mMonth, mDay)
        dialog.datePicker.maxDate = DateUtils.getMaxDateInCurrentYear()
        dialog.datePicker.minDate = DateUtils.getMinDateInCurrentYear()

        return dialog
    }
}