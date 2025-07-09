package com.example.alarmapp

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.example.alarmapp.databinding.ActivityMainBinding
import com.example.alarmapp.features.alarm.ui.AlarmFragment
import com.example.alarmapp.features.stopwatch.StopWatchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fragments = listOf(
        AlarmFragment.newInstance(),
        StopWatchFragment.newInstance()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(fragments[0])

        binding.tabAlarm.setOnClickListener {
            replaceFragment(fragments[0])
            updateIcons(0)
        }

        binding.tabStopwatch.setOnClickListener {
            replaceFragment(fragments[1])
            updateIcons(1)
        }
    }

    private fun updateIcons(index: Int) {
        when (index) {
            0 -> {
                binding.alarmIcon.setColorFilter("#FF1976D2".toColorInt(), PorterDuff.Mode.SRC_IN)
                binding.alarmTitle.setTextColor("#FF1976D2".toColorInt())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }
}
