package com.miftah.moviecatalog.ui.oboarding

import androidx.lifecycle.ViewModel
import com.miftah.moviecatalog.core.AppRepository

class OnBoardingViewModel(private val repo : AppRepository) : ViewModel() {

    fun firebaseAuth() = repo.firebaseAuth()
}