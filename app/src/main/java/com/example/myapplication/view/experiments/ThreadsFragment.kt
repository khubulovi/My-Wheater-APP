package com.example.myapplication.view.experiments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import coil.api.load
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentThreadsBinding
import java.util.Date
import java.util.concurrent.TimeUnit

const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"
const val THREADS_FRAGMENT_BROADCAST_EXTRA = "THREADS_FRAGMENT_EXTRA"

class ThreadsFragment : Fragment() {

    private var counterThread = 0
    private var _binding: FragmentThreadsBinding? = null
    private val binding get() = _binding!!

    private val testReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.getStringExtra(THREADS_FRAGMENT_BROADCAST_EXTRA)?.let {
                addView(context, it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(testReceiver, IntentFilter(TEST_BROADCAST_INTENT_FILTER))
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(testReceiver)
        }
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView1.load("https://i.pinimg.com/originals/ab/91/06/ab91067a11279f2f90892b92f8682dbe.png")
        binding.imageView2.load("https://i.pinimg.com/originals/ab/91/06/ab91067a11279f2f90892b92f8682dbe.png")
        initButtonMainThread()
        initButtonWorkerThread()
        initButtonHandlerThread()
        initServiceButton()
        initServiceWithBroadcastButton()
    }

    private fun initServiceWithBroadcastButton() {
        binding.serviceWithBroadcastButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_INT_EXTRA,
                        binding.editText.text.toString().toInt()
                    )
                })
            }
        }
    }

    private fun initServiceButton() {
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_STRING_EXTRA,
                        getString(R.string.hello_from_thread_fragment)
                    )
                })
            }
        }
    }

    private fun initButtonHandlerThread() {
        val handlerThread = HandlerThread(getString(R.string.my_handler_thread))
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        binding.calcThreadHandler.setOnClickListener {
            addView(
                it.context,
                String.format(getString(R.string.calculate_in_thread), handlerThread.name)
            )
            handler.post {
                startCalculations(binding.editText.text.toString().toInt())
                binding.mainContainer.post {
                    addView(
                        it.context,
                        String.format(getString(R.string.in_thread), Thread.currentThread().name)
                    )
                }
            }
        }
    }

    private fun initButtonWorkerThread() {
        binding.calcThreadBtn.setOnClickListener {
            Thread {
                counterThread++
                val calculatedText = startCalculations(binding.editText.text.toString().toInt())
                activity?.runOnUiThread {
                    binding.textView.text = calculatedText
                    addView(
                        it.context,
                        String.format(getString(R.string.from_thread), counterThread)
                    )
                }
            }.start()
        }
    }

    private fun initButtonMainThread() {
        binding.button.setOnClickListener {
            binding.textView.text = startCalculations(binding.editText.text.toString().toInt())
            addView(it.context, getString(R.string.in_main_thread))
        }
    }

    private fun addView(context: Context, textToShow: String) {
        binding.mainContainer.addView(AppCompatTextView(context).apply {
            text = textToShow
        })
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long
        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)
        return diffInSec.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }
}