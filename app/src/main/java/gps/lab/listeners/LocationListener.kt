package gps.lab.listeners

import android.location.Location
import android.os.Bundle
import gps.lab.contract.MainContract

class LocationListener(private var view: MainContract.View) : android.location.LocationListener {

    override fun onLocationChanged(p0: Location?) {

        if (p0 != null) {
            view.updateView()
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        view.showToast("Status changed!")
    }

    override fun onProviderEnabled(p0: String?) {
        view.showToast("Provider enabled!")
    }

    override fun onProviderDisabled(p0: String?) {
        view.showToast("Provider disabled!")
    }
}