package com.ignas.avia.solutions.service

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TerminalServiceTest {

    private lateinit var terminalService: TerminalService
    private val job = Job()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(job + testDispatcher)

    @BeforeEach
    fun init() {
        terminalService = TerminalService();
    }

    @Test
    fun `assign to terminal`() = testScope.runTest {
        for (i in 0..49) {
            terminalService.addToArrivalList("FF$i")
        }

        repeat(50) {
            launch {
                val terminal = async { terminalService.assignTerminal(it.toString())!! }
                assertEquals(terminal.await().terminalNumber, it.toString())
                assertEquals(terminal.await().currentFlight, "FF$it")
            }
        }
    }

    @Test
    fun `return null when no arrivals`() = testScope.runTest {
        launch {
            val terminal = terminalService.assignTerminal("999")
            assertNull(terminal)
        }
    }
}