package ru.gureva.yadro.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import ru.gureva.yadro.IContactService

class ContactService : Service() {
    private data class ContactKey(
        val name: String,
        val phone: String
    )

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private val binder = object : IContactService.Stub() {
        override fun removeDuplicates(): Int {
            return try {
                val deleted = removeContactDuplicates()
                if (deleted == 0) NOT_FOUND else SUCCESS
            } catch (ex: Exception) { ERROR }
        }
    }

    private fun removeContactDuplicates(): Int {
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
            ),
            null,
            null,
            null
        ) ?: throw Exception()

        val contacts = mutableMapOf<ContactKey, String>()
        val duplicates = mutableSetOf<String>()
        cursor.use {
            val idIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val name = it.getString(nameIndex).normalizeName()
                val number = it.getString(numberIndex).normalizeNumber()

                val key = ContactKey(name, number)
                if (contacts.containsKey(key) && contacts[key] != id) {
                    duplicates += id
                }
                else {
                    contacts[key] = id
                }
            }
        }

        var deleted = 0
        duplicates.forEach { id ->
            val rows = contentResolver.delete(
                ContactsContract.RawContacts.CONTENT_URI,
                "${ContactsContract.RawContacts.CONTACT_ID}=?",
                arrayOf(id)
            )

            if (rows > 0) { deleted++ }
        }

        return deleted
    }

    private fun String?.normalizeName(): String {
        return this
            ?.trim()
            ?.lowercase()
            ?: ""
    }

    private fun String?.normalizeNumber(): String {
        return this
            ?.replace(Regex("[^+ \\d]"), "")
            ?: ""
    }

    companion object {
        const val SUCCESS = 0
        const val ERROR = 1
        const val NOT_FOUND = 2
    }
}
