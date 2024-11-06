package com.envigite.minecraftaplication.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Observa los cambios en el LiveData solo una vez.
 *
 * Este método permite registrar un observer que será notificado con el primer valor
 * emitido por el LiveData y luego se eliminará automáticamente. Se utiliza para
 * situaciones en las que solo necesito reaccionar a un cambio específico
 * y no quiero recibir múltiples notificaciones.
 */

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<in T>) {
    val wrappedObserver = object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    }
    observe(lifecycleOwner, wrappedObserver)
}
