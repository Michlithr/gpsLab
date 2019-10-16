package gps.lab.models

class GpsDataModel(private var lat: Double, private var lon: Double) {

    fun getLat() = lat

    fun setLat(lat: Double) {
        this.lat = lat
    }

    fun getLon() = lon

    fun setLon(lon: Double) {
        this.lon = lon
    }
}