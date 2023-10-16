package com.nhncloud.android.ocr.idcard.sample.ui.scan

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nhncloud.android.ocr.idcard.sample.R
import com.nhncloud.android.ocr.idcard.sample.databinding.FragmentIdCardScanBinding
import com.nhncloud.android.ocr.idcard.sample.ui.EventObserver
import com.nhncloud.android.ocr.idcard.sample.ui.IdCardDataCacheViewModel
import timber.log.Timber

class IdCardScanFragment : Fragment() {
    private val dataCacheViewModel: IdCardDataCacheViewModel by activityViewModels()
    private val scanViewModel: IdCardScanViewModel by viewModels()

    private var _binding: FragmentIdCardScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdCardScanBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataCacheViewModel.setData(null)
        scanViewModel.recognizedEvent.observe(viewLifecycleOwner, EventObserver { data ->
            Timber.d("ID Card recognized: $data")
            vibrate()
            dataCacheViewModel.setData(data)
            findNavController().navigate(R.id.action_scanFragment_to_infoFragment)
        })
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.startRecognizer(binding.cameraPreview)
    }

    override fun onPause() {
        super.onPause()
        scanViewModel.stopRecognizer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scanViewModel.releaseRecognizer()
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