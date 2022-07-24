package com.rtl.android.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val scheduler = TestCoroutineScheduler()

    protected val dispatcher = StandardTestDispatcher(scheduler = scheduler)

    @Before
    open fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}
