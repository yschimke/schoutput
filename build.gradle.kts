plugins {
  kotlin("jvm") version Versions.kotlin
  `java-library`
  `maven-publish`
  id("com.github.ben-manes.versions") version "0.39.0"
  id("net.nemerosa.versioning") version "2.15.0"
}

repositories {
  mavenCentral()
  maven(url = "https://jitpack.io")
}

group = "com.github.yschimke"
version = versioning.info.display

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.apiVersion = "1.5"
    kotlinOptions.languageVersion = "1.5"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}

dependencies {
  implementation(Deps.activation)
  implementation(Deps.byteunits)
  implementation(Deps.coroutinesCore)
  implementation(Deps.kotlinReflect)
  implementation(Deps.kotlinStandardLibrary)
  implementation(Deps.okio)
  implementation(Deps.process)

  testImplementation(Deps.junitJupiterApi)
  testImplementation(Deps.kotlinTest)

  testRuntimeOnly(Deps.junitJupiterEngine)
}
