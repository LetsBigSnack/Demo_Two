package com.cc221019.demo_two.ui.views.model

import android.app.PendingIntent.getActivity
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.ViewModel
import com.cc221019.demo_two.data.DatabaseHandler
import com.cc221019.demo_two.data.Debt
import com.cc221019.demo_two.data.Person
import com.cc221019.demo_two.ui.views.MainViewState
import com.cc221019.demo_two.ui.views.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainViewModel(val db: DatabaseHandler) : ViewModel() {


    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    fun getDebts() {
        _mainViewState.update { it.copy(debts = db.getDebts()) }
    }

    fun getPeople(){
        _mainViewState.update { it.copy(people = db.getPeople()) }
    }

    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    fun debtDelete(debt: Debt){
        db.deleteDebts(debt)
        getDebts()
    }

    fun peopleDelete(person: Person){
        db.deletePeople(person)
        getPeople()
    }

    fun editDebt(debt: Debt){
        _mainViewState.update { it.copy(openDebtEditDialog =  true, editDebt = debt) }
    }

    fun saveDebt(debt: Debt){
        _mainViewState.update { it.copy(openDebtEditDialog = false) }
        db.updateDebts(debt)
        getDebts()
    }

    fun editPeople(person: Person){
        _mainViewState.update { it.copy(openPersonEditDialog =  true, editPerson = person) }
    }

    fun savePeople(person: Person){
        _mainViewState.update { it.copy(openPersonEditDialog = false) }
        db.updatePeople(person)
        getPeople()
    }

    fun openCreatePerson(){
        _mainViewState.update { it.copy(openPersonCreateDialog = true) }
    }

    fun openCreateDebt(){
        _mainViewState.update { it.copy(openDebtCreateDialog = true) }
    }

    fun createPerson(person: Person){
        _mainViewState.update { it.copy(openPersonCreateDialog = false) }
        if(person.firstName.isNullOrEmpty() || person.lastName.isNullOrEmpty()){
            return
        }else{
            db.insertPeople(person)
            getPeople()
        }
    }

    fun createDebt(debt: Debt){
        _mainViewState.update { it.copy(openDebtCreateDialog = false) }
        db.insertDebts(debt)
        getDebts()
    }

    fun dismissDialog(){
        _mainViewState.update { it.copy(openDebtEditDialog = false, openDebtCreateDialog = false, openPersonCreateDialog = false, openPersonEditDialog = false ) }
    }


}