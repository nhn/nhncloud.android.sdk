package com.nhncloud.android.ocr.creditcard.sample.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.nhncloud.android.ocr.creditcard.sample.R
import com.nhncloud.android.ocr.creditcard.sample.databinding.FragmentCreditCardInfoBinding
import com.nhncloud.android.ocr.creditcard.sample.ui.CreditCardRecognitionDataCacheViewModel

class CreditCardInfoFragment : Fragment() {
    private val dataCacheViewModel: CreditCardRecognitionDataCacheViewModel by activityViewModels()
    private val infoViewModel: CreditCardInfoViewModel by viewModels()

    private var _binding: FragmentCreditCardInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentCreditCardInfoBinding>(
            inflater,
            R.layout.fragment_credit_card_info,
            container,
            false
        ).apply {
            lifecycleOwner = this@CreditCardInfoFragment
            viewModel = infoViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataCacheViewModel.creditCardRecognitionData.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                infoViewModel.setCreditCardRecognitionData(data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}