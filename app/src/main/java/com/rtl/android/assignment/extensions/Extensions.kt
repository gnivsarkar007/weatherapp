package com.rtl.android.assignment.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate?.mapToFormattedString() =
    this?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""
