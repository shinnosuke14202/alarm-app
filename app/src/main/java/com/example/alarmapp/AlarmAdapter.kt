package com.example.alarmapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.alarmapp.models.Alarm

class AlarmAdapter(
    private val context: Context,
    private var alarms: MutableList<Alarm>,
    private val onAlarmAction: (Alarm, Action) -> Unit
) : BaseAdapter() {

    enum class Action { TOGGLE, DELETE }

    override fun getCount(): Int = alarms.size
    override fun getItem(position: Int): Alarm = alarms[position]
    override fun getItemId(position: Int): Long = alarms[position].id

    fun updateAlarms(newAlarms: List<Alarm>) {
        alarms.clear()
        alarms.addAll(newAlarms.sortedBy { "${it.hour}:${it.minute}" })
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.alarm_item, parent, false)

        val alarm = getItem(position)

        view.findViewById<TextView>(R.id.timeText).text = alarm.getTimeString()
        view.findViewById<TextView>(R.id.labelText).text = alarm.label
        view.findViewById<TextView>(R.id.daysText).text = alarm.getDaysString()

        val enableSwitch = view.findViewById<Switch>(R.id.enableSwitch)
        enableSwitch.isChecked = alarm.isEnabled
        enableSwitch.setOnCheckedChangeListener { _, _ ->
            onAlarmAction(alarm, Action.TOGGLE)
        }

        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            onAlarmAction(alarm, Action.DELETE)
        }

        return view
    }
}