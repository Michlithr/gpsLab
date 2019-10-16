package gps.lab.view

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
import gps.lab.contract.MainContract
import gps.lab.listeners.LocationListener
import kotlinx.android.synthetic.main.activity_main.*

class MainView : AppCompatActivity(), MainContract.View {

    private val permissionCodes = Array(3) { i -> i + 1 }
    private var permissionsGranted = true

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()
        setupGpsApi()
    }

    override fun updateView(lat: Double, lon: Double, speed: Float, direction: Float) {

        latitude.text = lat.toString()
        longitude.text = lon.toString()
        if (speed != 0f)
            this.speed.text = speed.toString()
        if (direction != 0f)
            this.direction.text = "$direction degree"
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setupGpsApi() {

        if (permissionsGranted) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = LocationListener(this)
            try {
                (locationManager as LocationManager).requestLocationUpdates(
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
