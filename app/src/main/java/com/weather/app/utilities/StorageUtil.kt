package com.weather.app.utilities

import android.content.Context
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class StorageUtil {

    @Throws(IOException::class)
    fun saveObjectByKey(context: Context, obj: Any, key: String?) {
        val fos: FileOutputStream = context.openFileOutput(key, Context.MODE_PRIVATE)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(obj)
        oos.close()
        fos.close()
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun getObjectByKey(context: Context, key: String?): Any? {
        val fis: FileInputStream = context.openFileInput(key)
        val ois = ObjectInputStream(fis)
        return ois.readObject()
    }

}