package lk.nibm.holidayviewer.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lk.nibm.holidayviewer.R
import lk.nibm.holidayviewer.model.MHModel

class MHAdapter(var context: Context, var parentList: List<MHModel>) : RecyclerView.Adapter<MHAdapter.MyViewHolder>()  {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.parent_holidays, parent, false))
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val parentItem = parentList[position]
        holder.txtMonth.text = parentItem.name
        holder.childRecyclerView.setHasFixedSize(true)
        holder.childRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        val adapter = HolidayAdapter(context,parentItem.holidays)
        holder.childRecyclerView.adapter = adapter
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMonth: TextView = itemView.findViewById(R.id.txtMonth)
        val childRecyclerView : RecyclerView = itemView.findViewById(R.id.childRecyclerView)
    }
}