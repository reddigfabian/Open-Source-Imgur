package com.fret.list.impl.ui.list.views

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fret.list.databinding.FragmentListBinding
import com.fret.list.impl.ui.list.adapters.ListAdapter
import com.fret.list.impl.ui.list.items.ListItem
import com.fret.list.impl.ui.list.viewmodels.ListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tangle.viewmodel.fragment.tangleViewModel


class ListFragment : Fragment(), ListAdapter.ListItemClickListener {
    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val listViewModel : ListViewModel by tangleViewModel()

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
        val deeplinkStr = getString(com.fret.detial.api.R.string.deeplink_detail)
        val navArgDetail = getString(com.fret.detial.api.R.string.nav_arg_detail)
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