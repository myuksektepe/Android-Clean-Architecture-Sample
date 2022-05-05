package murat.cleanarchitecture.sample.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * TODO BaseFragment?
 */


abstract class BaseFragment<T : BaseViewModel, VB : ViewDataBinding> : Fragment() {

    abstract val layoutRes: Int
    abstract val viewModel: T
    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun observeViewModel()
    open fun initBinding() {
        this._binding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this._binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        initBinding()
        observeViewModel()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}