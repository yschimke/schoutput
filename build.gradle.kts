import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.MavenPom
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
  kotlin("jvm") version Versions.kotlin
  `maven-publish`
  id("com.github.ben-manes.versions") version "0.21.0"
  id("com.jfrog.bintray") version "1.8.4"
  id("org.jetbrains.dokka") version "0.9.18"
  id("net.nemerosa.versioning") version "2.8.2"
  id("com.palantir.consistent-versions") version "1.9.2"
  id("com.diffplug.gradle.spotless") version "3.23.1"
}

repositories {
  jcenter()
  mavenCentral()
  maven(url = "https://jitpack.io")
  maven(url = "http://repo.maven.apache.org/maven2")
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap/")
}

group = "com.baulsupp"
val artifactID = "oksocial-output"
description = "OkHttp Social Output"
val projectVersion = versioning.info.display!!
println("Version $projectVersion")
version = projectVersion

base {
  archivesBaseName = "oksocial-output"
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
  withType(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.apiVersion = "1.3"
    kotlinOptions.languageVersion = "1.3"
  }
}

tasks {
  "dokka"(DokkaTask::class) {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/javadoc"
  }
  withType<GenerateMavenPom> {
    destination = file("$buildDir/libs/${jar.get().baseName}.pom")
  }
}

dependencies {
  implementation("javax.activation:activation")
  implementation("com.jakewharton.byteunits:byteunits")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-cbor")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
  implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  implementation("org.jfree:jfreesvg")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("com.squareup.okio:okio")
  implementation("org.slf4j:slf4j-api")
  implementation("com.kitfox.svg:svg-salamander")
  implementation("org.zeroturnaround:zt-exec")
  implementation("commons-io:commons-io")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

  testRuntime("org.junit.jupiter:junit-jupiter-engine")
  testRuntime("org.slf4j:slf4j-jdk14")
}

val sourcesJar by tasks.creating(Jar::class) {
  classifier = "sources"
  from(kotlin.sourceSets["main"].kotlin)
}

val javadocJar by tasks.creating(Jar::class) {
  classifier = "javadoc"
  from("$buildDir/javadoc")
}

val jar = tasks["jar"] as org.gradle.jvm.tasks.Jar

spotless {
  kotlinGradle {
    ktlint("0.31.0").userData(mutableMapOf("indent_size" to "2", "continuation_indent_size" to "2"))
    trimTrailingWhitespace()
    endWithNewline()
  }
}

fun MavenPom.addDependencies() = withXml {
  asNode().appendNode("dependencies").let { depNode ->
    configurations.implementation.get().allDependencies.forEach {
      depNode.appendNode("dependency").apply {
        appendNode("groupId", it.group)
        appendNode("artifactId", it.name)
        appendNode("version", it.version)
      }
    }
  }
}

publishing {
  publications {
    create("mavenJava", MavenPublication::class) {
      artifactId = artifactID
      groupId = project.group.toString()
      version = project.version.toString()
      description = project.description
      artifact(jar)
      artifact(sourcesJar) {
        classifier = "sources"
      }
      artifact(javadocJar) {
        classifier = "javadoc"
      }
      pom.addDependencies()
      pom {
        packaging = "jar"
        developers {
          developer {
            email.set("yuri@schimke.ee")
            id.set("yschimke")
            name.set("Yuri Schimke")
          }
        }
        licenses {
          license {
            name.set("Apache License")
            url.set("http://opensource.org/licenses/apache-2.0")
            distribution.set("repo")
          }
        }
        scm {
          connection.set("scm:git:https://github.com/yschimke/okurl.git")
          developerConnection.set("scm:git:git@github.com:yschimke/okurl.git")
          url.set("https://github.com/yschimke/okurl.git")
        }
      }
    }
  }
}

fun findProperty(s: String) = project.findProperty(s) as String?
bintray {
  user = findProperty("baulsuppBintrayUser")
  key = findProperty("baulsuppBintrayKey")
  publish = true
  setPublications("mavenJava")
  pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
    repo = "baulsupp.com"
    name = "oksocial-output"
    userOrg = user
    websiteUrl = "https://github.com/yschimke/oksocial-output"
    githubRepo = "yschimke/oksocial-output"
    vcsUrl = "https://github.com/yschimke/oksocial-output.git"
    desc = project.description
    setLabels("kotlin")
    setLicenses("Apache-2.0")
    version(delegateClosureOf<BintrayExtension.VersionConfig> {
      name = project.version.toString()
    })
  })
}
