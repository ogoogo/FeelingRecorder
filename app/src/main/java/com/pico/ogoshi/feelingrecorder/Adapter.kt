package com.pico.ogoshi.feelingrecorder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class Adapter(
    private val context: Context,
    private var eventList: OrderedRealmCollection<Memo>?,
    private val autoUpdate: Boolean
    ) : RealmRecyclerViewAdapter<Memo,Adapter.ViewHolder>(eventList,autoUpdate) {

        class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            val dateText:TextView=view.findViewById(R.id.datetextView)
            val eventText:TextView=view.findViewById(R.id.eventtextView2)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view=LayoutInflater.from(context).inflate(R.layout.activity_viewholder,viewGroup,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event :Memo= eventList?.get(position)?:return
            holder.dateText.text=event.date.toString()
            holder.eventText.text=event.event


        }

        override fun getItemCount(): Int {
            return eventList?.size?:0
        }

}