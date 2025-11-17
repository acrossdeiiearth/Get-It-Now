package com.example.getitnow.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.getitnow.data.GetItNowDatabase
import com.example.getitnow.data.Task
import com.example.getitnow.data.TaskDao
import com.example.getitnow.databinding.FragmentTasksBinding
import kotlin.concurrent.thread

class TasksFragment : Fragment(), TaskAdapter.TaskUpdatedListener {

    private lateinit var binding: FragmentTasksBinding
    private val taskDao: TaskDao by lazy {
        GetItNowDatabase.getDatabase(requireContext()).getTaskDao()
    }
    private val  adapter = TaskAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        fetchAllTasks()
    }
    fun fetchAllTasks(){
        thread {
            val tasks = taskDao.getAllTasks()
            requireActivity().runOnUiThread {
              adapter.setTasks(tasks)
            }
        }
    }

    override fun onTaskUpdated(task: Task) {
      thread {
          taskDao.updateTask(task)
          fetchAllTasks()
      }
    }

}
