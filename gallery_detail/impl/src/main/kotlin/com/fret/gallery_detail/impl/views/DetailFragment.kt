package com.fret.gallery_detail.impl.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fret.gallery_detail.impl.DetailBindings
import com.fret.gallery_detail.impl.DetailComponent
import com.fret.gallery_detail.impl.databinding.FragmentDetailBinding
import com.fret.gallery_detail.impl.viewmodels.DetailViewModel
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindingViewModelFactory
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent

class DetailFragment : Fragment(), DaggerComponentOwner {
    private var _binding: FragmentDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override val daggerComponent: DetailComponent by fragmentComponent { _, app ->
        app.bindings<DetailComponent.ParentBindings>().detailComponentBuilder().create()
    }

//    private val detailViewModel: DetailViewModel by bindingViewModelFactory("Test")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        bindings<DetailBindings>().inject(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireActivity().title = detailViewModel.title
    }
}