package com.fret.gallery_list.impl.views

//import com.fret.ktxauth.*
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.gallery_list.impl.*
import com.fret.gallery_list.impl.adapters.ImgurListAdapter
import com.fret.gallery_list.impl.databinding.FragmentListBinding
import com.fret.gallery_list.impl.items.ImgurListItem
import com.fret.gallery_list.impl.viewmodels.ListViewModel
import com.fret.imgur_api.api.ImgurRepository
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

private const val TAG = "ListFragment"

class ListFragment : Fragment(), DaggerComponentOwner,  ImgurListAdapter.ImgurListItemClickListener {
    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override val daggerComponent: ListComponent by fragmentComponent { _, app ->
        app.bindings<ListComponent.ParentBindings>().listComponentBuilder().create()
    }

    private val imgurListAdapter : ImgurListAdapter by lazy { ImgurListAdapter(this) }

    @Inject lateinit var imgurKtAuthRequest: AuthorizationRequest
    @Inject lateinit var imgurKtAuthService: AuthorizationService
    @Inject lateinit var imgurAuthState: AuthState

    private val listViewModel: ListViewModel by bindingViewModelFactory(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindings<ListBindings>().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
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
        binding.listRecycler.adapter = imgurListAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.imgurItems.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                imgurListAdapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.toast.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        // TODO: This toast is a reminder to replace initialState String with savedStateHandle and to make a more flexible code generator
        Toast.makeText(requireContext(), listViewModel.initialState, Toast.LENGTH_SHORT).show()
    }

    override fun onImgurItemClick(item: ImgurListItem) {

    }

    fun myImagesClick() {
        if (!imgurAuthState.isAuthorized) {
            doImgurAuth()
        } else {
            listViewModel.test()
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