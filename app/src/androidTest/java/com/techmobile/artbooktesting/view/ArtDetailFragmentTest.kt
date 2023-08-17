package com.techmobile.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.techmobile.artbooktesting.R
import com.techmobile.artbooktesting.getOrAwaitValue
import com.techmobile.artbooktesting.launchFragmentInHiltContainer
import com.techmobile.artbooktesting.repo.FakeArtRepository
import com.techmobile.artbooktesting.roomdb.Art
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
class ArtDetailFragmentTest {

@get:Rule
var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Inject
    lateinit var fragmentFactory : ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }
    @Test
    fun testNavigationToArtDetailFragmentToImageApiFragment(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())
        Mockito.verify(navController).navigate(
            ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
        )
    }

    @Test
    fun testOnBackPressed(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave(){
        val tviewmodel = ArtViewModel(FakeArtRepository())
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            viewModel = tviewmodel
        }
        Espresso.onView(withId(R.id.nameText)).perform(replaceText("Mona Lisa"))
        Espresso.onView(withId(R.id.artistText)).perform(replaceText("davinci"))
        Espresso.onView(withId(R.id.yearText)).perform(replaceText("1900"))
        Espresso.onView(withId(R.id.saveButton)).perform(click())
        assertThat(tviewmodel.artList.getOrAwaitValue()).contains(
            Art("Mona Lisa", "davinci",1900,"")
        )
    }
}