package com.example.getitnow.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getitnow.data.Task
import com.example.getitnow.databinding.ItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox

class TaskAdapter(private val listener: TaskUpdatedListener) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var tasks: List<Task> = listOf()

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTaskBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks.sortedBy {
            it.isComplete
         }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                checkBox.isChecked = task.isComplete
                toggleStar.isChecked = task.isStarred
                if (task.isComplete) {
                    textViewTitle.paintFlags = textViewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textViewDetails.paintFlags = textViewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    textViewTitle.paintFlags = 0
                    textViewDetails.paintFlags = 0
                }
                textViewTitle.text = task.title
                textViewDetails.text = task.description
                checkBox.setOnClickListener {
                    val updateTask = task.copy(isComplete = checkBox.isChecked)
                    listener.onTaskUpdated(updateTask)
                }
                toggleStar.setOnClickListener {
                    val updateTask = task.copy(isStarred = toggleStar.isChecked)
                    listener.onTaskUpdated(updateTask)
            }

            }

        }
    }

    interface TaskUpdatedListener {

        fun onTaskUpdated(task: Task)
    }
}
