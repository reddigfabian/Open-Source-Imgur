package com.fret.imgur_gallery.impl.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fret.gallery_list.impl.databinding.FragmentGalleryBinding
import com.fret.imgur_album.api.AlbumNavGraphArgs
import com.fret.imgur_gallery.impl.adapters.GalleryAdapter
import com.fret.imgur_gallery.impl.di.GalleryBindings
import com.fret.imgur_gallery.impl.di.GalleryComponent
import com.fret.imgur_gallery.impl.items.GalleryItem
import com.fret.imgur_gallery.impl.usf.GalleryEffect
import com.fret.imgur_gallery.impl.usf.GalleryEvent
import com.fret.imgur_gallery.impl.usf.GalleryViewState
import com.fret.imgur_gallery.impl.viewmodels.GalleryViewModel
import com.fret.imgur_gallery.impl.viewmodels.viewModelFactory
import com.fret.shared_menus.account.AccountMenuProvider
import com.fret.shared_menus.account.usf.AccountMenuEffect
import com.fret.shared_menus.language.LanguageMenuProvider
import com.fret.usf.UsfEffect
import com.fret.utils.di.DaggerComponentOwner
import com.fret.utils.di.bindings
import com.fret.utils.di.fragmentComponent
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

private const val TAG = "GalleryFragment"

class GalleryFragment : Fragment(), DaggerComponentOwner,
    GalleryAdapter.GalleryItemClickListener {
    private var _binding: FragmentGalleryBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override val daggerComponent: GalleryComponent by fragmentComponent { _, app ->
        app.bindings<GalleryComponent.ParentBindings>().galleryComponentBuilder().create()
    }

    private val galleryListAdapter : GalleryAdapter by lazy { GalleryAdapter(this) }
    private val accountMenuProvider: AccountMenuProvider by lazy { AccountMenuProvider(requireActivity().application, viewLifecycleOwner.lifecycleScope, galleryListViewModel) }

    @Inject lateinit var imgurAuthState: AuthState
    @Inject lateinit var imgurKtAuthRequest: AuthorizationRequest
    @Inject lateinit var imgurKtAuthService: AuthorizationService

    private val galleryListViewModel: GalleryViewModel by viewModelFactory()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindings<GalleryBindings>().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()
        initList()
    }

    private fun initMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(
            LanguageMenuProvider(),
            viewLifecycleOwner,
            Lifecycle.State.CREATED
        )
        menuHost.addMenuProvider(
            accountMenuProvider,
            viewLifecycleOwner,
            Lifecycle.State.CREATED
        )
    }

    private fun initList() {
        binding.listRecycler.adapter = galleryListAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.viewState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { viewState ->
                render(viewState)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.effects.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { effect ->
                trigger(effect)
            }
        }
    }

    private fun render(viewState: GalleryViewState) {
        viewState.galleryListPagingData?.let { galleryListAdapter.submitData(viewLifecycleOwner.lifecycle, it) }
    }

    private fun trigger(effect: UsfEffect) {
        when (effect) {
            is GalleryEffect.GalleryListItemClicked -> {
                goToAlbum(effect.albumHash)
            }
            is AccountMenuEffect.DoImgurAuthEffect -> {
                doImgurAuth()
            }
            is AccountMenuEffect.GoToMyAccountEffect -> {
                goToAlbum("dXXwXsa")
            }
        }
    }

    override fun onGalleryItemClick(item: GalleryItem) {
        galleryListViewModel.processEvent(GalleryEvent.GalleryListItemClickedEvent(item.id))
    }

    private fun goToAlbum(albumHash: String) {
        findNavController().navigate(com.fret.imgur_album.api.R.id.album_nav_graph, AlbumNavGraphArgs(albumHash).toBundle())
    }

    private val authResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.onAuthActivityResult.invoke(it)
        }
    }

    private fun doImgurAuth() {
        authResult.launch(imgurKtAuthService.getAuthorizationRequestIntent(imgurKtAuthRequest))
    }
}