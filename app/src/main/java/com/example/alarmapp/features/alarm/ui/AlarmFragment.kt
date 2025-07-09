package com.example.alarmapp.features.alarm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmapp.R
import com.example.alarmapp.databinding.FragmentAlarmBinding
import com.example.alarmapp.features.alarm.service.AlarmScheduler
import com.example.alarmapp.models.AlarmItem
import com.example.alarmapp.utils.NotificationHelper
import java.time.LocalDateTime


class AlarmFragment : Fragment() {

    private lateinit var binding : FragmentAlarmBinding

    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmScheduler = AlarmScheduler(requireContext())
        binding.switchAlarm1.setOnClickListener {
            if (it.isEnabled) {
                alarmScheduler.schedule(AlarmItem(
                    time = LocalDateTime.now().plusSeconds(5),
                    "test"
                ))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlarmFragment()
    }
}