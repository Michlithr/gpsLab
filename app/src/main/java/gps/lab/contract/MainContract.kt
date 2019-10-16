package gps.lab.contract

interface MainContract {

    interface View {
        fun updateView()
        fun showToast(text: String)
    }
}