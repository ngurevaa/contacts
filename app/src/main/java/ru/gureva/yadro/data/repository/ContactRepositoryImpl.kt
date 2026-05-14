package ru.gureva.yadro.data.repository

import android.content.Context
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gureva.yadro.data.service.ContactServiceConnector
import ru.gureva.yadro.domain.model.Contact
import ru.gureva.yadro.domain.repository.ContactRepository
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val serviceConnector: ContactServiceConnector
) : ContactRepository {
    override suspend fun getAllContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI
                ),
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )

            val contacts = mutableListOf<Contact>()
            cursor?.use {
                val idIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val imageIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex)
                    val name = it.getString(nameIndex)
                    val number = it.getString(numberIndex)
                    val image = it.getStringOrNull(imageIndex)

                    val contact = Contact(id, name, number, image)
                    contacts += contact
                }
            }

            contacts
        }
    }

    override suspend fun deleteDuplicates(): Int {
        serviceConnector.bind()

        return try {
            val service = serviceConnector.getService()
            service.removeDuplicates()
        } finally {
            serviceConnector.unbind()
        }
    }
}
