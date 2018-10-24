import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.MavenPom
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
  kotlin("jvm") version "1.3.0-rc-190"
  `maven-publish`
  id("com.github.ben-manes.versions") version "0.20.0"
  id("org.jlleitschuh.gradle.ktlint") version "6.1.0"
  id("com.jfrog.bintray") version "1.8.4"
  id("org.jetbrains.dokka") version "0.9.17"
//  id("com.palantir.git-version") version "0.12.0-rc2"
  id("net.nemerosa.versioning") version "2.8.2"
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
val projectVersion = versioning.info.display
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
  implementation(Deps.kotlinStandardLibrary)
  implementation(Deps.kotlinReflect)
  implementation(Deps.coroutinesCore)
  implementation(Deps.jfreesvg)
  implementation(Deps.svgSalamander)
  implementation(Deps.ztExec)
  implementation(Deps.jacksonYaml)
  implementation(Deps.jacksonDatabind)
  implementation(Deps.jacksonParams)
  implementation(Deps.jacksonJdk8)
  implementation(Deps.jacksonJsr310)
  implementation(Deps.jacksonCbor)
  implementation(Deps.slf4jApi)
  implementation(Deps.byteunits)
  implementation(Deps.activation)
  implementation(Deps.okio)

  testImplementation(Deps.junitJupiterApi)
  testImplementation(Deps.kotlinTest)
  testImplementation(Deps.kotlinTestJunit)

  testRuntime(Deps.junitJupiterEngine)
  testRuntime(Deps.slf4jJdk14)
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
