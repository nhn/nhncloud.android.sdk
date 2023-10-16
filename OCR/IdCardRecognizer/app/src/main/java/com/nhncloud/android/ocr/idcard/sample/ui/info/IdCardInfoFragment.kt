package com.nhncloud.android.ocr.idcard.sample.ui.info

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nhncloud.android.ocr.idcard.sample.R
import com.nhncloud.android.ocr.idcard.sample.databinding.FragmentIdCardInfoBinding
import com.nhncloud.android.ocr.idcard.sample.ui.EventObserver
import com.nhncloud.android.ocr.idcard.sample.ui.IdCardDataCacheViewModel

class IdCardInfoFragment : Fragment() {
    private val dataCacheViewModel: IdCardDataCacheViewModel by activityViewModels()
    private val infoViewModel: IdCardInfoViewModel by viewModels()

    private var _binding: FragmentIdCardInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentIdCardInfoBinding>(
            inflater,
            R.layout.fragment_id_card_info,
            container,
            false
        ).apply {
            lifecycleOwner = this@IdCardInfoFragment
            viewModel = infoViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataCacheViewModel.idCardRecognitionData.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                infoViewModel.setIdCardRecognitionData(data)
            }
        }

        infoViewModel.residentRecognitionData.observe(viewLifecycleOwner) { data ->
            binding.idCardResidentTypeInfo.idCardName.setText(data.name.value)
            binding.idCardResidentTypeInfo.idCardResidentNumber.setText(data.residentNumber.value)
            binding.idCardResidentTypeInfo.idCardIssueDate.setText(data.issueDate.value)
            binding.idCardResidentTypeInfo.idCardIssuer.setText(data.issuer.value)
            binding.idCardResidentTypeInfo.root.visibility = View.VISIBLE
        }

        infoViewModel.driverRecognitionData.observe(viewLifecycleOwner) { data ->
            binding.idCardDriverTypeInfo.idCardName.setText(data.name.value)
            binding.idCardDriverTypeInfo.idCardResidentNumber.setText(data.residentNumber.value)
            binding.idCardDriverTypeInfo.idCardIssueDate.setText(data.issueDate.value)
            binding.idCardDriverTypeInfo.idCardIssuer.setText(data.issuer.value)
            binding.idCardDriverTypeInfo.idCardLicenseNumber.setText(data.driverLicenseNumber.value)
            binding.idCardDriverTypeInfo.idCardSerialNumber.setText(data.serialNumber.value)
            binding.idCardDriverTypeInfo.idCardLicenseType.addTextViews(data.licenseType.value.split('/'))
            if (data.condition.value.isEmpty) {
                binding.idCardDriverTypeInfo.idCardLicenseConditionLayout.visibility = View.GONE
            } else {
                binding.idCardDriverTypeInfo.idCardLicenseCondition.setText(data.condition.value)
            }
            binding.idCardDriverTypeInfo.root.visibility = View.VISIBLE
        }

        infoViewModel.alertEvent.observe(viewLifecycleOwner, EventObserver { message ->
            AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}