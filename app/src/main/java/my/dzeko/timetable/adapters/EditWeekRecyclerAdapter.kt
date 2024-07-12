package my.dzeko.timetable.adapters

import android.content.Context
import android.os.Build
import android.support.transition.TransitionManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import my.dzeko.timetable.R
import my.dzeko.timetable.databinding.EditingExpandableListChildItemBinding
import my.dzeko.timetable.databinding.EditingExpandableListGroupBinding
import my.dzeko.timetable.entities.Day
import my.dzeko.timetable.entities.Subject
import my.dzeko.timetable.entities.Week
import my.dzeko.timetable.utils.WeekUtils

private const val PARENT_ITEMS_COUNT = 6

class EditWeekRecyclerAdapter(context: Context, private var mWeek: Week) :
    RecyclerView.Adapter<EditWeekRecyclerAdapter.EditWeekViewHolder>() {

    private val mViewVisibilityArray =
        arrayOf(View.GONE, View.GONE, View.GONE, View.GONE, View.GONE, View.GONE)

    private var mGroupAddListener: OnAddExpandableListViewGroupItemListener? = null
    private var mChildEditListener: OnEditExpandableListViewChildItemListener? = null
    private var mChildRemoveListener: OnRemoveExpandableListViewChildItemListener? = null

    var groupAddListener: OnAddExpandableListViewGroupItemListener?
        get() = mGroupAddListener
        set(value) {
            mGroupAddListener = value
        }

    var childEditListener: OnEditExpandableListViewChildItemListener?
        get() = mChildEditListener
        set(value) {
            mChildEditListener = value
        }

    var childRemoveListener: OnRemoveExpandableListViewChildItemListener?
        get() = mChildRemoveListener
        set(value) {
            mChildRemoveListener = value
        }

    init {
        val emptyWeek = WeekUtils.createWeek(context, mWeek.id)
        for (day in mWeek.daysList) {
            emptyWeek.daysList[day.id - 1] = day
        }
        mWeek = emptyWeek
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditWeekViewHolder {
        return EditWeekViewHolder(
            EditingExpandableListGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return PARENT_ITEMS_COUNT
    }

    fun updateData(week: Week) {
        WeekUtils.updateWeek(mWeek, week)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EditWeekViewHolder, position: Int) {
        val day = mWeek.daysList[position]
        holder.name.text = day.name
        val childViewsAmount = holder.childViewGroup.childCount
        val subjectCount = day.subjectAmount

        if (subjectCount < childViewsAmount) {
            for (i in subjectCount until childViewsAmount) {
                holder.childViewGroup.getChildAt(i).visibility = View.GONE
            }
        }

        holder.childViewGroup.visibility = mViewVisibilityArray[position]

        if (day.subjects.size >= 5) {
            holder.addGroupButton.isEnabled = false
        } else {
            holder.addGroupButton.isEnabled = true
            holder.addGroupButton.setOnClickListener {
                mGroupAddListener?.onAddGroupItemClick(mWeek.daysList[position])
            }
        }

        for (i in 0 until subjectCount) {
            holder.childViewGroup.getChildAt(i).apply {
                val childViewBinding = EditingExpandableListChildItemBinding.bind(this)

                visibility = View.VISIBLE

                childViewBinding.editingExpandableListChildItemDeleteButton.setOnClickListener {
                    val view = it.parent as View
                    val animation = AnimationUtils.loadAnimation(view.context, R.anim.removing_item)
                    animation.setAnimationListener(object : MyAnimationListener() {
                        override fun onAnimationEnd(animation: Animation?) {
                            view.visibility = View.INVISIBLE
                            mChildRemoveListener?.onRemoveChildItemClick(day.subjects[i])
                        }
                    })
                    view.startAnimation(animation)
                }

                childViewBinding.editingExpandableListChildItemEditButton.setOnClickListener {
                    mChildEditListener?.onEditChildItemClick(day.subjects[i])
                }

                childViewBinding.editingExpandableListChildItemSubjectNameTextView.apply {
                    text = day.subjects[i].subjectName
                }

                childViewBinding.editingExpandableListChildItemSubjectNumberTextView.apply {
                    text = day.subjects[i].position.toString()
                }
            }
        }
    }

    private abstract class MyAnimationListener : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
            //Empty
        }

        override fun onAnimationStart(animation: Animation?) {
            //Empty
        }
    }

    inner class EditWeekViewHolder(
        viewBinding: EditingExpandableListGroupBinding
    ) : RecyclerView.ViewHolder(viewBinding.root), View.OnClickListener {
        val name: TextView = viewBinding.editingExpandableListGroupItemDayNameTextView
        val childViewGroup: ViewGroup = viewBinding.childItems
        val addGroupButton: ImageView = viewBinding.editingExpandableListGroupItemAddButton

        init {
            val inflater = LayoutInflater.from(viewBinding.root.context)
            for (i in 0 until 5) {
                childViewGroup.addView(
                    inflater.inflate(
                        R.layout.editing_expandable_list_child_item,
                        childViewGroup,
                        false
                    )
                )
            }

            (viewBinding.root as ViewGroup).getChildAt(0).setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.let {
                if (v.id == R.id.group_items) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(v.rootView as ViewGroup)
                    }
                    if (childViewGroup.visibility == View.GONE) {
                        mViewVisibilityArray[layoutPosition] = View.VISIBLE
                        childViewGroup.visibility = View.VISIBLE
                    } else {
                        mViewVisibilityArray[layoutPosition] = View.GONE
                        childViewGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    interface OnAddExpandableListViewGroupItemListener {
        fun onAddGroupItemClick(day: Day)
    }

    interface OnRemoveExpandableListViewChildItemListener {
        fun onRemoveChildItemClick(subject: Subject)
    }

    interface OnEditExpandableListViewChildItemListener {
        fun onEditChildItemClick(subject: Subject)
    }
}

