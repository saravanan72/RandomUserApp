package com.ztask.me.randomuser.ui.userdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ztask.me.randomuser.data.localdb.ResultsItem
import com.ztask.me.randomuser.databinding.FragmentUserDetailsBinding
import com.ztask.me.randomuser.utils.autoCleared

class UserDetailsFragment : Fragment() {

    private var binding: FragmentUserDetailsBinding by autoCleared()
    private var resultItem: ResultsItem ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultItem = arguments?.getParcelable("RESULTITEM")
        bindCharacter(resultItem)
    }


    private fun bindCharacter(item: ResultsItem?) {
        binding.tvname.text = "${item?.name?.title}. ${item?.name?.first} ${item?.name?.last}"
        binding.tvmobile.text = "${item?.phone}"
        binding.tvStreet.text = "${item?.location?.street?.number}, ${item?.location?.street?.name}"
        binding.tvcity.text = "${item?.location?.city}"
        binding.tvcountry.text = "${item?.location?.country}"
        Glide.with(binding.root)
                .load(item?.picture?.large)
                .transform(CircleCrop())
                .into(binding.ivFlag)
    }

}