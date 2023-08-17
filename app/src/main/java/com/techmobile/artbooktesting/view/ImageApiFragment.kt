package com.techmobile.artbooktesting.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.techmobile.artbooktesting.R
import com.techmobile.artbooktesting.adapter.ImageRecyclerAdapter
import com.techmobile.artbooktesting.databinding.FragmentImageApiBinding
import com.techmobile.artbooktesting.utils.Status
import com.techmobile.artbooktesting.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
     val imageRecyclerAdapter: ImageRecyclerAdapter
): Fragment(R.layout.fragment_image_api) {

    lateinit var viewModel: ArtViewModel
    private var fragmentBindings : FragmentImageApiBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBindings = binding
        subscribeToObserver()
        var job : Job? = null
        binding.searchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(100)
               it?.let {
                   if(it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())
                   }
               }
            }
        }
        binding.imageRecyclerView.adapter = imageRecyclerAdapter
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)
        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
    }
    private fun subscribeToObserver(){
        viewModel.imageList.observe( viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    val urls = it.data?.hits?.map { it->it.previewURL }
                    imageRecyclerAdapter.images = urls ?: listOf()
                    fragmentBindings?.progressBar?.visibility = View.GONE

                }
                Status.LOADING->{
                    fragmentBindings?.progressBar?.visibility = View.VISIBLE

                }
                Status.ERROR->{
                    Toast.makeText(requireContext(),it.message ?:  "Error", Toast.LENGTH_LONG).show()
                    fragmentBindings?.progressBar?.visibility = View.GONE

                }
                else -> {}
            }
        })
    }
}