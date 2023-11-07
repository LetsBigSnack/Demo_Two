package com.cc221019.demo_two.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

    companion object DatabaseConfig {
        private const val dbName : String = "DebtsDatabase"
        private const val dbVersion : Int = 3

        private const val personTableName = "People"
        private const val personId = "_id"
        private const val personFirstName = "firstName"
        private const val personLastName = "lastName"

        private const val debtTableName = "Debts"
        private const val debtId = "_id"
        private const val debtName = "name"
        private const val debtAmount = "amount"
        private const val debtReason = "reason"
        private const val debtLendingDate = "lendingDate"
        private const val debtPersonId = "personID"

    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.execSQL("PRAGMA foreign_keys = ON")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $personTableName ($personId INTEGER PRIMARY KEY AUTOINCREMENT, $personFirstName VARCHAR(128), $personLastName VARCHAR(128));")
        db?.execSQL("CREATE TABLE IF NOT EXISTS $debtTableName ($debtId INTEGER PRIMARY KEY AUTOINCREMENT, $debtName VARCHAR(128), $debtReason VARCHAR(255), $debtAmount REAL, $debtLendingDate VARCHAR(128), $debtPersonId INTEGER, FOREIGN KEY($debtPersonId) REFERENCES $personTableName($personId) ON DELETE CASCADE);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $personTableName")
        db?.execSQL("DROP TABLE IF EXISTS $debtTableName")
        onCreate(db)
    }

    fun insertPeople(person: Person){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(personFirstName, person.firstName)
            put(personLastName, person.lastName)
        }

        db.insert(personTableName, null, values)
    }

    fun updatePeople(person: Person){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(personFirstName, person.firstName)
            put(personLastName, person.lastName)
        }

        db.update(personTableName,values,"$personId = ?", arrayOf(person.id.toString()))
    }

    fun deletePeople(person: Person){
        val db = this.writableDatabase
        db.delete(personTableName,"$personId = ?", arrayOf(person.id.toString()))
    }

    fun getPeople() : List<Person>{
        val people = mutableListOf<Person>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT p.$personId, p.$personFirstName, p.$personLastName, COALESCE(SUM(d.$debtAmount), 0) AS TotalDebt   FROM $personTableName p LEFT JOIN $debtTableName d ON p.$personId = p.$personId GROUP BY p.$personId", null)
        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(personId)
            val firstNameId = cursor.getColumnIndex(personFirstName)
            val lastNameId = cursor.getColumnIndex(personLastName)
            val debtAmountId = cursor.getColumnIndex("TotalDebt")
            println("TESTTTT $cursor")
            if(firstNameId >= 0 && lastNameId >= 0){
                println(cursor.getString(firstNameId))
                println(cursor.getString(lastNameId))
                println(cursor.getInt(idId))
                println(cursor.getFloat(debtAmountId))
                people.add(
                    Person(cursor.getString(firstNameId), cursor.getString(lastNameId), cursor.getInt(idId), cursor.getFloat(debtAmountId))
                )
            }
        }
        return people.toList();
    }


    fun insertDebts(debt: Debt){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(debtName, debt.name)
            put(debtAmount, debt.amount)
            put(debtReason, debt.reason)
            put(debtLendingDate, debt.lendingDate.toString())
            put(debtPersonId, debt.personID)
        }

        db.insert(debtTableName, null, values)
    }

    fun updateDebts(debt: Debt){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(debtName, debt.name)
            put(debtAmount, debt.amount)
            put(debtReason, debt.reason)
            put(debtLendingDate, debt.lendingDate.toString())
        }

        db.update(debtTableName,values,"$debtId = ?", arrayOf(debt.id.toString()))
    }

    fun deleteDebts(debt: Debt){
        val db = this.writableDatabase
        db.delete(debtTableName,"$debtId = ?", arrayOf(debt.id.toString()))
    }

    fun getDebts() : List<Debt>{
        val db = this.readableDatabase
        val students = mutableListOf<Debt>()
        val cursor = db.rawQuery("SELECT * FROM $debtTableName", null)
        while(cursor.moveToNext()){
            val idId = cursor.getColumnIndex(debtId)
            val nameId = cursor.getColumnIndex(debtName)
            val amountId = cursor.getColumnIndex(debtAmount)
            val reasonId = cursor.getColumnIndex(debtReason)
            val lendingDateId = cursor.getColumnIndex(debtLendingDate)
            val personIdId = cursor.getColumnIndex(debtPersonId)

            if(nameId >= 0 && amountId >= 0 && reasonId >= 0 && lendingDateId >= 0){
                students.add(Debt(cursor.getString(nameId), cursor.getFloat(amountId), cursor.getString(reasonId), cursor.getInt(personIdId), cursor.getString(lendingDateId), cursor.getInt(idId)))
            }
        }
        return students.toList();
    }


}