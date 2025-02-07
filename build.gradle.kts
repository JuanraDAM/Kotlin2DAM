// Top-level build file where you can add configuration options common to all sub-projects/modules.

/*
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}
true // Needed to make the Suppress annotation work for the plugins block
*/
//grandle de proyecto
plugins {
    id ("com.android.application") version "8.5.0" apply false
    id ("com.android.library") version "8.5.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id ("com.google.dagger.hilt.android") version "2.44" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.1" apply false
}
