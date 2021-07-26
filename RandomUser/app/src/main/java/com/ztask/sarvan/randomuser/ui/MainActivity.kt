package com.ztask.sarvan.randomuser.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.ztask.sarvan.randomuser.R
import com.ztask.sarvan.randomuser.data.model.WeatherApiResponse
import com.ztask.sarvan.randomuser.databinding.ActivityMainBinding
import com.ztask.sarvan.randomuser.utils.PermissionUtils
import com.ztask.sarvan.randomuser.viewmodel.MainViewModel
import com.ztask.sarvan.randomuser.viewmodel.factory.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 7777
    }
    private lateinit var viewModel: MainViewModel
    private lateinit var viewBinding: ActivityMainBinding

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModel = ViewModelProvider(this, MainViewModelFactory(this@MainActivity)).get(MainViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    if (latitude <= 0 && longitude<=0) {
                        for (location in locationResult?.locations!!) {
                            latitude = location.latitude
                            longitude = location.longitude
                        }
                        getWeatherDetails(latitude.toString(), longitude.toString())
                    }
                }

                override fun onLocationAvailability(locationResult: LocationAvailability?) {
                    super.onLocationAvailability(locationResult)
                }
            },
            Looper.myLooper()
        )

    }

    fun getWeatherDetails(latitude: String, longitude: String){
        viewModel.getWeatherDetails(latitude, longitude)
        viewModel.weatherInfo.observe(this, Observer {
            Log.d("TAG", "getWeatherDetails: "+ Gson().toJson(it))
            val celciusValue: Double = it.main?.temp?.toDouble()!! - 273
            viewBinding.maintoolbar.tvDescription.text = "${celciusValue.toInt()} ${getString(R.string.degree)} C, ${it.weather?.get(0)?.description}"
            viewBinding.maintoolbar.tvPlace.text = "${it.name}"
            viewBinding.maintoolbar.weatherdataloader.visibility = View.GONE
            viewBinding.maintoolbar.ivCloud.setImageResource(
                    when (it.weather?.get(0)?.description.toString().toLowerCase()) {
                        "clear sky" -> R.drawable.clear_sky
                        "few clouds" -> R.drawable.few_clouds
                        "scattered clouds" -> R.drawable.scattered_clouds
                        "broken clouds" -> R.drawable.broken_clouds
                        "shower rain" -> R.drawable.shower_rain
                        "rain" -> R.drawable.rain
                        "thunderstorm" -> R.drawable.thunder_storm
                        "snow" -> R.drawable.snow
                        "mist" -> R.drawable.mist
                        else -> R.drawable.clear_sky
                    }
            )
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}