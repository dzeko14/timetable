package my.dzeko.timetable.presenters

import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.AddNoteFragmentContract
import my.dzeko.timetable.entities.Subject

class AddNoteFragmentPresenter(private var mView :AddNoteFragmentContract.View?) : AddNoteFragmentContract.Presenter {
    private var mDayAndMonth = Pair(0,0)

    override fun onUserClick(itemId: Int): Boolean {
        TODO("not implemented")
    }

    override fun destroy() {
        mView = null
    }

    override fun onDateSelected(day: Int, month: Int) {
        mDayAndMonth = day to month
    }

    override fun onSubjectSelected(subject: Subject) {
        TODO("not implemented")
    }

    override fun onButtonClicked(id: Int) {
        when(id) {
            R.id.add_note_date_button -> mView?.showDatePickerDialog()
            R.id.add_note_schedule_button -> mView?.showSchedulePickerDialog()
        }
    }
}