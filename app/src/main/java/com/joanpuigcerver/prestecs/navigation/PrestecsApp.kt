package com.joanpuigcerver.prestecs.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.joanpuigcerver.prestecs.data.repository.InMemoryLoanRepository
import com.joanpuigcerver.prestecs.data.repository.LoanRepository
import com.joanpuigcerver.prestecs.domain.groupByRoom
import com.joanpuigcerver.prestecs.ui.screens.about.AboutScreen
import com.joanpuigcerver.prestecs.ui.screens.detail.LoanDetailScreen
import com.joanpuigcerver.prestecs.ui.screens.loans.LoansScreen
import com.joanpuigcerver.prestecs.ui.screens.login.LoginScreen
import com.joanpuigcerver.prestecs.ui.screens.onboarding.OnboardingScreen
import com.joanpuigcerver.prestecs.ui.screens.profile.ProfileScreen
import com.joanpuigcerver.prestecs.ui.screens.settings.SettingsScreen
import com.joanpuigcerver.prestecs.ui.screens.splash.SplashScreen
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import com.joanpuigcerver.prestecs.ui.theme.ThemeMode
import java.time.LocalDate

/**
 * Arrel de l'aplicació: guarda l'estat compartit i defineix la navegació.
 *
 * @author Joan Puigcerver
 * @version 0.5.0
 */
@Composable
fun PrestecsApp(
    widthClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now(),
    repository: LoanRepository = InMemoryLoanRepository(today),
) {
    var themeMode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }

    // La presentació només s'ensenya la primera vegada. Sense persistència, es torna a veure
    // en cada arrancament en fred, però no en girar la pantalla.
    var onboardingDone by rememberSaveable { mutableStateOf(false) }

    // Usuari de la sessió simulada; buit si no s'ha iniciat sessió.
    var userName by rememberSaveable { mutableStateOf("") }

    // Préstecs oberts, agrupats per aula, i els del professor connectat.
    val groups = groupByRoom(repository.getActiveLoans())
    val openLoans = groups.flatMap { it.loans }
    val myLoans = openLoans.filter { userName.isNotBlank() && it.teacher.equals(userName, ignoreCase = true) }

    val backStack = rememberNavBackStack(Splash)

    /** Buida la pila i deixa només esta pantalla. */
    fun replaceAll(key: NavKey) {
        backStack.clear()
        backStack.add(key)
    }

    PrestecsTheme(themeMode = themeMode) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            modifier = modifier,
            entryProvider = entryProvider {
                entry<Splash> {
                    SplashScreen(onFinished = {
                        replaceAll(
                            when {
                                !onboardingDone -> Onboarding
                                userName.isBlank() -> Login
                                else -> Home
                            },
                        )
                    })
                }
                entry<Onboarding> {
                    // Tant si s'acaba com si se salta, es va a l'inici de sessió.
                    OnboardingScreen(onFinished = {
                        onboardingDone = true
                        replaceAll(Login)
                    })
                }
                entry<Login> {
                    LoginScreen(onLoggedIn = { name ->
                        userName = name
                        replaceAll(Home)
                    })
                }
                entry<Home> {
                    LoansScreen(
                        groups = groups,
                        twoPane = widthClass != WindowWidthSizeClass.Compact,
                        onLoanClick = { backStack.add(Detail(it)) },
                        onProfile = { backStack.add(Profile) },
                        onSettings = { backStack.add(Settings) },
                        onAbout = { backStack.add(About) },
                        onLogOut = {
                            userName = ""
                            replaceAll(Login)
                        },
                    )
                }
                entry<Detail> { key ->
                    openLoans.firstOrNull { it.id == key.loanId }?.let { loan ->
                        LoanDetailScreen(loan = loan, onBack = { backStack.removeLastOrNull() })
                    }
                }
                entry<Profile> {
                    ProfileScreen(
                        userName = userName,
                        activeLoans = myLoans.count { !it.isOverdue },
                        overdueLoans = myLoans.count { it.isOverdue },
                        onBack = { backStack.removeLastOrNull() },
                    )
                }
                entry<Settings> {
                    SettingsScreen(
                        themeMode = themeMode,
                        onThemeModeChange = { themeMode = it },
                        onAboutClick = { backStack.add(About) },
                        onBack = { backStack.removeLastOrNull() },
                    )
                }
                entry<About> { AboutScreen(onBack = { backStack.removeLastOrNull() }) }
            },
        )
    }
}
