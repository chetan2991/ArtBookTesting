package com.techmobile.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.techmobile.artbooktesting.MainCoroutineRule
import com.techmobile.artbooktesting.getOrAwaitValueTest
import com.techmobile.artbooktesting.repo.FakeArtRepository
import com.techmobile.artbooktesting.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: ArtViewModel
    @Before
    fun setup(){
        //Test Doubles
        viewModel = ArtViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year return error` (){
            viewModel.makeArt("mona lisa", "davenci", "")
            val result = viewModel.insertArtMessage.getOrAwaitValueTest()
            assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name return error` (){
        viewModel.makeArt("", "davenci", "1900")
        val result = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistname return error` (){
        viewModel.makeArt("mona lisa", "", "1900")
        val result = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }
}