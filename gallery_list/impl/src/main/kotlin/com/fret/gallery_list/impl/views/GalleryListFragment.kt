package com.fret.gallery_list.impl.views

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.fret.gallery_list.impl.adapters.GalleryListAdapter
import com.fret.gallery_list.impl.databinding.FragmentGalleryListBinding
import com.fret.gallery_list.impl.di.GalleryListBindings
import com.fret.gallery_list.impl.di.GalleryListComponent
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.usf.GalleryListEvent
import com.fret.gallery_list.impl.usf.GalleryViewEffect
import com.fret.gallery_list.impl.viewmodels.GalleryListViewModel
import com.fret.imgur_album.api.AlbumNavGraphArgs
import com.fret.menus.account.AccountMenuProvider
import com.fret.menus.language.LanguageMenuProvider
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindingViewModelFactory
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent
import kotlinx.coroutines.launch
import net.openid.appauth.*
import javax.inject.Inject

private const val TAG = "ListFragment"

class GalleryListFragment : Fragment(), DaggerComponentOwner, GalleryListAdapter.GalleryListItemClickListener {
    private var _binding: FragmentGalleryListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override val daggerComponent: GalleryListComponent by fragmentComponent { _, app ->
        app.bindings<GalleryListComponent.ParentBindings>().listComponentBuilder().create()
    }

    private val galleryListAdapter : GalleryListAdapter by lazy { GalleryListAdapter(this) }

    @Inject lateinit var imgurKtAuthRequest: AuthorizationRequest
    @Inject lateinit var imgurKtAuthService: AuthorizationService
    @Inject lateinit var imgurAuthState: AuthState

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
        (requireActivity() as MenuHost).addMenuProvider(
            LanguageMenuProvider(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        (requireActivity() as MenuHost).addMenuProvider(
            AccountMenuProvider {
                galleryListViewModel.processEvent(GalleryListEvent.AccountMenuClickedEvent)
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun initList() {
        binding.listRecycler.adapter = galleryListAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.viewState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { viewState ->
                viewState.galleryListPagingData?.let { galleryListAdapter.submitData(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.viewEffects.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect { viewEffect ->
                when (viewEffect) {
                    is GalleryViewEffect.GalleryListItemClicked -> goToAlbum(viewEffect.albumHash)
                    GalleryViewEffect.AccountMenuClickedEffect -> {
                        if (!imgurAuthState.isAuthorized) {
                            doImgurAuth()
                        } else {
                            galleryListViewModel.test()
                        }
                    }
                }
            }
        }
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
                    imgurAuthState.update(resp, ex)
                    if (ex != null) {
                        Log.e(TAG, "AppAuth Activity result returned exception", ex)
                    } else {
                        Log.d(TAG, "AppAuth Activity result returned successfully")
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