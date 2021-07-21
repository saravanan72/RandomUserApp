package com.ztask.me.randomuser.ui.splash

import com.google.android.gms.location.FusedLocationProviderClient
import com.ztask.me.randomuser.R
import com.ztask.me.randomuser.databinding.FragmentSplashBinding

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {


    private lateinit var splashFragmentBinding: FragmentSplashBinding
    private lateinit var navController: NavController
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        dataStore = requireActivity().createDataStore(name = "data-store-prefs")
//        goToNextScreen()
        getCurrentLocation()
    }
    private fun getCurrentLocation() {
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
        if (ContextCompat.checkSelfPermission(
                        requireActivity(),
                        ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        requireActivity(),
                        ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocationDetail()
        } else {
            requestMultiplePermissions.launch(
                    arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
            )
        }
    }

    private val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    //Log.d(TAG, "${it.key} = ${it.value}")
                }
                if (permissions[ACCESS_COARSE_LOCATION] == true && permissions[ACCESS_COARSE_LOCATION] == true) {
                    getCurrentLocationDetail()
                } else {
                    //Log.d(TAG, "Permission not granted")
                }
            }


    private fun getCurrentLocationDetail() {
        val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            if (ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireActivity(),
                            ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if (it.result != null) {
                    latitude = it.result.latitude.toString()
                    longitude = it.result.longitude.toString()
                    if (activity != null)
                        goToNextScreen()
                } else {
                    val locationRequest = LocationRequest.create().apply {
                        interval = 100
                        fastestInterval = 50
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        maxWaitTime = 100
                    }

                    val locationCallback: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationRequest: LocationResult) {
                            latitude = locationRequest.lastLocation.latitude.toString()
                            longitude = locationRequest.lastLocation.longitude.toString()
                            goToNextScreen()
                        }
                    }

                    fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.myLooper()!!
                    )
                }
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    private fun goToNextScreen() {
        lifecycleScope.launch {
            val bundle = Bundle()
            bundle.putString("latitude", latitude)
            bundle.putString("longitude", longitude)
            navController.navigate(
                    R.id.action_splashFragment_to_userListFragment,
                    bundle
            )
        }

    }

    private suspend fun read(key: String): Boolean {
        val datastoreKey = preferencesKey<Boolean>(key)
        val preferences = dataStore.data.first()
        return preferences[datastoreKey] ?: false
    }

    private suspend fun save(key: String, value: Boolean) {
        val datastoreKey = preferencesKey<Boolean>(key)
        dataStore.edit { save ->
            save[datastoreKey] = value
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        splashFragmentBinding = FragmentSplashBinding.inflate(inflater, container, false)
        return splashFragmentBinding.root
    }


}