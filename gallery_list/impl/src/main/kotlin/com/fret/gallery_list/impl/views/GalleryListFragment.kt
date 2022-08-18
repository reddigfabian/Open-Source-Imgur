package com.fret.gallery_list.impl.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fret.gallery_list.impl.GalleryListViewModelFactory
import com.fret.gallery_list.impl.adapters.GalleryListAdapter
import com.fret.gallery_list.impl.databinding.FragmentGalleryListBinding
import com.fret.gallery_list.impl.di.GalleryListBindings
import com.fret.gallery_list.impl.di.GalleryListComponent
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.usf.GalleryListEffect
import com.fret.gallery_list.impl.usf.GalleryListEvent
import com.fret.gallery_list.impl.usf.GalleryListViewState
import com.fret.gallery_list.impl.viewmodels.GalleryListViewModel
import com.fret.imgur_album.api.AlbumNavGraphArgs
import com.fret.shared_menus.account.AccountMenuProvider
import com.fret.shared_menus.account.usf.AccountMenuEffect
import com.fret.shared_menus.account.usf.AccountMenuEvent
import com.fret.shared_menus.account.usf.AccountMenuViewState
import com.fret.shared_menus.language.LanguageMenuProvider
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindingViewModelFactory
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent
import kotlinx.coroutines.launch
import net.openid.appauth.*
import javax.inject.Inject

private const val TAG = "GalleryListFragment"

class GalleryListFragment : Fragment(),
    DaggerComponentOwner,
    GalleryListAdapter.GalleryListItemClickListener {
    private var _binding: FragmentGalleryListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override val daggerComponent: GalleryListComponent by fragmentComponent { _, app ->
        app.bindings<GalleryListComponent.ParentBindings>().listComponentBuilder().create()
    }

    private val galleryListAdapter : GalleryListAdapter by lazy { GalleryListAdapter(this) }
    private val accountMenuProvider: AccountMenuProvider by lazy { AccountMenuProvider(galleryListViewModel) }

    @Inject lateinit var imgurAuthState: AuthState
    @Inject lateinit var imgurKtAuthRequest: AuthorizationRequest
    @Inject lateinit var imgurKtAuthService: AuthorizationService

    private val galleryListViewModel: GalleryListViewModel by bindingViewModelFactory()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindings<GalleryListBindings>().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGalleryListBinding.inflate(inflater, container, false)
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
            galleryListViewModel.viewEffects.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { viewEffect ->
                when (viewEffect) {
                    is GalleryListEffect.GalleryListItemClicked -> goToAlbum(viewEffect.albumHash)
                    is AccountMenuEffect.DoImgurAuthEffect -> {
                        doImgurAuth()
                    }
                    is AccountMenuEffect.GoToMyAccountEffect -> {
                        goToAlbum("dXXwXsa")
                    }
                }
            }
        }
    }

    private fun render(viewState: GalleryListViewState) {
        viewState.galleryListPagingData?.let { galleryListAdapter.submitData(viewLifecycleOwner.lifecycle, it) }
    }

    override fun onGalleryItemClick(item: GalleryListItem) {
        galleryListViewModel.processEvent(GalleryListEvent.GalleryListItemClickedEvent(item.id))
    }

    private fun goToAlbum(albumHash: String) {
        findNavController().navigate(com.fret.imgur_album.api.R.id.album_nav_graph, AlbumNavGraphArgs(albumHash).toBundle())
    }

    private val authResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.let { intent ->
                    val resp = AuthorizationResponse.fromIntent(intent)
                    val ex = AuthorizationException.fromIntent(intent)
                    Log.d(TAG, "imgurAuthState: $imgurAuthState")
                    imgurAuthState.update(resp, ex)
                    if (ex != null) {
                        Log.e(TAG, "AppAuth Activity result returned exception", ex)
                    } else {
                        Log.d(TAG, "AppAuth Activity result returned successfully")
                        viewLifecycleOwner.lifecycleScope.launch {
                            galleryListViewModel.processEvent(AccountMenuEvent.AuthSucceededEvent)
                        }
                    }
                } ?: run {
                    Log.e(TAG, "AppAuth Activity result returned with a null data Intent")
                }
            }
            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "AppAuth Activity result returned due to user cancellation")
            }
            else -> {
                Log.e(TAG, "AppAuth Activity result returned with unexpected Activity result")
            }
        }
    }

    private fun doImgurAuth() {
        authResult.launch(imgurKtAuthService.getAuthorizationRequestIntent(imgurKtAuthRequest))
    }
}