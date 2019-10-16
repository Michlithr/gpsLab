package gps.lab.listeners

import android.location.Location
import android.os.Bundle
import android.util.Log
import gps.lab.contract.MainContract

class LocationListener(private var view: MainContract.View) : android.location.LocationListener {

    override fun onLocationChanged(point: Location?) {

        if (point != null) {
            view.updateView(point.latitude, point.longitude, point.speed, point.bearing)
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        Log.println(Log.INFO, "Location", "Location status changed.")
    }

    override fun onProviderEnabled(p0: String?) {
        Log.println(Log.INFO, "Location", "Location provider enabled")
    }

    override fun onProviderDisabled(p0: String?) {
        Log.println(Log.INFO, "Location", "Location provided disabled")
    }
}