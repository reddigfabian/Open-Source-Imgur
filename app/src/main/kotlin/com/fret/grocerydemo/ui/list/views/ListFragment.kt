package com.fret.grocerydemo.ui.list.views

import android.net.Uri
import com.fret.grocerydemo.ui.list.adapters.ListAdapter
import com.fret.grocerydemo.ui.list.viewmodels.ListViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.grocerydemo.R
import com.fret.grocerydemo.databinding.FragmentListBinding
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.kroger_api.KrogerRepositoryImpl
import com.fret.grocerydemo.kroger_api.KrogerService
import com.fret.grocerydemo.kroger_api.requests.AccessTokenRequest
import com.fret.grocerydemo.ui.list.items.ListItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "ListFragment"

class ListFragment : Fragment(), ListAdapter.ListItemClickListener {
    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(AccessTokenRequest::class.java, "grant_type")
                .withSubtype(AccessTokenRequest.ClientCredentialsAccessTokenRequest::class.java,
                    AccessTokenRequest.GrantType.client_credentials.name
                )
                .withSubtype(AccessTokenRequest.AuthCodeAccessTokenRequest::class.java,
                    AccessTokenRequest.GrantType.authorization_code.name
                )
                .withSubtype(AccessTokenRequest.RefreshTokenAccessTokenRequest::class.java,
                    AccessTokenRequest.GrantType.refresh_token.name
                )
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.kroger.com/v1/")
        .client(client)
        .build()

    val krogerService : KrogerService = retrofit.create(KrogerService::class.java)

    private val krogerRepository : KrogerRepository = KrogerRepositoryImpl(krogerService)

    private val listViewModel : ListViewModel by viewModels {
        ListViewModel.Factory(krogerRepository)
    }
    private val listAdapter : ListAdapter by lazy { ListAdapter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listRecycler.adapter = listAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.items.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                listAdapter.submitData(it)
            }
        }
    }

    override fun onItemClick(item: ListItem) {
        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                krogerRepository.getAuthCode(
//                    KrogerApiScope.PRODUCT_COMPACT,
//                    "grocerydemoapp-01a99ad9043b5fb2bd856b5805c58a405307789012342318835",
//                    getString(R.string.deeplink_list_base)
//                )
//            } catch (ex : Exception) {
//                Log.e(TAG, "onItemClick: ", )
//            }

            try {
                val t = krogerRepository.getAccessCode(AccessTokenRequest.ClientCredentialsAccessTokenRequest())
                Log.d(TAG, "onItemClick: ${t.access_token}")
            } catch (ex : Exception) {
                Log.e(TAG, "onItemClick: ", ex)
            }
        }
//        findNavController().navigate(getDeepLinkUri(item.text))
    }

    private fun getDeepLinkString(itemText : String) : String {
        val deeplinkStr = getString(R.string.deeplink_detail)
        val navArgDetail = getString(R.string.nav_arg_detail)
        return deeplinkStr.replace("{${navArgDetail}}", itemText)
    }

    private fun getDeepLinkUri(itemText : String) : Uri {
        val deeplinkStr = getString(R.string.deeplink_detail)
        val navArgDetail = getString(R.string.nav_arg_detail)
        return deeplinkStr.toUri().replaceQueryParam(navArgDetail, itemText)
    }

    // TODO: Move to utils module
    private fun Uri.replaceQueryParam(key : String, newValue : String) : Uri {
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
}