import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
  maven
  signing
  `maven-publish`
  kotlin("jvm") version "1.2.41"
  id("org.jlleitschuh.gradle.ktlint").version("3.3.0")
  id("com.github.ben-manes.versions").version("0.17.0")
  id("net.nemerosa.versioning").version("2.7-beta.1")
  id("com.gradle.build-scan").version("1.13.2")
}

group = "com.baulsupp"

base {
  archivesBaseName = "oksocial-output"
}

versioning {
  releaseMode = project.findProperty("releaseMode") ?: "snapshot"
}

version = versioning.info.display
description = "OkHttp Social Output"

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion
val kotlinxCoroutinesVersion = "0.22.5"
val jacksonVersion = "2.9.5"

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
    languageVersion = "1.2"
    apiVersion = "1.2"
    freeCompilerArgs = listOf("-Xjsr305=strict")
  }
}

tasks.withType<Test> {
  testLogging.showStandardStreams = false
}

kotlin {
  experimental.coroutines = Coroutines.ENABLE
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}

repositories {
  google()
  jcenter()
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib-jdk8", kotlinVersion))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
  implementation("com.squareup.okio:okio:1.14.1")
  implementation("org.jfree:jfreesvg:3.3")
  implementation("com.kitfox.svg:svg-salamander:1.0")
  implementation("org.zeroturnaround:zt-exec:1.10")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
  implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
  implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:$jacksonVersion")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:$jacksonVersion")
  implementation("org.slf4j:slf4j-api:1.8.0-beta2")
  implementation("com.jakewharton.byteunits:byteunits:0.9.1")

  testImplementation(kotlin("test-junit", kotlinVersion))
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.2.0")
  testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
  testRuntimeOnly("org.slf4j:slf4j-jdk14:1.8.0-beta2")
}

publishing {
  publications.create("mavenJava", MavenPublication::class.java) {
    from(components.getByName("java"))
    artifact(sourcesJar)
  }
}

val sourcesJar by tasks.creating(Jar::class) {
  classifier = "sources"
  from(java.sourceSets.getByName("main").allSource)
}

buildScan {
  setTermsOfServiceAgree("yes")
  setTermsOfServiceUrl("https://gradle.com/terms-of-service")
}
