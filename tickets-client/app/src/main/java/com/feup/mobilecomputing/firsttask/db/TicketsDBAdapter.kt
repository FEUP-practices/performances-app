package com.feup.mobilecomputing.firsttask.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.feup.mobilecomputing.firsttask.models.TicketInternalType
import com.feup.mobilecomputing.firsttask.ui.utils.functions.Companion.dateFormat
import java.util.*
import kotlin.collections.ArrayList


class TicketsDBAdapter(context: Context) {

    private lateinit var mDb: SQLiteDatabase
    private lateinit var mDbHelper: DatabaseHelper
    private val mContext = context

    companion object {

        const val DATABASE_TICKET: String = "Tickets"
        const val DATABASE_VERSION = 2
        const val TAG = "TicketsDBAdapter"

        private const val KEY_TICKET_ID = "_id"
        private const val KEY_SEAT_NUMBER = "seatNumber"
        private const val KEY_USED = "used"
        private const val KEY_PERFORMANCE_ID = "performanceId"
        private const val KEY_TITLE = "name"
        private const val KEY_PRICE = "price"
        private const val KEY_START_DATE = "startDate"
        private const val KEY_END_DATE = "endDate"
        private const val KEY_ADDRESS = "address"
        private const val KEY_NUMBER_BOUGHT = "numberBought"

        private const val DATABASE_TICKET_CREATE =
            ("create table $DATABASE_TICKET ($KEY_TICKET_ID text primary key,"
                    + "$KEY_SEAT_NUMBER integer not null," +
                    " $KEY_PRICE real not null," +
                    " $KEY_USED boolean not null," +
                    " $KEY_PERFORMANCE_ID text not null," +
                    " $KEY_TITLE text not null," +
                    " $KEY_ADDRESS text not null," +
                    " $KEY_START_DATE datetime not null," +
                    " $KEY_END_DATE datetime not null," +
                    " $KEY_NUMBER_BOUGHT integer not null);")

        private class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
            DATABASE_TICKET,
            null,
            DATABASE_VERSION) {
            override fun onCreate(db: SQLiteDatabase?) {
                db?.execSQL(DATABASE_TICKET_CREATE);
            }

            override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
                db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TICKET");
                onCreate(db)
            }
        }
    }

    fun open(): TicketsDBAdapter {
        mDbHelper = DatabaseHelper(mContext);
        mDb = mDbHelper.writableDatabase;
        return this;
    }

    fun close() {
        mDbHelper.close()
    }

    fun saveTicket(ticket: TicketInternalType): Long {
        val initialValues = ContentValues()
        val (id, seatNumber, used, performanceId, name, price, startDate, endDate, address, numberBought) = ticket
        initialValues.put(KEY_TICKET_ID, id)
        initialValues.put(KEY_SEAT_NUMBER, seatNumber)
        initialValues.put(KEY_USED, used)
        initialValues.put(KEY_PERFORMANCE_ID, performanceId)
        initialValues.put(KEY_TITLE, name)
        initialValues.put(KEY_PRICE, price)
        initialValues.put(KEY_START_DATE, dateFormat.format(startDate))
        initialValues.put(KEY_END_DATE, dateFormat.format(endDate))
        initialValues.put(KEY_ADDRESS, address)
        initialValues.put(KEY_NUMBER_BOUGHT, numberBought)

        return mDb.insertOrThrow(DATABASE_TICKET, null, initialValues)
    }

    @SuppressLint("Range")
    fun findAllTickets() : ArrayList<TicketInternalType> {
        val cursor = mDb.rawQuery("select * from $DATABASE_TICKET order by $KEY_START_DATE", null)
        val ticketsList = arrayListOf<TicketInternalType>()
        if (cursor.moveToFirst()) {
            do {
                ticketsList.add(
                    TicketInternalType(
                        _id = cursor.getString(cursor.getColumnIndex(KEY_TICKET_ID)),
                        seatNumber = cursor.getInt(cursor.getColumnIndex(KEY_SEAT_NUMBER)),
                        used = cursor.getInt(cursor.getColumnIndex(KEY_USED)) > 0,
                        performanceId = cursor.getString(cursor.getColumnIndex(KEY_PERFORMANCE_ID)),
                        name = cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)),
                        startDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_START_DATE)))!!,
                        endDate = dateFormat.parse(cursor.getString(cursor.getColumnIndex(KEY_END_DATE)))!!,
                        address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        numberBought = cursor.getInt(cursor.getColumnIndex(KEY_NUMBER_BOUGHT))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ticketsList
    }

    fun deleteTicket(id: String): Int {
        return mDb.delete(DATABASE_TICKET, "$KEY_TICKET_ID = ?", arrayOf(id))
    }

}