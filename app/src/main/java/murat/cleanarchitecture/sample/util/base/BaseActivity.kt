package murat.cleanarchitecture.sample.util.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import murat.cleanarchitecture.sample.util.ResultState
import sample.R
import java.util.*

/**
 * TODO BaseActivity?
 */

abstract class BaseActivity<T : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {

    abstract val layoutRes: Int
    abstract val viewModel: T
    abstract var viewLifecycleOwner: LifecycleOwner
    private var _binding: B? = null
    val binding get() = _binding!!

    open fun initBinding() {
        this._binding?.lifecycleOwner = this
        viewLifecycleOwner = this
    }

    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.inflate(layoutInflater, layoutRes, null, false)
        setContentView(_binding!!.root)

        initBinding()
        observeViewModel()
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeLoadingAndError() {
        viewModel.loadingErrorState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.SUCCESS -> {
                    hideLoading()
                }
                is ResultState.LOADING -> {
                    showLoading()
                }
                is ResultState.FAIL -> {
                    hideLoading()
                    showErrorDailog(it.errorTitle, it.buttonTitle, it.callback)
                }
            }
        }
    }

    private val loadingAlertDialog by lazy {
        this.let {
            Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
            }
        }
    }

    private fun showLoading() = loadingAlertDialog.show()
    private fun hideLoading() = loadingAlertDialog.hide()


    fun showErrorDailog(
        message: String?,
        buttonMessage: String?,
        callback: () -> Unit? = {}
    ) {
        this.let {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle(getString(R.string.warning))
            alertDialog.setMessage(message)

            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, buttonMessage
            ) { dialog, which -> callback }

            val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
        }
    }

    fun randomColorHex(): String {
        val random = Random()
        val nextInt: Int = random.nextInt(0xffffff + 1)
        val colorCode = String.format("#%06x", nextInt)

        return colorCode
    }
}