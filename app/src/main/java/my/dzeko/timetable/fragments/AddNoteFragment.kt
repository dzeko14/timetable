package my.dzeko.timetable.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.AddNoteContract

class AddNoteFragment : Fragment(), AddNoteContract.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun showLoading() {
        TODO("not implemented")
    }

    override fun hideLoading() {
        TODO("not implemented")
    }

    override fun onSelectedSchedu() {
        TODO("not implemented")
    }
}
