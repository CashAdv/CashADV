package app.cashadvisor.common.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Базовый фрагмент, позволяет привести к единому виду все реализации фрагментов, работает в связке с BaseViewModel.
 *
 * При реализации потребуется реализовать абстрактные методы для инициализации вью: configureViews()  и слушатели событий: subscribe()
 * так же потребуется реализовать абстрактное свойство viewModel: VM, то есть override val viewModel by viewModels&lt;SomeViewModel&gt;()
 */
abstract class BaseFragment<VB: ViewBinding, VM: BaseViewModel> (
    private val inflate: Inflate<VB>
): Fragment() {

        private var _binding: VB? = null
        protected val binding: VB get() = _binding!!

        abstract val viewModel: VM

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: android.os.Bundle?
        ): android.view.View? {
            _binding = inflate.invoke(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            onConfigureViews()
            onSubscribe()
        }

        abstract fun onConfigureViews()

        abstract fun onSubscribe()

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}