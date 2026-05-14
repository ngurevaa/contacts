package ru.gureva.yadro.data.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import ru.gureva.yadro.IContactService
import javax.inject.Inject

class ContactServiceConnector @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var iService: IContactService? = null
    private var pending = CompletableDeferred<IContactService>()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            iService = IContactService.Stub.asInterface(service)
            iService?.let { pending.complete(it) }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            iService = null
        }
    }

    fun bind() {
        pending = CompletableDeferred()
        context.bindService(
            Intent(context, ContactService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    suspend fun getService(): IContactService {
        iService?.let { return it }
        return pending.await()
    }

    fun unbind() {
        try { context.unbindService(connection) }
        catch (ex: Exception) {}
        iService = null
    }
}
