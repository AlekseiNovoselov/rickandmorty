package com.lexaloris.rickandmorty.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lexaloris.rickandmorty.R
import com.lexaloris.rickandmorty.data.response.Character
import com.lexaloris.rickandmorty.domain.model.NetworkState

class CharacterAdapter(
    private val retryClick: () -> Unit,
    private val updatedAction: (size: Int, networkState: NetworkState?) -> Unit
) : PagedListAdapter<Character, RecyclerView.ViewHolder>(diffCallback) {

    private var networkState: NetworkState = NetworkState.RUNNING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.character_item_view -> CharacterItemViewHolder(view)
            R.layout.network_state_item_view -> NetworkStateViewHolder(view)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.character_item_view -> (holder as CharacterItemViewHolder).bind(getItem(position)!!)
            R.layout.network_state_item_view -> {
                (holder as NetworkStateViewHolder).bind(networkState, retryClick)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item_view
        } else {
            R.layout.character_item_view
        }
    }

    override fun getItemCount(): Int {
        updatedAction.invoke(super.getItemCount(), networkState)
        return super.getItemCount()
    }

    private fun hasExtraRow() = networkState != NetworkState.SUCCESS

    fun updateNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }
}
