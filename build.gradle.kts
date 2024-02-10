buildscript {
    extra.apply {
//        set("room_version", "2.5.2") // old
        set("room_version", "2.6.1") // new
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
//    id("org.jetbrains.kotlin.android") version "1.8.21" apply false // old
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false // new
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id("com.android.application") version "8.2.0" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
//}