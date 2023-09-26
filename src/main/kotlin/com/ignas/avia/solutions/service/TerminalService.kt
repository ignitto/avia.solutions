package com.ignas.avia.solutions.service

import com.ignas.avia.solutions.model.Terminal
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Service
import java.util.*

@Service
class TerminalService {

    private val arrivals = LinkedList<String>()
    private val mutex = Mutex()

    fun addToArrivalList(flightNumber: String) {
        arrivals.add(flightNumber)
    }

    suspend fun assignTerminal(terminalNumber: String): Terminal? = mutex.withLock {

        if (arrivals.isEmpty()) {
            return null
        }

        val flightNumber = arrivals.first();
        arrivals.removeFirst()

        return Terminal(
            terminalNumber = terminalNumber,
            currentFlight = flightNumber
        )
    }

}