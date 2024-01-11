package com.miftah.moviecatalog.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.miftah.moviecatalog.databinding.FragmentAccountBinding
import com.miftah.moviecatalog.ui.ViewModelFactory
import com.miftah.moviecatalog.ui.oboarding.OnboardingActivity


class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext())
            .load(viewModel.firebaseAuth().currentUser?.photoUrl)
            .circleCrop()
            .into(binding.imgAccount)

        binding.titleAccount.text = viewModel.firebaseAuth().currentUser?.displayName

        binding.logoutAccount.setOnClickListener {
            viewModel.firebaseAuth().currentUser?.getIdToken(true)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Token refreshed successfully
                        FirebaseAuth.getInstance().signOut()
                        Intent(activity, OnboardingActivity::class.java).apply {
                            startActivity(this)
                        }
                        activity?.finish()
                    }
                }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}