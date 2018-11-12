package my.dzeko.timetable.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import my.dzeko.timetable.R
import my.dzeko.timetable.adapters.SelectSubjectRecyclerViewAdapter
import my.dzeko.timetable.contracts.SelectSubjectContract
import my.dzeko.timetable.entities.Schedule
import my.dzeko.timetable.entities.Subject
import my.dzeko.timetable.presenters.SelectSubjectPresenter

private const val SUBJECT_ID = "subject_id"

class SelectSubjectFragment : Fragment(), SelectSubjectContract.View {
    private val mPresenter :SelectSubjectContract.Presenter = SelectSubjectPresenter(this)

    private val mAdapter = SelectSubjectRecyclerViewAdapter(Schedule.getEmptySchedule())
    private var mRecyclerView :RecyclerView? = null

    private var mSubjectId :Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mSubjectId = it.getInt(SUBJECT_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_select_subject, container, false)
        findViews(rootView)
        setupRecyclerView(inflater.context)
        mPresenter.onViewCreated()
        return rootView
    }

    private fun setupRecyclerView(context: Context) {
        mRecyclerView?.adapter = mAdapter
        mRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun findViews(rootView: View) {
        mRecyclerView = rootView.findViewById(R.id.select_subject_recycler_view)
    }

    override fun setSchedule(schedule: Schedule) {
        mAdapter.setData(schedule)
    }

    override fun showLoading() {
        TODO("not implemented")
    }

    override fun hideLoading() {
        TODO("not implemented")
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.destroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(subjectId: Int) =
                SelectSubjectFragment().apply {
                    arguments = Bundle().apply {
                        putInt(SUBJECT_ID, subjectId)
                    }
                }
    }
}
