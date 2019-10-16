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

    init {
        points = ArrayList()
    }

    override fun addPoint(lat: Double, lon: Double) {

        points!!.add(GpsDataModel(lat, lon))

        if(points!!.size > 1)
            calculateMinDistance(lat, lon)

        view!!.showToast("Point $lat, $lon saved")
    }

    private fun calculateMinDistance(lat: Double, lon: Double) {

        val newPoint = Location("")
        newPoint.latitude = lat
        newPoint.longitude = lon
        var oldPoint = Location("")
        oldPoint.latitude = points!![0].getLat()
        oldPoint.longitude = points!![0].getLon()

        var minDistance = Float.MAX_VALUE
        var bearing = calculateBearing(newPoint, oldPoint)

        points!!.distinct().forEach {
            oldPoint = Location("")
            oldPoint.latitude = it.getLat()
            oldPoint.longitude = it.getLon()

            if(oldPoint.latitude != newPoint.latitude && oldPoint.longitude != newPoint.longitude) {
                val newDistance = newPoint.distanceTo(oldPoint)
                if (minDistance > newDistance) {
                    minDistance = newDistance
                    bearing = calculateBearing(newPoint, oldPoint)
                }
            }
        }

        view!!.updateDistanceBetweenPoints(minDistance, bearing)
    }

    private fun calculateBearing(newPoint: Location, oldPoint: Location): Double {

        val dLon = oldPoint.longitude - newPoint.longitude
        val y = sin(dLon) * cos(oldPoint.latitude)
        val x = cos(newPoint.latitude) * sin(oldPoint.latitude)
                        - sin(newPoint.latitude) * cos(oldPoint.latitude) * cos(dLon)
        val bearing = Math.toDegrees(atan2(y, x))
        return 360 - ((bearing + 360) % 360)
    }
}