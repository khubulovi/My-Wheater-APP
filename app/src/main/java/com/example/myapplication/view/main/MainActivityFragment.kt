package com.example.myapplication.view.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.view.details.DetailsFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainActivityBinding
import com.example.myapplication.model.Wheater
import com.example.myapplication.viewmodels.AppState
import com.example.myapplication.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar


class MainActivityFragment : Fragment() {
    private var _binding: FragmentMainActivityBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun itemViewClickListener(weather: Wheater) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(
                        R.id.fragment_container_view_tag,
                        DetailsFragment.newInstance(Bundle().apply {
                            putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                        })
                    ).addToBackStack("")
                    .commit()
            }
        }
    })

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

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    interface OnItemViewClickListener {
        fun itemViewClickListener(weather: Wheater)
    }

    companion object {
        fun newInstance(): MainActivityFragment = MainActivityFragment()
    }
}
