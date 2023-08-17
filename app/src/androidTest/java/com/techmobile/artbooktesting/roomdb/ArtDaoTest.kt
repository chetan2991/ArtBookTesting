package com.techmobile.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.techmobile.artbooktesting.HiltTestRunner
import com.techmobile.artbooktesting.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {
    //we don't want main coroutine rules
    @get:Rule
    var instantTaskExecutorRule  = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
     lateinit var databse : ArtsDatabase
    private lateinit var artDao: ArtDao

    @Before
    fun setup(){
        //inMemoryDatabaseBuilder = this will create databse in ram tempary for testing purpose
        // allowMainThreadQueries : it allow it to run on main thread.
//        databse = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),ArtsDatabase::class.java
//        ).allowMainThreadQueries().build()
            hiltRule.inject()
        artDao = databse.artDao()
    }

    @After
    fun tearDown(){
        databse.close()
    }

    @Test
    fun insertArtTesting() = runTest {
            val art = Art("mona","davenci",1900,"test.com",1)
            artDao.insertArt(art)
            val artList = artDao.observeArt().getOrAwaitValue()
            assertThat(artList).contains(art)
    }

    @Test
    fun deleteArtTesting() = runTest {
        val art = Art("mona","davenci",1900,"test.com",1)
        artDao.insertArt(art)
        artDao.deleteArt(art)
        val artList = artDao.observeArt().getOrAwaitValue()
        assertThat(artList).doesNotContain(art)
    }

}