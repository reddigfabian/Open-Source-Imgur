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
import androidx.navigation.fragment.findNavController
import com.fret.grocerydemo.R
import com.fret.grocerydemo.common.extensions.replaceQueryParam
import com.fret.grocerydemo.databinding.FragmentListBinding
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.kroger_api.KrogerService
import com.fret.grocerydemo.kroger_api.requests.AccessTokenRequest
import com.fret.grocerydemo.ui.list.items.ListItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
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

    private var newAccessToken : String? = null

    private val authenticator = Authenticator { _, response ->
        newAccessToken = krogerRepository.getAccessCode(AccessTokenRequest.ClientCredentialsAccessTokenRequest()).execute().body()?.access_token

        response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .authenticator(authenticator)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.kroger.com/v1/")
        .client(client)
        .build()

    val krogerService : KrogerService = retrofit.create(KrogerService::class.java)

    private val krogerRepository = KrogerRepository(krogerService)

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
            krogerRepository
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.items.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                listAdapter.submitData(it)
            }
        }
    }

    override fun onItemClick(item: ListItem) {
        findNavController().navigate(getDeepLinkUri(item.text))
    }

    private fun getDeepLinkUri(itemText : String) : Uri {
        val deeplinkStr = getString(R.string.deeplink_detail)
        val navArgDetail = getString(R.string.nav_arg_detail)
        return deeplinkStr.toUri().replaceQueryParam(navArgDetail, itemText)
    }
}