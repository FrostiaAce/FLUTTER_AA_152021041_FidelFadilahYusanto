package com.miftah.moviecatalog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.miftah.moviecatalog.core.data.domain.Bookmark
import com.miftah.moviecatalog.databinding.FragmentBookmarkBinding
import com.miftah.moviecatalog.ui.ViewModelFactory


class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterCardBookmark
    private val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
        val query = viewModel.firebaseDbBookmark().orderByChild("uid")
            .equalTo(viewModel.firebaseAuth().currentUser?.uid!!)
        val options = FirebaseRecyclerOptions.Builder<Bookmark>()
            .setQuery(query, Bookmark::class.java)
            .build()
        adapter = AdapterCardBookmark(options)
        binding.rvBookmarks.adapter = adapter

        adapter.setOnClickCallback(object : AdapterCardBookmark.OnClickListener {
            override fun onClickCard(id: String?, posterUrl: String?, year: String?) {
                BookmarkFragmentDirections.actionBookmarkFragmentGraphToDetailFragment().apply {
                    this.imdbId = id!!
                    this.posterUrl = posterUrl!!
                    this.year = year!!
                    findNavController().navigate(this)
                }
            }

        })
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

}