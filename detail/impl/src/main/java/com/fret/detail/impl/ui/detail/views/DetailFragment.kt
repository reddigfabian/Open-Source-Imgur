package com.fret.detail.impl.ui.detail.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fret.detail.databinding.FragmentDetailBinding
import com.fret.detail.impl.ui.detail.viewmodels.DetailViewModel
import tangle.viewmodel.fragment.tangleViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val detailViewModel : DetailViewModel by tangleViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDetail.text = detailViewModel.text
    }
}