package com.fret.grocerydemo.ui.list.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.grocerydemo.common.ViewModelFactory
import com.fret.grocerydemo.common.getViewModel
import com.fret.grocerydemo.databinding.FragmentListBinding
import com.fret.grocerydemo.di.ApplicationScope
import com.fret.grocerydemo.ui.list.adapters.ListAdapter
import com.fret.grocerydemo.ui.list.viewmodels.ListViewModel
import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFragment : Fragment() {

    @Inject
    internal lateinit var factory: ViewModelFactory<ListViewModel>

    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val listViewModel : ListViewModel by getViewModel { factory }

    private val listAdapter : ListAdapter by lazy { ListAdapter() }

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
}