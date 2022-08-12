package com.fret.gallery_detail.impl.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.gallery_detail.impl.adapters.GalleryDetailAdapter
import com.fret.gallery_detail.impl.databinding.FragmentDetailBinding
import com.fret.gallery_detail.impl.di.GalleryDetailBindings
import com.fret.gallery_detail.impl.di.GalleryDetailComponent
import com.fret.gallery_detail.impl.viewmodels.GalleryDetailViewModel
import com.fret.menus.language.LanguageMenuProvider
import com.fret.menus.language.LanguageSelectListenerImpl
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindingViewModelFactory
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class GalleryDetailFragment : Fragment(),
    DaggerComponentOwner
{
    private var _binding: FragmentDetailBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override val daggerComponent: GalleryDetailComponent by fragmentComponent { _, app ->
        app.bindings<GalleryDetailComponent.ParentBindings>().detailComponentBuilder().create()
    }

    private val galleryDetailViewModel: GalleryDetailViewModel by bindingViewModelFactory {
        arguments?.getString(getString(com.fret.gallery_detail.api.R.string.nav_arg_detail))
    }

    private val galleryDetailAdapter : GalleryDetailAdapter by lazy { GalleryDetailAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindings<GalleryDetailBindings>().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()
        initList()
    }

    private fun initMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            LanguageMenuProvider(LanguageSelectListenerImpl()), // TODO: Replace with injected version or something
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun setLocale(locale: Locale) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }

    private fun initList() {
        binding.listRecycler.adapter = galleryDetailAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            galleryDetailViewModel.images.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                galleryDetailAdapter.submitList(it)
            }
        }
    }
}