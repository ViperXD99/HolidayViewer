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
import lk.nibm.holidayviewer.model.SeparatedHolidaysModel

class HolidayAdapter(var context: Context, private val childList: List<SeparatedHolidaysModel>) :
    RecyclerView.Adapter<HolidayAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.child_holidays, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return childList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtholidayName.text = childList[position].name
        holder.txtHolidayDate.text = childList[position].date
        holder.txtPrimaryType.text = childList[position].primaryType
        holder.txtDescription.text = childList[position].description
        val isVisible : Boolean = childList[position].isVisible
        holder.linearLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
        holder.txtholidayName.setOnClickListener {
            childList[position].isVisible = !childList[position].isVisible
            notifyDataSetChanged()
            if ( childList[position].isVisible){
                holder.txtholidayName.setTextColor(ContextCompat.getColor(context,R.color.blue_600))
            }
            else{
                holder.txtholidayName.setTextColor(ContextCompat.getColor(context,R.color.gray_800))
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtholidayName: TextView = itemView.findViewById(R.id.txtholidayName)
        val txtPrimaryType: TextView = itemView.findViewById(R.id.txtPrimaryType)
        val txtDescription : TextView = itemView.findViewById(R.id.txtHolidayDescription)
        val txtHolidayDate: TextView = itemView.findViewById(R.id.txtHolidayDate)
        val linearLayout : LinearLayout = itemView.findViewById(R.id.rvChildLayout)
    }
}