package co.jasonwyatt.asynclistutil_example

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

private var database: SQLiteDatabase? = null
fun getDatabase(c: Context, assetFileName: String): SQLiteDatabase {
    if (database == null) {
        val input = c.applicationContext.assets.open(assetFileName)
        val outfile = File(c.filesDir, assetFileName)
        if (outfile.exists()) {
            outfile.delete()
        }
        copyToFile(input, outfile)

        database = SQLiteDatabase.openOrCreateDatabase(outfile, null)
    }
    return database!!
}

private fun copyToFile(inputStream: InputStream, outputFile: File) {
    val outputStream = FileOutputStream(outputFile)
    val buffer = ByteArray(1024 * 8)
    var numOfBytesToRead: Int = inputStream.read(buffer)
    while (numOfBytesToRead > 0) {
        outputStream.write(buffer, 0, numOfBytesToRead)
        numOfBytesToRead = inputStream.read(buffer)
    }
    inputStream.close()
    outputStream.close()
}