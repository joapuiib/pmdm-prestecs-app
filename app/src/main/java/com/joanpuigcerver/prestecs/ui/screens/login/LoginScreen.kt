package com.joanpuigcerver.prestecs.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import kotlinx.coroutines.delay

/** Temps que dura l'inici de sessió simulat, perquè es vegi l'indicador de càrrega. */
private const val FAKE_LOGIN_MILLIS = 700L

/**
 * Pantalla d'inici de sessió amb l'aspecte de la de l'aplicació de préstecs (compte de Moodle).
 *
 * L'inici de sessió és **simulat**: no es connecta a Moodle i accepta qualsevol usuari i
 * contrasenya, sempre que no estiguen buits.
 *
 * @author Joan Puigcerver
 * @version 0.5.0
 */
@Composable
fun LoginScreen(onLoggedIn: (String) -> Unit, modifier: Modifier = Modifier) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val currentOnLoggedIn by rememberUpdatedState(onLoggedIn)

    // Inici de sessió simulat: espera un moment i sempre acaba bé.
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(FAKE_LOGIN_MILLIS)
            currentOnLoggedIn(username.trim())
        }
    }

    fun submit() {
        if (isLoading) return
        if (username.isBlank() || password.isBlank()) showError = true else isLoading = true
    }

    // Surface pinta el fons i el color dels textos.
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        ) {
            // Capçalera: icona de l'aplicació sobre un fons blau, nom i subtítol.
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                    )
                }
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.login_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }

            // Formulari dins d'una targeta.
            ElevatedCard(modifier = Modifier.widthIn(max = 420.dp).fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; showError = false },
                        label = { Text(stringResource(R.string.login_username)) },
                        leadingIcon = { Icon(painterResource(R.drawable.ic_person), contentDescription = null) },
                        singleLine = true,
                        enabled = !isLoading,
                        isError = showError && username.isBlank(),
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; showError = false },
                        label = { Text(stringResource(R.string.login_password)) },
                        leadingIcon = { Icon(painterResource(R.drawable.ic_lock), contentDescription = null) },
                        singleLine = true,
                        enabled = !isLoading,
                        isError = showError && password.isBlank(),
                        shape = MaterialTheme.shapes.medium,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(onDone = { submit() }),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility,
                                    ),
                                    contentDescription = stringResource(
                                        if (passwordVisible) R.string.login_hide_password else R.string.login_show_password,
                                    ),
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    AnimatedVisibility(visible = showError) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = stringResource(R.string.login_error_missing_fields),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp),
                            )
                        }
                    }
                    Button(
                        onClick = ::submit,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text(stringResource(R.string.login_submit), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}

@PrestecsPreviews
@Composable
private fun LoginPreview() {
    PrestecsTheme { LoginScreen(onLoggedIn = {}) }
}
