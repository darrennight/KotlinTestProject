package com.bread.room.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bread.room.R
import com.bread.room.net.bean.Room
import com.bread.room.ui.RoomDetailActivity
import kotlinx.android.synthetic.main.item_room_list.view.*

/**
 * Created by zenghao on 2017/6/8.
 */
class RoomListAdapter(val context: Context,val list:ArrayList<Room> ):RecyclerView.Adapter<RoomListAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): RoomViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.item_room_list,parent,false)

        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = list[position]
        holder.itemView.tv_number.text = room.room_id.toString()
        holder.itemView.tv_name.text = room.name

        holder.itemView.tv_landlord_name.text = if(!TextUtils.isEmpty(room.landlord_name)) room.landlord_name else "bread"

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,RoomDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }




    inner class RoomViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
}