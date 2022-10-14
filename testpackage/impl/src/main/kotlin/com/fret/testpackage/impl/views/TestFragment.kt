package com.fret.testpackage.impl.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.fret.testpackage.impl.databinding.FragmentTestBinding
import com.fret.testpackage.impl.di.TestBindings
import com.fret.testpackage.impl.di.TestComponent
import com.fret.testpackage.impl.usf.TestEvent
import com.fret.testpackage.impl.viewmodels.TestViewModel
import com.fret.utils.di.DaggerComponentOwner
import com.fret.utils.di.bindings
import com.fret.utils.di.fragmentComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "AlbumFragment"

class TestFragment : Fragment(), DaggerComponentOwner {
    private var _binding: FragmentTestBinding? = null
    //This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override val daggerComponent: TestComponent by fragmentComponent { _, app ->
        app.bindings<TestComponent.ParentBindings>().testComponentBuilder().create()
    }

    private val testViewModel: TestViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindings<TestBindings>().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        binding.testEditText.doOnTextChanged { text, start, before, count ->
            testViewModel.processEvent(TestEvent.TextChangeEvent(text.toString()))
        }
        binding.testButton.setOnClickListener {
            testViewModel.processEvent(TestEvent.SubmitPressEvent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            testViewModel.viewState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                binding.testTextView.text = it.showText
                binding.testButton.setText(it.buttonText)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            testViewModel.viewEffects.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        testViewModel.processEvent(TestEvent.ScreenLoadEvent)
    }
}