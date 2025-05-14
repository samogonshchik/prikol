buildscript {
    extra.apply {
        set("room_version", "2.6.1")
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false

//  choco
    id("com.chaquo.python") version "15.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
