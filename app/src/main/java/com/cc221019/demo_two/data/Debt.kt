package com.cc221019.demo_two.data

import java.util.Date

data class Debt(val name: String, val amount: Float, val reason: String, val personID:Int, val lendingDate: String = Date().toString(), val id: Int = 0)
