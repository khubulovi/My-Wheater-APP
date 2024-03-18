package com.example.myapplication.view.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapsMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException

@Suppress("DEPRECATION")
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private var marker: ArrayList<Marker> = arrayListOf()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val initialPlace = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(initialPlace).title("Set Peace"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialPlace))
        googleMap.setOnMapLongClickListener { latLang ->
            getAddressAsync(latLang)
            addMarkerToArray(latLang)
            drawLine()
        }
        activateMyLocation(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchByAdress()
    }

    private fun initSearchByAdress() {
        binding.buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(it.context)
            val searchText = binding.searchAddress.text.toString()
            Thread {
                try {
                    val adress = geoCoder.getFromLocationName(searchText, 1)
                    if (adress?.size!! > 0) {
                        goToAdress(adress, it, searchText)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun goToAdress(adress: MutableList<Address>, view: View?, searchText: String) {
        val location = LatLng(adress[0].latitude, adress[0].longitude)
        view?.post {
            setMarker(location, searchText)
            map.moveCamera(
                CameraUpdateFactory.newLatLng(location)
            )
        }
    }

    private fun getAddressAsync(location: LatLng) {
        context?.let {
            val geocoder = Geocoder(it)
            Thread {
                try {
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    binding.textAddress.post {
                        binding.textAddress.text =
                            addresses?.get(0)?.getAddressLine(0)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    private fun addMarkerToArray(location: LatLng) {
        val markers = setMarker(location, marker.size.toString())
        if (markers != null) {
            marker.add(markers)
        }
    }

    private fun setMarker(location: LatLng, toString: String): Marker? {
        return map.addMarker(
            MarkerOptions().position(location).title(toString)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )
    }

    private fun drawLine() {
        val last: Int = marker.size - 1
        if (last >= 1) {
            val previous: LatLng = marker[last - 1].position
            val current: LatLng = marker[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun activateMyLocation(googleMap: GoogleMap) {
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            googleMap.isMyLocationEnabled = isPermissionGranted
            googleMap.uiSettings.isMyLocationButtonEnabled = isPermissionGranted
        }
    }

    companion object {
        fun newInstanse() = MapsFragment()
    }
}