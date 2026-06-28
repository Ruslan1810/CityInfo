package ru.project.cities.di

//import androidx.fragment.app.FragmentActivity
//import androidx.navigation.NavController
//import androidx.navigation.Navigator
//import androidx.navigation.findNavController
//import dagger.Binds
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.components.ViewModelComponent
//import ru.project.cities.navigation.CitiesNavigator

//@Module
//@InstallIn(ViewModelComponent::class)
//abstract class CitiesModule {
//
//    @Binds
//    abstract fun bindNavigator(navigator: CitiesNavigator): Navigator
//}
//
//@Module
//@InstallIn(ActivityComponent::class)
//object ActivityNavModule {
//    @Provides
//    fun provideNavController(activity: FragmentActivity): NavController =
//        activity.findNavController(R.id.nav_host_fragment)
//}