package my.dzeko.timetable.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import my.dzeko.timetable.R
import my.dzeko.timetable.entities.Day
import my.dzeko.timetable.entities.Schedule
import my.dzeko.timetable.entities.Subject
import my.dzeko.timetable.entities.Week
import java.lang.IllegalStateException
import java.lang.RuntimeException

private const val SUBJECT_VIEW_TYPE = 0
private const val WEEK_CONST_TYPE = 1

class SelectSubjectRecyclerViewAdapter(private var mSchedule: Schedule)
    : RecyclerView.Adapter<AbstractViewHolder>() {

    private var mSubjectWeekDayList :MutableList<SubjectWeekDay>? = null

    private var mFirstWeekLabelPosition :Int? = null
    private var mSecondWeekLabelPosition :Int? = null

    init {
        updateSchedule()
    }

    private fun updateSchedule() {
        mSubjectWeekDayList = wrapItems(mSchedule)

        if (mSubjectWeekDayList == null) return

        if(!mSchedule.isSingleWeek){
            mFirstWeekLabelPosition = 0
            mSubjectWeekDayList!!.add(0, SubjectWeekDay(-1,"", "", mSubjectWeekDayList!![0].week))
            mSubjectWeekDayList!!.add(mSecondWeekLabelPosition!!, SubjectWeekDay(-1,"", "", mSubjectWeekDayList!![mSecondWeekLabelPosition!!].week))
        } else {
            mFirstWeekLabelPosition = null
            mSecondWeekLabelPosition = null
        }
    }

    private fun wrapItems(schedule: Schedule) :MutableList<SubjectWeekDay>? {
            val list = mutableListOf<SubjectWeekDay>()

        var isSecondWeekIdFound = false
        var secondWeekId = 0

        for (day in schedule.schedule){
            for (subject in day.subjects){
                list.add(SubjectWeekDay(
                        subject = subject.subjectName,
                        week = day.week,
                        day = day.name, id = subject.id

                ))
                if(!isSecondWeekIdFound) {
                    if (subject.weekId == 2){
                        mSecondWeekLabelPosition = secondWeekId
                        isSecondWeekIdFound = true
                    }
                    secondWeekId++
                }
            }
        }
        return list
    }

    fun setData(schedule: Schedule){
        mSchedule = schedule
        updateSchedule()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            SUBJECT_VIEW_TYPE -> SubjectViewHolder(inflater.inflate(R.layout.select_subject,
                    parent, false))

            WEEK_CONST_TYPE -> WeekViewHolder(
                    inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            )

            else -> throw IllegalStateException("Can't create view in SelectSubjectRecyclerViewAdapter! Incorrect layout!")
        }
    }

    override fun getItemCount(): Int {
        return mSubjectWeekDayList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        mSubjectWeekDayList?.let {
            holder.update(it[position])
        }

    }

    override fun getItemViewType(position: Int): Int {
         return when {
             (position == mFirstWeekLabelPosition || position == mSecondWeekLabelPosition)
                -> WEEK_CONST_TYPE

             else -> SUBJECT_VIEW_TYPE
         }
    }
}

abstract class AbstractViewHolder(view :View) : RecyclerView.ViewHolder(view){

    abstract fun update(item :SubjectWeekDay)
}

class SubjectViewHolder(view: View) : AbstractViewHolder(view){
    private val mSubjectNameTextView = view.findViewById<TextView>(R.id.select_subject_name)
    private val mDayNameTextView = view.findViewById<TextView>(R.id.select_subject_day_name)


    override fun update(item :SubjectWeekDay) {
        mSubjectNameTextView.text = item.subject
        mDayNameTextView.text = item.day
    }
}

class WeekViewHolder(view: View) : AbstractViewHolder(view){
    private var mWeekNameTextView = view.findViewById<TextView>(android.R.id.text1)

    override fun update(item: SubjectWeekDay) {
        mWeekNameTextView.text = item.week
    }
}

data class SubjectWeekDay(val id :Long, val subject :String, val day :String, val week :String)