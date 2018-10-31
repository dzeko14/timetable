package my.dzeko.timetable.contracts

import my.dzeko.timetable.interfaces.IPresenter
import my.dzeko.timetable.interfaces.IView

abstract class AddNoteContract {
    interface View : IView{
        fun onSelectedSchedu()
    }

    interface Presenter : IPresenter {

    }
}