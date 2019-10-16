package gps.lab

open class BasePresenter<T> {

    protected var view: T? = null

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun isViewAttached() = view != null
}