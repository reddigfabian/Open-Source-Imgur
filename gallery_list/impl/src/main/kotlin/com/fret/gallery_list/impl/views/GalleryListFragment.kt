package com.fret.gallery_list.impl.views

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fret.gallery_list.impl.R
import com.fret.gallery_list.impl.adapters.GalleryListAdapter
import com.fret.gallery_list.impl.databinding.FragmentGalleryListBinding
import com.fret.gallery_list.impl.di.GalleryListBindings
import com.fret.gallery_list.impl.di.GalleryListComponent
import com.fret.gallery_list.impl.items.GalleryListItem
import com.fret.gallery_list.impl.viewmodels.GalleryListViewModel
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindingViewModelFactory
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.openid.appauth.*
import javax.inject.Inject

private const val TAG = "ListFragment"

class GalleryListFragment : Fragment(), DaggerComponentOwner,  GalleryListAdapter.GalleryListItemClickListener {
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
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                myImagesClick()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initList() {
        binding.listRecycler.adapter = galleryListAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.imgurItems.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                galleryListAdapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryListViewModel.toast.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGalleryItemClick(item: GalleryListItem) {
        val deeplinkStr = getString(com.fret.gallery_detail.api.R.string.deeplink_detail)
        val navArgDetail = getString(com.fret.gallery_detail.api.R.string.nav_arg_detail)
        findNavController().navigate(deeplinkStr.toUri().replaceQueryParam(navArgDetail, item.id))
    }

    // TODO: Move to utils module
    private fun Uri.replaceQueryParam(key : String, newValue : String?) : Uri {
        val queryParameterNames = queryParameterNames
        val newUriBuilder = buildUpon().clearQuery()
        queryParameterNames.forEach {
            newUriBuilder.appendQueryParameter(it,
                when (it) {
                    key -> newValue
                    else -> getQueryParameter(it)
                }
            )
        }
        return newUriBuilder.build()
    }

    fun myImagesClick() {
        if (!imgurAuthState.isAuthorized) {
            doImgurAuth()
        } else {
            galleryListViewModel.test()
        }
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