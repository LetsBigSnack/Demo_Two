package com.cc221019.demo_two.ui.views

import com.cc221019.demo_two.data.Debt
import com.cc221019.demo_two.data.Person
import com.cc221019.demo_two.ui.views.Screen

data class MainViewState(
    val debts: List<Debt> = emptyList(),
    val people: List<Person> = emptyList(),
    val editDebt: Debt = Debt("",00.0f,"", 0),
    val editPerson: Person = Person("",""),
    val selectedScreen: Screen = Screen.First,
    val openDebtEditDialog: Boolean = false,
    val openDebtCreateDialog: Boolean = false,
    val openPersonEditDialog: Boolean = false,
    val openPersonCreateDialog: Boolean = false,
)
