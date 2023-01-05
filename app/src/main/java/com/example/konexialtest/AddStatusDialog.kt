package com.example.konexialtest

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import com.example.konexialtest.databinding.AddStatusDialogBinding

class AddStatusDialog(
    private val addStatus: (newStatus: Int, startTime: Int, endTime: Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: AddStatusDialogBinding
    private var statusSelected = START_INDEX
    private var startTime = START_INDEX
    private var endTime = START_INDEX

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddStatusDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            it.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addStatusBtn.setOnClickListener {
            addStatus.invoke(statusSelected, startTime, endTime)
            this.dismiss()
        }
        binding.statusField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                statusSelected = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.startTimeField.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    startTime = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.endTimeField.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    endTime = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

    companion object {
        const val START_INDEX = 0
    }
}