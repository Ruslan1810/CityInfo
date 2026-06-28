package ru.project.cities.navigation

//import androidx.navigation.NavController
//import ru.project.core.navigation.Navigator
//import ru.project.core.navigation.Screen
//
//class CitiesNavigator(private val navController: NavController) : Navigator {
//    override fun navigateTo(screen: Screen) {
//        when (screen) {
//            is Screen.CityDetails -> navController.navigate("city_details?name=${screen.cityName}")
//            Screen.Cities -> navController.popBackStack()
//            Screen.Error -> navController.navigate("error")
//        }
//    }
//
//    override fun back() {
//        navController.popBackStack()
//    }
//
//    override fun newRoot(screen: Screen) {
//        // ✅ Используем navigate с popUpTo для очистки бэкстека
//        navController.navigate(screen.route) {
//            popUpTo(0) { inclusive = true }  // Удаляем все
//            launchSingleTop = true
//        }
//    }
//}