package com.example.getitnow.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.getitnow.data.GetItNowDatabase
import com.example.getitnow.data.Task
import com.example.getitnow.data.TaskDao
import com.example.getitnow.databinding.ActivityMainBinding
import com.example.getitnow.databinding.DialogAddTaskBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.concurrent.thread
import com.example.getitnow.R
import com.example.getitnow.ui.tasks.TasksFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val database: GetItNowDatabase by lazy { GetItNowDatabase.getDatabase(this) }
    private val taskDao: TaskDao by lazy { database.getTaskDao() }
    private val tasksFragment: TasksFragment = TasksFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            pager.adapter = PagerAdapter(this@MainActivity)

            TabLayoutMediator(tabs, pager) { tab, _->
                tab.text = "Tasks"
            }.attach()
          fab.setOnClickListener { showAddTaskDialog() }
            setContentView(root)
        }
    }
    private fun showAddTaskDialog() {
         DialogAddTaskBinding.inflate(layoutInflater).apply {
            val dialog = BottomSheetDialog(this@MainActivity)
            dialog.setContentView(root)

            buttonShowDetails.setOnClickListener {
                editTextTaskDetails.visibility =
                    if (editTextTaskDetails.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            buttonSave.setOnClickListener {
                val task = Task(
                    title = editTextTaskTitle.text.toString(),
                    description = editTextTaskDetails.text.toString()
                )

                thread {
                    taskDao.createTask(task)
                }

                dialog.dismiss()
                tasksFragment.fetchAllTasks()
            }

            dialog.show()

        }

    }

    inner class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount() = 1

        override fun createFragment(position: Int): Fragment {
            return tasksFragment
        }
    }
}
