package com.joanpuigcerver.prestecs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.joanpuigcerver.prestecs.navigation.PrestecsApp

private const val TAG = "PrestecsLifecycle"

/**
 * Activitat única de l'aplicació. Les pantalles es canvien amb la navegació de Compose.
 *
 * Cada mètode del cicle de vida escriu en el Logcat amb l'etiqueta `PrestecsLifecycle`.
 *
 * @author Joan Puigcerver
 * @version 0.3.0
 */
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate (recreada: ${savedInstanceState != null})")
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            PrestecsApp(widthClass = windowSizeClass.widthSizeClass)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy (canvi de configuració: $isChangingConfigurations)")
        super.onDestroy()
    }
}
