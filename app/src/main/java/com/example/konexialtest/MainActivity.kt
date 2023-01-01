package com.example.konexialtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konexialtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter: StatusListAdapter? = null
    private val dialog = AddStatusDialog { newStatus, startTime ->
        addStatus(
            newStatus,
            startTime,
        )
    }

    private var statusList = arrayListOf(
        StatusChange(Status.SLEEPER, Date(0)),
        StatusChange(Status.ON_DUTY, Date(9)),
        StatusChange(Status.DRIVING, Date(11)),
        StatusChange(Status.OFF_DUTY, Date(18))
    )
    private val mapStatus = mapOf(
        0 to Status.DRIVING,
        1 to Status.ON_DUTY,
        2 to Status.OFF_DUTY,
        3 to Status.SLEEPER
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setButtonsActions()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        adapter = StatusListAdapter(statusList)
        binding.listStatus.adapter = adapter
    }


    private fun setButtonsActions() {
        binding.editButton.setOnClickListener {
            dialog.show(this.supportFragmentManager, "AddStatusDialog")
        }
    }

    private fun addStatus(newStatusIndex: Int, startTimeIndex: Int) {
        val status = mapStatus[newStatusIndex]

        status?.let { newStatus ->
            edit(
                newStatus,
                Date(startTimeIndex),
            )
        }
    }

    private fun edit(
        newStatus: Status,
        startTime: Date
    ): List<StatusChange> {
        val newStatusChange = StatusChange(newStatus, startTime)
        val newStatusList: ArrayList<StatusChange> = arrayListOf()
        newStatusList.addAll(statusList)
        newStatusList.add(newStatusChange)
        newStatusList.sortBy { hour -> hour.time.hour }

        val isListValid = checkIfListIsValid(newStatusList)
        val isStatusValid = checkIfNewStatusTimeIsAlreadyTaken(newStatusChange)

        if (isListValid && isStatusValid) {
            statusList = arrayListOf()
            statusList.addAll(newStatusList)
        }

        updateList(statusList)
        return statusList
    }

    private fun checkIfNewStatusTimeIsAlreadyTaken(newStatus: StatusChange): Boolean {
        statusList.forEach { StatusChange ->
            if (StatusChange.time.hour == newStatus.time.hour) {
                Toast.makeText(this, "Status hour is already taken", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun checkIfListIsValid(newStatusList: List<StatusChange>): Boolean {
        var result = true
        var index = START_INDEX
        while (index < newStatusList.size) {
            val element = newStatusList[index]

            val previousIndex = index - 1

            if (previousIndex > -1) {
                val previousElement = newStatusList[previousIndex]
                if (element.status == previousElement.status) {
                    Toast.makeText(this, "Invalid new Status", Toast.LENGTH_SHORT).show()
                    result = false
                }
            }
            val nextIndex = index + 1

            if (nextIndex < newStatusList.size) {
                val nextElement = newStatusList[nextIndex]
                if (element.status == nextElement.status) {
                    Toast.makeText(this, "Invalid new Status", Toast.LENGTH_SHORT).show()
                    result = false
                }
            }
            index++
        }
        return result
    }

    private fun updateList(newList: List<StatusChange>) {
        binding.listStatus.adapter = StatusListAdapter(newList)
    }

    companion object {
        const val START_INDEX = 0
    }
}

data class StatusChange(
    val status: Status,
    val time: Date
)

data class Date(
    val hour: Int
)

enum class Status {
    DRIVING,
    ON_DUTY,
    OFF_DUTY,
    SLEEPER
}