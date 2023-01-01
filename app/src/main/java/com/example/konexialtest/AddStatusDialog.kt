package com.example.konexialtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import com.example.konexialtest.databinding.AddStatusDialogBinding

class AddStatusDialog(
    private val addStatus: (newStatus: Int, startTime: Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: AddStatusDialogBinding
    private var statusSelected = START_INDEX
    private var startTime = START_INDEX

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddStatusDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addStatusBtn.setOnClickListener {
            addStatus.invoke(statusSelected, startTime)
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
    }

    companion object {
        const val START_INDEX = 0
    }
}