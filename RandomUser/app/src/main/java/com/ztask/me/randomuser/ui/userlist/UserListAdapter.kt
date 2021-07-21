package com.ztask.me.randomuser.ui.userlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ztask.me.randomuser.data.localdb.ResultResponseElement
import com.ztask.me.randomuser.data.localdb.ResultsItem
import com.ztask.me.randomuser.databinding.ItemCardBinding

class UserListAdapter (private val listener: UserItemListener) : RecyclerView.Adapter<UserViewHolder>() {

    interface UserItemListener {
        fun onClickedUser(item: ResultsItem)
    }

    private var items = ArrayList<ResultsItem>()

    fun setItems(items: ArrayList<ResultsItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun filterList(resultList: ArrayList<ResultsItem>) {
        this.items = resultList;
        notifyDataSetChanged();
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: ItemCardBinding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(items[position])
}

class UserViewHolder(private val itemBinding: ItemCardBinding, private val listener: UserListAdapter.UserItemListener) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

    private lateinit var user: ResultsItem

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ResultsItem) {
        this.user = item
        itemBinding.ivCountryName.text = "${item.name?.title}. ${item.name?.first} ${item.name?.last}"
        Glide.with(itemBinding.root)
                .load(item.picture?.large)
                .transform(CircleCrop())
                .into(itemBinding.ivFlag)
    }

    override fun onClick(v: View?) {
        listener.onClickedUser(user)
    }
}