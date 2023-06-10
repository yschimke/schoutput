plugins {
  kotlin("multiplatform") version "1.8.21"
  id("maven-publish")
  id("net.nemerosa.versioning") version "3.0.0"
}

group = "com.github.yschimke"
version = versioning.info.effectiveVersion()

repositories {
  mavenCentral()
}

kotlin {
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "17"
    }
    withJava()
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }
  js(IR) {
    compilations.all {
      kotlinOptions {
        moduleKind = "umd"
        sourceMap = true
        metaInfo = true
      }
    }
    nodejs {
      testTask {
        useMocha {
          timeout = "30s"
        }
      }
    }
    browser {
    }
  }
  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")

  sourceSets {
    val commonMain by getting {
      dependencies {
        api("com.squareup.okio:okio:3.3.0")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
      }
    }
    val commonTest by getting {
      dependencies {
        dependsOn(commonMain)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
        implementation(kotlin("test"))
        implementation("com.squareup.okio:okio:3.3.0")
      }
    }
    val jvmMain by getting {
      dependencies {
        api(libs.process)
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
        api("com.squareup.okio:okio:3.3.0")
        api(libs.activation)
      }
    }
    val jvmTest by getting {
      dependencies {
        dependsOn(jvmMain)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
        implementation(kotlin("test"))
        implementation("com.squareup.okio:okio:3.3.0")
      }
    }
    val nonJvmMain by creating {
      dependencies {
        dependsOn(commonMain)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
      }
    }
    val nonJvmTest by creating {
      dependencies {
        dependsOn(nonJvmMain)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
        implementation(kotlin("test"))
        dependsOn(commonTest)
      }
    }
    val jsMain by getting {
      dependsOn(nonJvmMain)
      dependencies {
      }
    }
    val jsTest by getting {
      dependencies {
        dependsOn(nonJvmTest)
        implementation(kotlin("test"))
      }
    }
  }
}

publishing {
  publications {
    withType<MavenPublication> {
//      artifact(javadocJar)
      tasks.withType<AbstractPublishToMaven>()
        .matching { it.publication == this }
        .configureEach { enabled = true }
    }
    repositories {
      maven {
        url = uri("file:build/repo")
      }
    }
  }
}

fun Project.booleanProperty(name: String) = this.findProperty(name).toString().toBoolean()

fun Project.booleanEnv(name: String) = (System.getenv(name) as String?).toString().toBoolean()

task("tagRelease") {
  doLast {
    val tagName = versioning.info.nextVersion() ?: throw IllegalStateException("unable to compute tag name")
    exec {
      commandLine("git", "tag", tagName)
    }
    exec {
      commandLine("git", "push", "origin", "refs/tags/$tagName")
    }
  }
}

fun net.nemerosa.versioning.VersionInfo.nextVersion() = when {
  this.tag == null && this.branch == "main" -> {
    val matchResult = "(\\d+)\\.(\\d+)\\.(\\d+)".toRegex().matchEntire(this.lastTag ?: "")
    if (matchResult != null) {
      val (_, major, minor) = matchResult.groupValues
      "v$major.${minor.toInt() + 1}"
    } else {
      null
    }
  }
  else -> {
    null
  }
}

fun net.nemerosa.versioning.VersionInfo.effectiveVersion() = when {
  this.tag == null && this.branch == "main" -> {
    val matchResult = "(\\d+)\\.(\\d+)\\.(\\d+)".toRegex().matchEntire(this.lastTag ?: "")
    if (matchResult != null) {
      val (_, major, minor) = matchResult.groupValues
      "$major.${minor.toInt() + 1}.0-SNAPSHOT"
    } else {
      "0.0.1-SNAPSHOT"
    }
  }
  else -> {
    this.display
  }
}
