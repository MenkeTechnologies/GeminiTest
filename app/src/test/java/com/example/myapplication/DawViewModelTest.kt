package com.example.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DawViewModelTest {

    private lateinit var viewModel: DawViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DawViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() {
        assertFalse(viewModel.isPlaying)
        assertEquals(4, viewModel.trackVolumes.size)
        assertEquals(4, viewModel.trackNames.size)
    }

    @Test
    fun testTogglePlayback() {
        viewModel.togglePlayback()
        assertTrue(viewModel.isPlaying)
        viewModel.togglePlayback()
        assertFalse(viewModel.isPlaying)
    }

    @Test
    fun testUpdateVolume() {
        viewModel.updateVolume(0, 0.8f)
        assertEquals(0.8f, viewModel.trackVolumes[0])
        
        viewModel.updateVolume(3, 0.1f)
        assertEquals(0.1f, viewModel.trackVolumes[3])
    }
}
