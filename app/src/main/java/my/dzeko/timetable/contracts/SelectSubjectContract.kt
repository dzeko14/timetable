package my.dzeko.timetable.contracts

import my.dzeko.timetable.entities.Schedule
import my.dzeko.timetable.entities.Subject
import my.dzeko.timetable.interfaces.IPresenter
import my.dzeko.timetable.interfaces.IView

class SelectSubjectContract {
    interface View : IView {
        fun setSchedule(schedule :Schedule)
    }

    interface Presenter : IPresenter {
        fun onViewCreated()
    }
}