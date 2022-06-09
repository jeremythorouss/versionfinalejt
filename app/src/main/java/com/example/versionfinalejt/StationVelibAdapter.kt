package com.example.versionfinalejt

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.versionfinalejt.model.StationVelib

class StationVelibAdapter(val favoris: MutableList<StationVelib>) :
    RecyclerView.Adapter<StationVelibAdapter.FavorisVIewHolder>() {

    class FavorisVIewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: FavorisVIewHolder, position: Int) {

        val favoris = favoris[position]

        holder.view.setOnClickListener {
            val context = it.context
            val intent = Intent(context, DetailsStationVelibActivity::class.java)
            intent.putExtra("idStation", favoris.station_id)
            context.startActivity(intent)
        }

        val textviewId = holder.view.findViewById<TextView>(R.id.adapter_stationvelib_id_text_view)
        val textviewName = holder.view.findViewById<TextView>(R.id.adapter_stationvelib_name_textview)

        textviewId.text = favoris.station_id.toString()
        textviewName.text = favoris.name

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorisVIewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val velibView = inflater.inflate(R.layout.adapter_station_velib, parent, false)
        return FavorisVIewHolder(velibView)
    }

    override fun getItemCount()=favoris.size
}



