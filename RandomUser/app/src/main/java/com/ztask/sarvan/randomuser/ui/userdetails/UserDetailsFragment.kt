package com.ztask.sarvan.randomuser.ui.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ztask.sarvan.randomuser.data.localdb.entity.RandomUserEntities
import com.ztask.sarvan.randomuser.databinding.FragmentUserDetailsBinding
import com.ztask.sarvan.randomuser.viewmodel.MainViewModel
import com.ztask.sarvan.randomuser.viewmodel.factory.MainViewModelFactory

class UserDetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewBinding: FragmentUserDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserDetailsBinding.inflate(inflater)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val json: String? = arguments?.getString("RESULTITEM")
        val item: RandomUserEntities = Gson().fromJson(json, RandomUserEntities::class.java)
        try {
            viewBinding.tvUserAgenew.text=item.age.toString() + " Years Old"
            viewBinding.tvCapitalValue.text=item.gender
            viewBinding.tvAreaValue.text=item.mobile
            viewBinding.tvRegionValue.text=item.emailid
            viewBinding.tvPopulationValue.text=item.dob?.substring(0, 10)
            viewBinding.tvNativeNameValue.text=
                item.streetNo.toString() + " " +
                        item.streetname + ", \n" +
                        item.state + ", \n" +
                        item.city + ", \n" +
                        item.country + ", \n" +
                        item.postalcode
            activity?.baseContext?.let {
                Glide.with(it)
                    .load(item.userimg)
                    .circleCrop()
                    .into(viewBinding.ivFlag)
            }
            activity?.baseContext?.let {
                Glide.with(it)
                    .load(item.userimg)
                    .into(viewBinding.ivFullFlag)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

}