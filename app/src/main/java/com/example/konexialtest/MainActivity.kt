package com.example.konexialtest

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.konexialtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var adapter: StatusListAdapter? = null
    private val dialog = AddStatusDialog { newStatus, startTime, endTime ->
        addStatus(
            newStatus,
            startTime,
            endTime
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
        2 to Status.OFF_DUTY
    )

    private val listOfStatus = arrayListOf(Status.DRIVING, Status.ON_DUTY, Status.OFF_DUTY)

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

    private fun addStatus(newStatusIndex: Int, startTimeIndex: Int, endTimeIndex: Int) {
        val status = mapStatus[newStatusIndex]

        status?.let { newStatus ->
            edit(
                statusList,
                newStatus,
                Date(startTimeIndex),
                Date(endTimeIndex),
            )
        }
    }

    private fun edit(
        day: List<StatusChange>,
        newStatus: Status,
        startTime: Date,
        endTime: Date
    ): List<StatusChange> {
        val newStatusChangeStart = StatusChange(newStatus, startTime)
        val newStatusList: ArrayList<StatusChange> = arrayListOf()

        newStatusList.addAll(statusList)
        newStatusList.add(newStatusChangeStart)
        newStatusList.sortBy { hour -> hour.time.hour }

        val isStatusValid =
            checkIfNewStatusTimeIsAlreadyTaken(startTime.hour, endTime.hour)

        val listWithEndTimeStatus =
            addEndTimeStatusChange(newStatusList, newStatusChangeStart, endTime)

        val isListValid = checkIfListIsValid(listWithEndTimeStatus)


        if (isStatusValid && isListValid) {
            statusList = arrayListOf()
            statusList.addAll(newStatusList)
        }

        updateList(statusList)
        return statusList
    }

    private fun addEndTimeStatusChange(
        newStatusList: ArrayList<StatusChange>,
        startStatusChange: StatusChange,
        endTime: Date
    ): List<StatusChange> {
        val indexOfStartStatus = newStatusList.indexOf(startStatusChange)
        val nextStatusChange = newStatusList[indexOfStartStatus + 1]

        if (nextStatusChange.time.hour == endTime.hour) {
            return newStatusList
        }

        val availableListOfStatus = arrayListOf<Status>()
        availableListOfStatus.addAll(listOfStatus)

        availableListOfStatus.remove(startStatusChange.status)
        availableListOfStatus.remove(nextStatusChange.status)

        val remainingAvailableStatus = availableListOfStatus[0]
        val endStatusChange = StatusChange(remainingAvailableStatus, endTime)

        newStatusList.add(endStatusChange)
        newStatusList.sortBy { hour -> hour.time.hour }
        return newStatusList
    }

    private fun checkIfNewStatusTimeIsAlreadyTaken(
        startHour: Int,
        endHour: Int
    ): Boolean {
        statusList.forEach { StatusChange ->
            if (StatusChange.time.hour == startHour) {
                Toast.makeText(this, "Status hour is already taken", Toast.LENGTH_SHORT).show()
                return false
            }
            if (StatusChange.time.hour > startHour && StatusChange.time.hour < endHour) {
                Toast.makeText(this, "Invalid períod of time", Toast.LENGTH_SHORT).show()
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