package my.dzeko.timetable.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import kotlinx.android.synthetic.main.fragment_add_note.view.*

import my.dzeko.timetable.R
import my.dzeko.timetable.contracts.AddNoteFragmentContract
import my.dzeko.timetable.dialogs.DatePickerFragmentDialog
import my.dzeko.timetable.presenters.AddNoteFragmentPresenter

private const val SELECT_SCHEDULE_FRAGMENT_TAG = "select_schedule_fragment_tag"

class AddNoteFragment : Fragment(), AddNoteFragmentContract.View {
    private val mPresenter :AddNoteFragmentContract.Presenter = AddNoteFragmentPresenter(this)
    private val mDatePickerDialog = DatePickerFragmentDialog()
    private var mSelectScheduleFragment :View? = null

    private var mDatePickerButton :Button? = null
    private var mSchedulePickerButton :Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatePickerDialog.listener = this
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.destroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)
        findViews(view)
        setupButtons(view)
        setupSelectScheduleFragment(activity?.supportFragmentManager)
        return view
    }

    private fun setupSelectScheduleFragment(supportFragmentManager: FragmentManager?) {
        supportFragmentManager?.
                beginTransaction()?.
                add(    R.id.add_note_select_schedule_fragment,
                        SelectSubjectFragment.newInstance(0),
                        SELECT_SCHEDULE_FRAGMENT_TAG
                )?.
                commit()
    }

    private fun findViews(rootView: View) {
        mDatePickerButton = rootView.findViewById(R.id.add_note_date_button)
        mSelectScheduleFragment = rootView.findViewById(R.id.add_note_select_schedule_fragment)
        mSchedulePickerButton = rootView.add_note_schedule_button
    }

    private fun setupButtons(rootView: View) {
        val handler = { view :View -> mPresenter.onButtonClicked(view.id) }
        mDatePickerButton?.setOnClickListener(handler)
        mSchedulePickerButton?.setOnClickListener(handler)
    }

    fun onButtonClick(v :View){
        mPresenter.onButtonClicked(v.id)
    }

    override fun showDatePickerDialog() {
        mDatePickerDialog.show(activity?.supportFragmentManager, "datePicker")
    }

    override fun showSchedulePickerDialog() {
        mSelectScheduleFragment?.visibility = View.VISIBLE
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateString = "$dayOfMonth.${month + 1}"
        mPresenter.onDateSelected(dayOfMonth, month)
        mDatePickerButton?.text = dateString
        mDatePickerDialog.updateDayAndMonth(dayOfMonth, month)
    }

    override fun showLoading() {
        TODO("not implemented")
    }

    override fun hideLoading() {
        TODO("not implemented")
    }
}
