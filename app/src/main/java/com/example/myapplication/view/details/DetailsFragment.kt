package com.example.myapplication.view.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.myapplication.R
import com.example.myapplication.databinding.DetailsFragmentBinding
import com.example.myapplication.model.Wheater
import com.example.myapplication.app.AppState
import com.example.myapplication.viewmodels.DetailsViewModel
import com.google.android.material.snackbar.Snackbar


@Suppress("DEPRECATION")
class DetailsFragment : Fragment() {
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var wheaterBundle: Wheater
    private val viewModel: DetailsViewModel by lazy { ViewModelProvider(this)[DetailsViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wheaterBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Wheater()
        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner) { renderData(it) }
            viewModel.getWeatherFromRemoteSource(wheaterBundle.city.lat, wheaterBundle.city.lon)
        }
    }
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Sucsess -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingView.visibility = View.GONE
                setWeather(appState.wheateherData[0])
            }

            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingView.visibility = View.VISIBLE
            }
            is AppState.Eror -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingView.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getWeatherFromRemoteSource(
                            wheaterBundle.city.lat,
                            wheaterBundle.city.lon
                        )
                    })
            }
        }
    }

    private fun setWeather(weather: Wheater) {
        with(binding) {
            val city = wheaterBundle.city
            cityName.text = city.city
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )

            weather.icon?.let {
                binding.weatherIcon.load("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg")
                temperatureValue.text = weather.temperature.toString()
                feelsLikeValue.text = weather.feelsLike.toString()
                wheaterCondition.text = weather.condition
            }
            binding.headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    companion object {

        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}