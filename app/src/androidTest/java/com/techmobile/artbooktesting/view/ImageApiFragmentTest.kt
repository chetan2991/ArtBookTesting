package com.techmobile.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.techmobile.artbooktesting.R
import com.techmobile.artbooktesting.adapter.ImageRecyclerAdapter
import com.techmobile.artbooktesting.getOrAwaitValue
import com.techmobile.artbooktesting.launchFragmentInHiltContainer
import com.techmobile.artbooktesting.repo.FakeArtRepository
import com.techmobile.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImageApiFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }
    @Test
    fun selectImage(){

        val navecontroller = Mockito.mock(NavController::class.java)
        val selectedImageUrl = "abc.com"
        val tViewModel = ArtViewModel(FakeArtRepository())
        launchFragmentInHiltContainer<ImageApiFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navecontroller)
            viewModel = tViewModel
            imageRecyclerAdapter.images = listOf(selectedImageUrl)
        }
        Espresso.onView(withId(R.id.imageRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerAdapter.ImageViewHolder
                    >(0, click())

        )
        Mockito.verify(navecontroller).popBackStack()
        assertThat(tViewModel.selectedImageUrl.getOrAwaitValue()).isEqualTo(selectedImageUrl)
    }
}