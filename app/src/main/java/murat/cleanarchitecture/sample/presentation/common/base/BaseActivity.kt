package murat.cleanarchitecture.sample.presentation.common.base

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
import dagger.hilt.android.AndroidEntryPoint
import murat.cleanarchitecture.sample.domain.model.ResultData
import sample.R
import java.util.*

/**
 * TODO BaseActivity?
 */

abstract class BaseActivity<T : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {

    // Binding işlemi için ilgili ekranın layout'u
    abstract val layoutRes: Int

    // İlgili UI'ın viewModel'i
    abstract val viewModel: T

    // Binding için lifecycleowner değişkeni
    abstract var viewLifecycleOwner: LifecycleOwner

    // binding'i yönetebilmek için geçici _binding değişkeni
    private var _binding: B? = null

    // Geçici _binding değişkeninin içi oldurulmuş, non-null hali
    val binding get() = _binding!!

    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        observeViewModel()
    }

    private fun initBinding() {
        // binding'e lifeCycleOwner tanımlaması
        this._binding?.lifecycleOwner = this

        // ...
        viewLifecycleOwner = this

        // Binding işlemi
        _binding = DataBindingUtil.inflate(layoutInflater, layoutRes, null, false)
        setContentView(_binding!!.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Acitivity destroy olduğunda binding'in içini boşalt
        _binding = null
    }

    private fun observeLoadingAndError() {
        viewModel.loadingErrorState.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Success -> {
                    hideLoading()
                }
                is ResultData.Loading -> {
                    showLoading()
                }
                is ResultData.Failed -> {
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

    fun randomColorHex() : String{
        val random = Random()
        val nextInt: Int = random.nextInt(0xffffff + 1)
        val colorCode = String.format("#%06x", nextInt)

        return colorCode
    }
}