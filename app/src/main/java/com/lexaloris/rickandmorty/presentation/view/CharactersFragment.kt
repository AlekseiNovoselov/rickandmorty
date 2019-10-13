package com.lexaloris.rickandmorty.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.lexaloris.rickandmorty.R
import com.lexaloris.rickandmorty.di.CharactersComponent
import com.lexaloris.rickandmorty.domain.model.NetworkState
import com.lexaloris.rickandmorty.presentation.adapter.CharacterAdapter
import com.lexaloris.rickandmorty.presentation.viewmodel.CharactersViewModel
import javax.inject.Inject

class CharactersFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CharactersViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressView: ProgressBar
    private lateinit var infoContainer: View
    private lateinit var errorMessageView: TextView
    private lateinit var retryButton: Button

    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CharactersComponent.init().inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CharactersViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_characters, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        progressView = view.findViewById(R.id.progress)
        infoContainer = view.findViewById(R.id.info_container)
        retryButton = view.findViewById(R.id.retry_button)
        errorMessageView = view.findViewById(R.id.error_message)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservables()
        initializeRecyclerView()
        retryButton.setOnClickListener { viewModel.refreshAllList() }
    }

    private fun initializeObservables() {
        viewModel.networkState.observe(this, Observer { adapter.updateNetworkState(it) })
        viewModel.characters.observe(this, Observer { adapter.submitList(it) })
    }

    private fun initializeRecyclerView() {
        adapter = CharacterAdapter(
            retryClick = { viewModel.refreshFailedRequest() },
            updatedAction = updateListAction()
        )
        recyclerView.adapter = adapter
    }

    private fun updateListAction(): (Int, NetworkState?) -> Unit {
        return { itemsCount, networkState ->
            updateProgressBar(itemsCount, networkState)
            updateEmptyView(itemsCount, networkState)
        }
    }

    private fun updateProgressBar(itemsCount: Int, networkState: NetworkState?) {
        val shouldShowProgressBar = itemsCount == 0 && networkState == NetworkState.RUNNING
        progressView.visibility = if (shouldShowProgressBar) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun updateEmptyView(itemsCount: Int, networkState: NetworkState?) {
        retryButton.visibility = View.GONE
        errorMessageView.visibility = View.GONE
        if (itemsCount != 0) {
            return
        }
        when (networkState) {
            NetworkState.SUCCESS -> showEmptyResult()
            NetworkState.FAILED -> showError()
            else -> {}
        }
    }

    private fun showEmptyResult() {
        infoContainer.visibility = View.VISIBLE
        errorMessageView.text = getString(R.string.empty_result)
        errorMessageView.visibility = View.VISIBLE
        retryButton.visibility = View.GONE
    }

    private fun showError() {
        infoContainer.visibility = View.VISIBLE
        errorMessageView.text = getString(R.string.error)
        errorMessageView.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE
    }
}
