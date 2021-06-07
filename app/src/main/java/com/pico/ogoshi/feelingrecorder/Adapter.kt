package com.pico.ogoshi.feelingrecorder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class Adapter(
    private val context: Context,
    private var eventList: OrderedRealmCollection<Memo>?,
    private val autoUpdate: Boolean
    ) : RealmRecyclerViewAdapter<Memo,Adapter.ViewHolder>(eventList,autoUpdate) {

    lateinit var listener: OnItemClickListener

        class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            val dateText:TextView=view.findViewById(R.id.datetextView)
            val eventText:TextView=view.findViewById(R.id.eventtextView2)
            val personText:TextView=view.findViewById(R.id.personTextView2)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view=LayoutInflater.from(context).inflate(R.layout.activity_viewholder,viewGroup,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event :Memo= eventList?.get(position)?:return
            holder.dateText.text="${event.year.toString()}年\n${event.month.toString()}月${event.date.toString()}日"
            if(event.quoteOrNot==true){
                holder.eventText.text=event.quote
            }else{
                holder.eventText.text=event.event
            }
            holder.personText.text=event.personName
            holder.itemView.setOnClickListener{
                listener.onItemClick(it,position,event)
            }



        }

        override fun getItemCount(): Int {
            return eventList?.size?:0
        }

        interface OnItemClickListener{
            fun onItemClick(view:View, position: Int, event:Memo)
        }

        fun setOnItemClickListener(listener:OnItemClickListener){
            this.listener=listener
        }


}