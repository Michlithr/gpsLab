package gps.lab.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import gps.lab.Constants.Companion.COARSE_LOC_CODE
import gps.lab.Constants.Companion.FINE_LOC_CODE
import gps.lab.Constants.Companion.INTERNET_CODE
import gps.lab.R
import gps.lab.contracts.MainContract
import gps.lab.listeners.LocationListener
import gps.lab.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainView : AppCompatActivity(), MainContract.View {

    private val permissionCodes = Array(3) { i -> i + 1 }
    private var permissionsGranted = true

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    private var mainPresenter: MainPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()
        setupGpsApi()

        mainPresenter = MainPresenter()
        mainPresenter!!.attachView(this)

        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter!!.detachView()
    }

    override fun updateLocation(lat: Double, lon: Double, speed: Float, bearing: Float) {

        latitude.text = lat.toString()
        longitude.text = lon.toString()
        if (speed != 0f)
            this.speed.text = speed.toString()
        if (bearing != 0f)
            this.direction.text = "$bearing degree"
    }

    override fun updateDistanceBetweenPoints(distance: Float, bearing: Double) {

        this.distanceBetweenPoints.text = distance.toString()
        this.directionBetweenPoints.text = bearing.toString()
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setupView() {

        saveButton.setOnClickListener {

            try {
                mainPresenter!!.addPoint(
                    userLatitude.text.toString().toDouble(),
                    userLongitude.text.toString().toDouble()
                )
            } catch (exception: Exception) {
                showToast("Bad data format!")
            }

        }
    }

    private fun setupGpsApi() {

        if (permissionsGranted) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = LocationListener(this)
            try {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )
            } catch (securityException: SecurityException) {
                showToast("There's problem with your permissions!")
            }
        }
    }

    private fun setupPermissions() {
        permissionCodes.forEach { checkPermission(it) }
    }

    private fun checkPermission(code: Int) {

        var permission = 0
        when (code) {
            INTERNET_CODE -> permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            )
            FINE_LOC_CODE -> permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            COARSE_LOC_CODE -> permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        if (permission != PackageManager.PERMISSION_GRANTED)
            makeRequest(code)
    }

    private fun makeRequest(code: Int) {

        when (code) {
            INTERNET_CODE -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET), INTERNET_CODE
            )
            FINE_LOC_CODE -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_LOC_CODE
            )
            COARSE_LOC_CODE -> ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), COARSE_LOC_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                this, "Permissions denied! You have to grant permissions.",
                Toast.LENGTH_SHORT
            ).show()
            permissionsGranted = false
        }
    }
}
