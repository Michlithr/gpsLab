package gps.lab.presenter

import android.location.Location
import gps.lab.BasePresenter
import gps.lab.contracts.MainContract
import gps.lab.models.GpsDataModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private var points: ArrayList<GpsDataModel>? = null
    private var currentLocation: GpsDataModel? = null

    init {
        points = ArrayList()
        currentLocation = GpsDataModel(0.0, 0.0)
    }

    override fun updateLocation(lat: Double, lon: Double) {

        currentLocation!!.setLat(lat)
        currentLocation!!.setLon(lon)
    }

    override fun addPoint(lat: Double, lon: Double) {

        points!!.add(GpsDataModel(lat, lon))

        if (points!!.size > 0)
            calculateMinDistance()

        view!!.showToast("Point $lat, $lon saved")
    }

    private fun calculateMinDistance() {

        val currentLoc = Location("")
        currentLoc.latitude = currentLocation!!.getLat()
        currentLoc.longitude = currentLocation!!.getLon()

        var minDistance = Float.MAX_VALUE
        var bearing = 0.0

        points!!.distinct().forEach {
            val point = Location("")
            point.latitude = it.getLat()
            point.longitude = it.getLon()

            val newDistance = currentLoc.distanceTo(point)
            if (minDistance > newDistance) {
                minDistance = newDistance
                bearing = calculateBearing(currentLoc, point)
            }
        }

        view!!.updateDistanceBetweenPoints(minDistance, bearing)
    }

    private fun calculateBearing(newPoint: Location, oldPoint: Location): Double {

        val dLon = oldPoint.longitude - newPoint.longitude
        val y = sin(dLon) * cos(oldPoint.latitude)
        val x = cos(newPoint.latitude) * sin(oldPoint.latitude)
        -sin(newPoint.latitude) * cos(oldPoint.latitude) * cos(dLon)
        val bearing = Math.toDegrees(atan2(y, x))
        return 360 - ((bearing + 360) % 360)
    }
}