plugins {
  kotlin("jvm") version Versions.kotlin
  `maven-publish`
  id("com.github.ben-manes.versions") version "0.28.0"
  id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

repositories {
  jcenter()
  mavenCentral()
  maven(url = "https://jitpack.io")
  maven(url = "https://repo.maven.apache.org/maven2")
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap/")
}

group = "com.baulsupp"
val artifactID = "oksocial-output"
description = "OkHttp Social Output"

base {
  archivesBaseName = "oksocial-output"
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.apiVersion = "1.3"
    kotlinOptions.languageVersion = "1.3"
}

dependencies {
  implementation(Deps.activation)
  implementation(Deps.byteunits)
  implementation(Deps.coroutinesCore)
  implementation(Deps.jacksonCbor)
  implementation(Deps.jacksonDatabind)
  implementation(Deps.jacksonJdk8)
  implementation(Deps.jacksonJsr310)
  implementation(Deps.jacksonParams)
  implementation(Deps.jacksonYaml)
  implementation(Deps.jfreesvg)
  implementation(Deps.kotlinReflect)
  implementation(Deps.kotlinStandardLibrary)
  implementation(Deps.okio)
  implementation(Deps.slf4jApi)
  implementation(Deps.svgSalamander)
  implementation(Deps.ztExec)

  testImplementation(Deps.junitJupiterApi)
  testImplementation(Deps.kotlinTest)
  testImplementation(Deps.kotlinTestJunit)

  testRuntime(Deps.junitJupiterEngine)
  testRuntime(Deps.slf4jJdk14)
}
