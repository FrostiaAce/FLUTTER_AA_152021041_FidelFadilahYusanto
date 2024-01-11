package com.miftah.moviecatalog.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.miftah.moviecatalog.R
import com.miftah.moviecatalog.core.data.domain.Bookmark
import com.miftah.moviecatalog.core.data.domain.Comment
import com.miftah.moviecatalog.core.data.domain.Outcome
import com.miftah.moviecatalog.databinding.FragmentDetailBinding
import com.miftah.moviecatalog.ui.ViewModelFactory
import com.miftah.moviecatalog.utils.Constant
import java.util.Date


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterComment
    private val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get mbId from fragment
        val mbdIdFromMoviesFragment = DetailFragmentArgs.fromBundle(arguments as Bundle).imdbId
        val posterFromMoviesFragment = DetailFragmentArgs.fromBundle(arguments as Bundle).posterUrl
        val yearFromMoviesFragment = DetailFragmentArgs.fromBundle(arguments as Bundle).year

        // fetch data from realtime database with mbId
        viewModel.getMovieDetailById(mbdIdFromMoviesFragment)
            .observe(viewLifecycleOwner) { outcome ->
                when (outcome) {
                    is Outcome.Error -> {
                        binding.progressBar.isIndeterminate = false
                        AlertDialog.Builder(requireContext())
                            .setTitle("Something Wrong")
                            .setMessage(outcome.error)
                            .setPositiveButton("Back to Search") { _, _ ->
                                findNavController().popBackStack()
                            }.create().show()
                    }

                    Outcome.Loading -> binding.progressBar.isIndeterminate = true

                    is Outcome.Success -> {
                        binding.progressBar.isIndeterminate = false
                        outcome.data.let {
                            binding.tvActorsDetail.text = it.actors
                            binding.tvRatingDetail.text = it.ratingList.first().value
                            binding.tvReleaseDetail.text = it.released
                            binding.tvTitleDetail.text = it.title
                            binding.tvVotesDetail.text = it.imdbVotes
                            binding.tvWritersDetail.text = it.imdbVotes
                            binding.tvPlotDetail.text = it.plot
                            binding.tvPrimaryTitleDetail.text = it.title
                        }
                        binding.imgWebViewPoster.loadUrl("${Constant.BASE_MOVIE_IMG_URL}/?i=$mbdIdFromMoviesFragment&apikey=${Constant.MOVIE_KEY}")
                    }
                }
            }

        // send Comment to realtime database
        binding.btnSendComment.setOnClickListener {
            if (!binding.edInputComment.text.isNullOrEmpty()) {
                val comment = Comment(
                    sender = viewModel.firebaseAuth().currentUser?.displayName.toString(),
                    text = binding.edInputComment.text.toString(),
                    timestamp = Date().time,
                    photoUrl = viewModel.firebaseAuth().currentUser?.photoUrl.toString(),
                    mdbId = mbdIdFromMoviesFragment
                )
                if (viewModel.sendComment(comment)) {
                    Toast.makeText(requireContext(), "scc", Toast.LENGTH_SHORT).show()
                    binding.edInputComment.setText("")
                }
            }
        }

        // get all comment with same mbId from realtime database
        binding.rvComment.layoutManager = LinearLayoutManager(requireContext())
        val query =
            viewModel.firebaseDbComment().orderByChild("mdbId").equalTo(mbdIdFromMoviesFragment)
        val options = FirebaseRecyclerOptions.Builder<Comment>()
            .setQuery(query, Comment::class.java)
            .build()
        adapter = AdapterComment(options)
        binding.rvComment.adapter = adapter

        // check if bookmark is exists or not
        viewModel.firebaseQuery(
            mbdIdFromMoviesFragment,
            viewModel.firebaseAuth().currentUser?.uid!!
        ).observe(viewLifecycleOwner) {
            if (it) {
                binding.btnBookmarksDetail.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_bookmark_24
                    )
                )

                binding.btnBookmarksDetail.setOnClickListener {
                    viewModel.firebaseUnBookmark(
                        mbdIdFromMoviesFragment,
                        viewModel.firebaseAuth().uid!!
                    ).observe(viewLifecycleOwner) {result ->
                        binding.progressBar.isIndeterminate = !result
                    }
                }
            } else {
                binding.btnBookmarksDetail.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_bookmark_border_24
                    )
                )
                binding.btnBookmarksDetail.setOnClickListener {
                    viewModel.firebaseSaveBookmark(
                        Bookmark(
                            title = binding.tvTitleDetail.text.toString(),
                            poster = posterFromMoviesFragment,
                            mbdId = mbdIdFromMoviesFragment,
                            year = yearFromMoviesFragment,
                            uid = viewModel.firebaseAuth().uid
                        )
                    )
                }
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}