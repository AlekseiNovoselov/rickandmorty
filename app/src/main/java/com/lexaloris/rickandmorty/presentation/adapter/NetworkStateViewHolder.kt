package com.lexaloris.rickandmorty.presentation.adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lexaloris.rickandmorty.R
import com.lexaloris.rickandmorty.domain.model.NetworkState

class NetworkStateViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val progressBar = parentView.findViewById<ProgressBar>(R.id.progress_bar)
    private val retryButton = parentView.findViewById<Button>(R.id.retry_button)
    private val errorMessageView = parentView.findViewById<TextView>(R.id.error_message)

    fun bind(networkState: NetworkState, retryClick: () -> Unit) {
        hideViews()
        setVisibleRightViews(networkState, itemView.context)
        retryButton.setOnClickListener { retryClick.invoke() }
    }

    private fun hideViews() {
        retryButton.visibility = View.GONE
        errorMessageView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    private fun setVisibleRightViews(networkState: NetworkState, context: Context) {
        when (networkState) {
            NetworkState.FAILED -> {
                retryButton.visibility = View.VISIBLE
                errorMessageView.visibility = View.VISIBLE
                errorMessageView.text = context.getString(R.string.error)
            }
            NetworkState.RUNNING -> {
                progressBar.visibility = View.VISIBLE
            }
            else -> {}
        }
    }
}
