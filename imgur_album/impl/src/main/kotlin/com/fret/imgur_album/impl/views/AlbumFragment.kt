package com.fret.imgur_album.impl.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.imgur_album.api.AlbumNavGraphArgs
import com.fret.imgur_album.impl.adapters.AlbumAdapter
import com.fret.imgur_album.impl.databinding.FragmentAlbumBinding
import com.fret.imgur_album.impl.di.AlbumBindings
import com.fret.imgur_album.impl.di.AlbumComponent
import com.fret.imgur_album.impl.usf.AlbumEvent
import com.fret.imgur_album.impl.usf.AlbumViewEffect
import com.fret.imgur_album.impl.viewmodels.AlbumViewModel
import com.fret.shared_menus.account.AccountMenuProvider
import com.fret.shared_menus.language.LanguageMenuProvider
import com.fret.utils.DaggerComponentOwner
import com.fret.utils.bindingViewModelFactory
import com.fret.utils.bindings
import com.fret.utils.fragmentComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AlbumFragment"

class AlbumFragment : Fragment(),
    DaggerComponentOwner {
    private var _binding: FragmentAlbumBinding? = null
    //This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override val daggerComponent: AlbumComponent by fragmentComponent { _, app ->
        app.bindings<AlbumComponent.ParentBindings>().detailComponentBuilder().create()
    }

    private val albumViewModel: AlbumViewModel by bindingViewModelFactory {
        AlbumNavGraphArgs.fromArgs(arguments).albumHash
    }

    private val albumAdapter : AlbumAdapter by lazy { AlbumAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindings<AlbumBindings>().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenu()

        binding.listRecycler.adapter = albumAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            albumViewModel.viewState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                albumAdapter.submitList(it.imageListItems)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            albumViewModel.viewEffects.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    AlbumViewEffect.AccountMenuClickedEffect -> Toast.makeText(
                        requireContext(),
                        "Account Menu Under Construction",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        albumViewModel.processEvent(AlbumEvent.ScreenLoadEvent)
    }

    private fun initMenu() {
        (requireActivity() as MenuHost).addMenuProvider(
            LanguageMenuProvider(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
//        (requireActivity() as MenuHost).addMenuProvider(
//            AccountMenuProvider {
//                albumViewModel.processEvent(AlbumEvent.AccountMenuClickedEvent)
//            },
//            viewLifecycleOwner,
//            Lifecycle.State.RESUMED
//        )
    }
}