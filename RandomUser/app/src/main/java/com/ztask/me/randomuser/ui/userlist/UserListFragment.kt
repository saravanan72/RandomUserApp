package com.ztask.me.randomuser.ui.userlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ztask.me.randomuser.R
import com.ztask.me.randomuser.data.localdb.ResultResponseElement
import com.ztask.me.randomuser.data.localdb.ResultsItem
import com.ztask.me.randomuser.databinding.FragmentUserListBinding
import com.ztask.me.randomuser.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment(), UserListAdapter.UserItemListener {
    private var binding: FragmentUserListBinding by autoCleared()
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var adapter: UserListAdapter

    private var resultItemList: List<ResultsItem> =
            ArrayList<ResultsItem>()
    private lateinit var filterResultItemList: List<ResultsItem>

    private lateinit var latitude: String
    private lateinit var longitude: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        latitude = arguments?.get("latitude") as String
        longitude = arguments?.get("longitude") as String
        setupRecyclerView()
        setupObservers()
        searchDataObserver()
        apiCall()
    }

    private fun setupRecyclerView() {
        adapter = UserListAdapter(this)
        binding.recyclerView.layoutManager =  StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        )
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.users.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    val response : ResultResponseElement? = it.data
                    val resultsItem: ArrayList<ResultsItem> = response?.results as ArrayList<ResultsItem>
                    if (resultsItem.size > 0) adapter.setItems(resultsItem)
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->
                    binding.progress.visibility = View.VISIBLE
            }
        })
    }


    override fun onClickedUser(item: ResultsItem) {

        findNavController().navigate(
                R.id.action_userListFragment_to_userDetailsFragment,
                bundleOf("RESULTITEM" to item)
        )
    }




    private fun apiCall() {
        if (isInternetAvailable(requireActivity())) {
            showLoadingScreen(requireActivity())
            viewModel.getWeatherDataLatLong(latitude, longitude)
            viewModel.res.observe(viewLifecycleOwner, {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it.data.let { res ->
                            binding.toolbar.tvPlace.text =
                                    res?.main?.temp?.let { it1 ->
                                        String.format(
                                                "%.2f",
                                                convertKelvinToCelsius(it1)
                                        ).toDouble()
                                                .toString() + 0x00B0.toChar() + " " + res.name
                                    }
                            binding.toolbar.tvDescription.text =
                                    res?.weather!![0].description
                            dismissLoadingScreen()
                            binding.llSearch.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.progress.visibility = View.GONE
                        }
                    }
                    Resource.Status.LOADING -> {
                        binding.progress.visibility = View.VISIBLE
                    }
                    Resource.Status.ERROR -> {
                        dismissLoadingScreen()
                        binding.progress.visibility = View.GONE
                        Toast.makeText(
                                requireActivity(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        } else {
            Toast.makeText(
                    requireActivity(),
                    "No internet. Please try again after sometime.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun convertKelvinToCelsius(kelvin: Double): Double {
        return (kelvin - 273.15)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.SEARCH_DATA, binding.searchBar.text.toString())
    }

    private fun searchDataObserver() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (binding.searchBar.length() > 0) {
                    binding.closeSearch.visibility = View.VISIBLE
                } else {
                    binding.closeSearch.visibility = View.GONE
                    hideKeyboard()
                }
                filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding.closeSearch.setOnClickListener {
            hideKeyboard()
            binding.searchBar.setText("")
            binding.searchBar.isFocusable = false
            binding.searchBar.isFocusableInTouchMode = true
            adapter.notifyDataSetChanged()
        }
    }

    private fun filter(text: String) {
        filterResultItemList = ArrayList<ResultsItem>()
        for (i in resultItemList.indices) {
            if ((resultItemList[i].name?.first.toString().toLowerCase().contains(text.toString().toLowerCase()))
                    || (resultItemList[i].name?.last.toString().toLowerCase().contains(text.toString().toLowerCase()))) {
                (filterResultItemList as ArrayList<ResultsItem>).add(
                        resultItemList[i]
                )
            }
        }
        adapter.filterList(filterResultItemList as ArrayList<ResultsItem>)
        if (filterResultItemList.isNotEmpty()) {
            binding.noResultFound.visibility = View.GONE
        } else {
            binding.noResultFound.visibility = View.VISIBLE
        }
    }

}