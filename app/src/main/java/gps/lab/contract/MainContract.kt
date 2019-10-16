package gps.lab.contract

interface MainContract {

    interface View {
        fun updateView(lat: Double, lon: Double, speed: Float, direction: Float)
        fun showToast(text: String)
    }
}