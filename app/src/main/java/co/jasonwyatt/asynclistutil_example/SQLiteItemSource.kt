package co.jasonwyatt.asynclistutil_example

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class SQLiteItemSource(val database: SQLiteDatabase) : ItemSource {
    private var _cursor: Cursor? = null
    private val cursor: Cursor
        @SuppressLint("Recycle")
        get() {
            if (_cursor == null || _cursor?.isClosed != false) {
                _cursor = database.rawQuery("SELECT title, content FROM data", null)
            }
            return _cursor ?: throw AssertionError("Set to null or closed by another thread")
        }

    override fun getCount() = cursor.count

    override fun getItem(position: Int): Item {
        cursor.moveToPosition(position)
        return Item(cursor)
    }

    override fun close() {
        _cursor?.close()
    }
}

private fun Item(c: Cursor): Item = Item(c.getString(0), c.getString(1))
