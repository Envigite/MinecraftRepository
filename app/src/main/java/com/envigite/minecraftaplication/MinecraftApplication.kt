package com.envigite.minecraftaplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // Prepara la aplicación para usar Hilt como sistema de inyección de dependencias.
class MinecraftApplication: Application() //Esta clase permite las configuraciones iniciales para tu app, principalmente relacionadas con Hilt.