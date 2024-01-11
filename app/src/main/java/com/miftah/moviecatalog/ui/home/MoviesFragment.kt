package com.miftah.moviecatalog.ui.home

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.miftah.moviecatalog.databinding.FragmentMoviesBinding
import com.miftah.moviecatalog.ui.ViewModelFactory

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: AdapterCardMovies

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRv()

        binding.edInputMovie.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                Log.d("TAG", "press")
                refreshData()
                return@setOnKeyListener true
            }
            false
        }

        viewModel.pagingData.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun refreshData() {
        val searchQuery = binding.edInputMovie.text.toString()
        viewModel.setSearchQuery(searchQuery)
    }

    private fun setUpRv() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = AdapterCardMovies()
        binding.rvMovies.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.rvMovies.layoutManager = layoutManager
        adapter.setOnClickCallback(object : AdapterCardMovies.OnClickListener {
            override fun onClickCard(id: String, posterUrl: String, year : String) {
                MoviesFragmentDirections.actionMoviesFragmentGraphToDetailFragment().apply {
                    this.imdbId = id
                    this.posterUrl = posterUrl
                    this.year = year
                    findNavController().navigate(this)
                }
            }
        })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}