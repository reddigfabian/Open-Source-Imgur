package com.fret.grocerydemo.ui.list.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.grocerydemo.BuildConfig
import com.fret.grocerydemo.R
import com.fret.grocerydemo.common.extensions.replaceQueryParam
import com.fret.grocerydemo.databinding.FragmentListBinding
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.kroger_api.KrogerService
import com.fret.grocerydemo.kroger_api.requests.oauth2.AccessTokenRequest
import com.fret.grocerydemo.ui.list.adapters.ListAdapter
import com.fret.grocerydemo.ui.list.items.ListItem
import com.fret.grocerydemo.ui.list.viewmodels.ListViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.openid.appauth.*
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val TAG = "ListFragment"

class ListFragment : Fragment(), ListAdapter.ListItemClickListener {
    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val RC_AUTH = 9028357

    private val authConfig = AuthorizationServiceConfiguration(
        Uri.parse(BuildConfig.KROGER_OAUTH2_AUTH_CODE),
        Uri.parse(BuildConfig.KROGER_OAUTH2_ACCESS_TOKEN)
    )

    private var authState: AuthState = AuthState(authConfig)

    private lateinit var authService : AuthorizationService

    private val authRequest: AuthorizationRequest = AuthorizationRequest.Builder(
        authConfig, // the authorization service configuration
        BuildConfig.KROGER_CLIENT_ID, // the client ID, typically pre-registered and static
        ResponseTypeValues.CODE, // the response_type value: we want a code
        BuildConfig.DEEPLINK_REDIRECT.toUri()) // the redirect URI to which the auth response is sent
        .setScope("profile.compact cart.basic:write")
        .build()

    private val moshi = Moshi.Builder()
        .add(
            PolymorphicJsonAdapterFactory.of(AccessTokenRequest::class.java, "grant_type")
                .withSubtype(
                    AccessTokenRequest.ClientCredentialsAccessTokenRequest::class.java,
                    AccessTokenRequest.GrantType.client_credentials.name
                )
                .withSubtype(
                    AccessTokenRequest.AuthCodeAccessTokenRequest::class.java,
                    AccessTokenRequest.GrantType.authorization_code.name
                )
                .withSubtype(
                    AccessTokenRequest.RefreshTokenAccessTokenRequest::class.java,
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
        if (response.request.header("Authorization") != null) {
            return@Authenticator null
        }

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
        .baseUrl(BuildConfig.KROGER_API_BASE_URL)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        authService = AuthorizationService(context)
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
        doAuth()
//        findNavController().navigate(getDeepLinkUri(item.text))
    }

    private fun getDeepLinkUri(itemText : String) : Uri {
        val deeplinkStr = getString(R.string.deeplink_detail)
        val navArgDetail = getString(R.string.nav_arg_detail)
        return deeplinkStr.toUri().replaceQueryParam(navArgDetail, itemText)
    }

    private fun doAuth() {
        startActivityForResult(authService.getAuthorizationRequestIntent(authRequest), RC_AUTH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_AUTH && data != null) {
            val authResponse = AuthorizationResponse.fromIntent(data)
            authService.performTokenRequest(authResponse!!.createTokenExchangeRequest(), ClientSecretBasic(BuildConfig.KROGER_CLIENT_SECRET)
            ) { resp, ex ->
                authState.update(resp, ex)
                getProfileID()
            }
        }
    }

    private fun getProfileID() {
        authState.performActionWithFreshTokens(authService) { accessToken, idToken, ex ->
            if (ex != null) {
                return@performActionWithFreshTokens
            }
            accessToken?.let {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val id = krogerRepository.getProfileID("Bearer $it").data.id
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "ID : $id", Toast.LENGTH_LONG).show()
                        }
                    } catch (ex : Exception) {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "EX : ${ex.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}