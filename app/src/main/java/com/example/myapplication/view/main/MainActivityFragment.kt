package com.example.myapplication.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.view.details.DetailsFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainActivityBinding
import com.example.myapplication.model.Wheater
import com.example.myapplication.app.AppState
import com.example.myapplication.model.City
import com.example.myapplication.viewmodels.MainViewModel
import com.google.android.gms.location.LocationListener
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class MainActivityFragment : Fragment() {
    private var _binding: FragmentMainActivityBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun itemViewClickListener(weather: Wheater) {
            openDetailsFragment(weather)
        }
    })
    private val locationListener = LocationListener { location ->
        context?.let {
            getAddressAsync(it, location)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitTransaction")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            mainFragmentFABLocation.setOnClickListener { checkPermission() }
            mainRecyclerView.adapter = adapter
        }
        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner) { renderData(it as AppState) }
            binding.mainFragmentFAB.setOnClickListener { viewModel.changeGeoToWorld() }
            icon.observe(viewLifecycleOwner) {
                binding.mainFragmentFAB.setImageResource(it)
            }
        }
    }


    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Sucsess -> {
                binding.anotherLayout.visibility = View.GONE
                adapter.setWheater(appState.wheateherData)
            }

            is AppState.Loading -> {
                binding.anotherLayout.visibility = View.VISIBLE
            }

            is AppState.Eror -> {
                binding.apply {
                    anotherLayout.visibility = View.GONE
                    mainFragmentActivity.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeatherFromLocalSourceGeo() })
                }
            }
        }
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

                    showRationaleDialog()
                }

                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    if (grantResults.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            "No Gps",
                            "No Gps"
                        )
                    }
                } else {
                    showDialog(
                        "No Gps",
                        "No Gps"
                    )
                }
                return
            }
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationListener
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            "Gps turned off ",
                            "Last known location"
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            "Gps turned off ",
                            "Last known location"
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private fun getAddressAsync(
        context: Context,
        location: Location
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.mainFragmentFAB.post {
                    addresses?.get(0)?.let { showAddressDialog(it.getAddressLine(0), location) }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Give accses") { _, _ ->
                    requestPermission()
                }
                .setNegativeButton("Decline") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Adress Title")
                .setMessage(address)
                .setPositiveButton("Get Wheater") { _, _ ->
                    openDetailsFragment(
                        Wheater(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Close") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun openDetailsFragment(wheater: Wheater) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .replace(
                    R.id.fragment_container_view_tag,
                    DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, wheater)
                    })
                ).addToBackStack("")
                .commit()
        }
    }

    interface OnItemViewClickListener {
        fun itemViewClickListener(weather: Wheater)
    }

    companion object {
        private const val REQUEST_CODE = 12345
        private fun View.showSnackBar(
            text: String,
            actionText: String,
            action: (View) -> Unit,
            length: Int = Snackbar.LENGTH_INDEFINITE
        ) {
            Snackbar.make(this, text, length).setAction(actionText, action).show()
        }

        fun newInstance(): MainActivityFragment = MainActivityFragment()

    }
}
