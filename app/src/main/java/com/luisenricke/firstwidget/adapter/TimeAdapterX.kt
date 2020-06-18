package com.luisenricke.firstwidget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luisenricke.firstwidget.R
import com.luisenricke.firstwidget.data.dao.TimerDAO
import com.luisenricke.firstwidget.data.entity.Timer
import kotlinx.android.synthetic.main.adapter_time.view.*

class TimeAdapterX(
    private var list: MutableList<Timer>,
    private val itemClickListener: OnItemClickListener,
    private val timerDao: TimerDAO
) : RecyclerView.Adapter<TimeAdapterX.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_time, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel = list.get(position)
        holder.bind(dataModel, position, itemClickListener)
    }

    inner class ViewHolder(val itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var id = itemLayoutView.lbl_id
        var time = itemLayoutView.lbl_time
        var distance = itemLayoutView.lbl_distance
        var delete = itemLayoutView.btn_delete

        fun bind(
            item: Timer,
            position: Int,
            clickListener: OnItemClickListener
        ) {
            id.text = item.id.toString()
            time.text = item.time
            distance.text = item.distance

            delete.setOnClickListener {
                //clickListener.onItemClicked(item)
                timerDao.delete(id.text.toString().toLong())
                list.removeAt(position)
                notifyDataSetChanged()
            }

            itemLayoutView.setOnClickListener {
                clickListener.onItemClicked(item)
            }
        }

    }

    //
//    fun filterList(filteredNames: ArrayList<recyclerresponse>) {
//        Log.e("list", filteredNames.toString())
//        Log.e("list", filteredNames.size.toString())
//        // this.dataList.clear()
//        this.list = filteredNames
//        notifyDataSetChanged()
//    }
    interface OnItemClickListener {
        fun onItemClicked(item: Timer)
    }

}
