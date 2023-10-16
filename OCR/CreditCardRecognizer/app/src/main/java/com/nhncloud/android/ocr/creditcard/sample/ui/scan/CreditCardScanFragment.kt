package com.nhncloud.android.ocr.creditcard.sample.ui.scan

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nhncloud.android.ocr.creditcard.sample.R
import com.nhncloud.android.ocr.creditcard.sample.databinding.FragmentCreditCardScanBinding
import com.nhncloud.android.ocr.creditcard.sample.ui.CreditCardRecognitionDataCacheViewModel
import com.nhncloud.android.ocr.creditcard.sample.ui.EventObserver
import timber.log.Timber

class CreditCardScanFragment : Fragment() {
    private val dataCacheViewModel: CreditCardRecognitionDataCacheViewModel by activityViewModels()
    private val scanViewModel: CreditCardScanViewModel by viewModels()

    private var _binding: FragmentCreditCardScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentCreditCardScanBinding>(
            inflater,
            R.layout.fragment_credit_card_scan,
            container,
            false
        ).apply {
            lifecycleOwner = this@CreditCardScanFragment
            viewModel = scanViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataCacheViewModel.setData(null)
        scanViewModel.recognizedEvent.observe(viewLifecycleOwner, EventObserver { data ->
            Timber.d("Credit card recognized: $data")
            vibrate()
            dataCacheViewModel.setData(data)
            findNavController().navigate(R.id.action_scanFragment_to_infoFragment)
        })
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.startRecognitionService(binding.cameraPreview)
    }

    override fun onPause() {
        super.onPause()
        scanViewModel.stopRecognitionService()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scanViewModel.releaseRecognitionService()
        _binding = null
    }

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                requireActivity().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(VIBRATION_PATTERN, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(VIBRATION_PATTERN, -1)
        }
    }

    companion object {
        private val VIBRATION_PATTERN = longArrayOf(0L, 70L, 10L, 40L)
    }
}