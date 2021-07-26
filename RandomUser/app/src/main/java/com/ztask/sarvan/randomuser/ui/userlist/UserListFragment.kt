package com.ztask.sarvan.randomuser.ui.userlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.ztask.sarvan.randomuser.R
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities
import com.ztask.sarvan.randomuser.databinding.FragmentUserListBinding
import com.ztask.sarvan.randomuser.viewmodel.MainViewModel
import com.ztask.sarvan.randomuser.viewmodel.factory.MainViewModelFactory

class UserListFragment : Fragment(), UserListAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewBinding: FragmentUserListBinding
    private lateinit var adapter: UserListAdapter
    var userList: List<RandomUserEntities> = ArrayList<RandomUserEntities>()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserListBinding.inflate(inflater)
        viewModel = ViewModelProvider(this@UserListFragment, MainViewModelFactory(requireContext())).get(MainViewModel::class.java)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        if (viewBinding.searchedittext.text.toString().length > 0) {
            setAdapterFilter(viewBinding.searchedittext.text.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRandomUserDetails()
        viewModel.userApiResponse.observe(viewLifecycleOwner, Observer {

            if (resources.displayMetrics.widthPixels > resources.displayMetrics.heightPixels)
                viewBinding.recyclerview.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            else
                viewBinding.recyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            userList = it
            adapter = UserListAdapter(this@UserListFragment, userList)
            viewBinding.recyclerview.adapter = adapter
        })

        viewBinding.searchedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val txt = s.toString()
                setAdapterFilter(txt)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun setAdapterFilter(txt: String) {
        try {

            var tempList: MutableList<RandomUserEntities> = mutableListOf<RandomUserEntities>()
            if (txt.length > 0) {
                for (item in userList) {
                    if ((item.first.toString().toLowerCase()).contains(txt.toLowerCase()) ||
                            (item.last.toString().toLowerCase()).contains(txt.toLowerCase())) {
                        tempList.add(item)
                    }
                }
                adapter.setFilter(tempList)
            } else
                adapter.setFilter(userList)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun OnItemClick(item: RandomUserEntities?) {
        val json = Gson().toJson(item)
        findNavController().navigate(
                R.id.action_userListFragment_to_userDetailsFragment,
                bundleOf("RESULTITEM" to json)
        )
    }

}