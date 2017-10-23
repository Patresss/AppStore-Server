package edu.inf.store.domain

import java.time.LocalDateTime


data class OutputMessage(
        var from: String = "",
        var text: String = "",
        var time: String = LocalDateTime.now().toString()
)