package com.viartemev.calories.model

import java.time.LocalDate

data class CaloriesPerDay(val date: LocalDate, val calories: Int) {
    constructor(year: Int?, month: Int?, day: Int?, calories: Int?) : this(LocalDate.of(year!!, month!!, day!!), calories!!)
}