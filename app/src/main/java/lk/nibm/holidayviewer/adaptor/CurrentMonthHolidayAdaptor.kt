package lk.nibm.holidayviewer.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.holidayviewer.R
import lk.nibm.holidayviewer.model.HolidaysModel


class CurrentMonthHolidayAdaptor(var context: Context, var holidayList: List<HolidaysModel>) : RecyclerView.Adapter<CurrentMonthHolidayAdaptor.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.current_month_holidays, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return holidayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // val date = holidayList[position].holidayDate
        // val name = Position.getDateName(date!!.toInt())

        holder.txtCMHDate.text = holidayList[position].holidayDate
        holder.txtCMHName.text = holidayList[position].holidayName
        holder.txtCMHPrimaryType.text = holidayList[position].holidayPrimaryType
        holder.txtCMHDescription.text = holidayList[position].holidayDescription
        val isVisible: Boolean = holidayList[position].isVisible
        holder.linearLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        holder.txtCMHName.setOnClickListener {
            holidayList[position].isVisible = !holidayList[position].isVisible
            if (holidayList[position].isVisible) {
                holder.txtCMHName.setTextColor(ContextCompat.getColor(context, R.color.blue_600))
            } else {
                holder.txtCMHName.setTextColor(ContextCompat.getColor(context, R.color.gray_800))
            }
            notifyDataSetChanged()
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCMHDate: TextView = itemView.findViewById(R.id.txtCMHDate)
        val txtCMHName: TextView = itemView.findViewById(R.id.txtCMHName)
        val txtCMHPrimaryType: TextView = itemView.findViewById(R.id.txtCMHPrimaryType)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val txtCMHDescription: TextView = itemView.findViewById(R.id.txtCMHDescription)
    }
}