package gps.lab.contracts

interface MainContract {

    interface View {
        fun updateLocation(lat: Double, lon: Double, speed: Float, bearing: Float)
        fun updateDistanceBetweenPoints(distance: Float, bearing: Double)
        fun showToast(text: String)
    }

    interface Presenter {
        fun addPoint(lat: Double, lon: Double)
    }
}