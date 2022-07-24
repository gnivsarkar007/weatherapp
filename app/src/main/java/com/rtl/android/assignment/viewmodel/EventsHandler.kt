package com.rtl.android.assignment.viewmodel

interface EventsHandler<E> {
    fun onEvent(event: E)
}