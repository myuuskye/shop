/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our samples at https://docs.gradle.org/7.6/samples
 */

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "jp.fishnetwork"
version = "0.0.0"

repositories {
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
}