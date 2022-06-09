package com.example.versionfinalejt


import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import com.example.versionfinalejt.model.MarkerHolder
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowAdapter(context: Context, val markerHolderMap : HashMap<String, MarkerHolder>) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val markerHolderMap = markerHolderMap
        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        val tvCapacity = view.findViewById<TextView>(R.id.capacity)
        val tvNbrVelosDispo = view.findViewById<TextView>(R.id.nbrVelosDispo)
        val tvNbrDockDispo = view.findViewById<TextView>(R.id.nbrDockDispo)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet

        val mHolder = markerHolderMap[marker.id]

        tvCapacity.text= mHolder!!.capacity.toString()
        tvNbrVelosDispo.text=mHolder!!.nbrVelosDispo.toString()
        tvNbrDockDispo.text=mHolder!!.nbrDockDispo.toString()
        tvSnippet.setVisibility(View.GONE)
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}


