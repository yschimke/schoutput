plugins {
  kotlin("jvm") version libs.versions.kotlin
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
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain {
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of("17"))
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions.jvmTarget = "17"
    kotlinOptions.apiVersion = "1.5"
    kotlinOptions.languageVersion = "1.5"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}

dependencies {
  implementation(libs.activation)
  implementation(libs.byteunits)
  implementation(libs.coroutines.core)
  implementation(libs.kotlin.reflect)
  implementation(libs.kotlin.stdlibJdk8)
  implementation(libs.okio)
  implementation(libs.process)

  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.kotlinTest)

  testRuntimeOnly(libs.junitJupiterEngine)
}

val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.main.get().allSource)
}

afterEvaluate {
  publishing {
    publications {
      register("mavenJava", MavenPublication::class) {
        from(components["java"])
        artifact(sourcesJar.get())
      }
    }
  }
}
