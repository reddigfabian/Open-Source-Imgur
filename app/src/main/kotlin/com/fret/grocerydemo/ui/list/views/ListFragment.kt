package com.fret.grocerydemo.ui.list.views

import android.net.Uri
import com.fret.grocerydemo.ui.list.adapters.ListAdapter
import com.fret.grocerydemo.ui.list.viewmodels.ListViewModel
import android.os.Bundle
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
import com.fret.grocerydemo.databinding.FragmentListBinding
import com.fret.grocerydemo.kroger_api.KrogerRepository
import com.fret.grocerydemo.kroger_api.KrogerRepositoryImpl
import com.fret.grocerydemo.kroger_api.KrogerService
import com.fret.grocerydemo.ui.list.items.ListItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ListFragment : Fragment(), ListAdapter.ListItemClickListener {
    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    var client: OkHttpClient = OkHttpClient.Builder()
//    .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.kroger.com/v1/")
        .client(client)
        .build()

    val krogerService : KrogerService = retrofit.create(KrogerService::class.java)

//    private val retrofit = RetroFit
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
        val deeplinkStr = getString(R.string.deeplink_detail)
        val navArgDetail = getString(R.string.nav_arg_detail)
        findNavController().navigate(deeplinkStr.toUri().replaceQueryParam(navArgDetail, item.text))
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