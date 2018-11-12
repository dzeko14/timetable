package my.dzeko.timetable.contracts

import android.app.DatePickerDialog
import my.dzeko.timetable.entities.Subject
import my.dzeko.timetable.interfaces.IPresenter
import my.dzeko.timetable.interfaces.IView
import java.util.*

abstract class AddNoteFragmentContract {
    interface View : IView, DatePickerDialog.OnDateSetListener {
        fun showDatePickerDialog()
        fun showSchedulePickerDialog()
    }

    interface Presenter : IPresenter {
        fun onDateSelected(day :Int, month :Int)
        fun onSubjectSelected(subject :Subject)
        fun onButtonClicked(id :Int)
    }
}