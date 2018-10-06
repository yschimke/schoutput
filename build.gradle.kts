import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.3.0-rc-116"
  `maven-publish`
  signing
  id("com.github.ben-manes.versions") version "0.20.0"
  id("net.nemerosa.versioning") version "2.7.1"
  id("io.codearte.nexus-staging") version "0.12.0"
  id("org.jlleitschuh.gradle.ktlint") version "6.1.0"
}

repositories {
  jcenter()
  mavenCentral()
  maven(url = "https://jitpack.io")
  maven(url = "http://repo.maven.apache.org/maven2")
  maven(url = "https://dl.bintray.com/kotlin/kotlin-eap/")
}

group = "com.baulsupp"
description = "OkHttp Social Output"

base {
  archivesBaseName = "oksocial-output"
}


//
//versioning {
//  // TODO automate this
//  releaseMode = project.hasProperty('releaseMode') ? project.property('releaseMode') : 'snapshot'
//}
//
//version = versioning.info.display
//
//

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

//publishing {
//  publications {
//    mavenJava(MavenPublication) {
//      from components.java
//    }
//  }
//}
//
////packaging tests
//task packageTests(type: Jar) {
//  from sourceSets.test.output
//  classifier = 'tests'
//}
//
//task javadocJar(type: Jar) {
//  classifier = 'javadoc'
//  from javadoc
//}
//
//task sourcesJar(type: Jar) {
//  classifier = 'sources'
//  from sourceSets.main.allSource
//}
//
//artifacts {
//  archives javadocJar, sourcesJar, packageTests, jar
//}
//
//// TODO support publish to maven local
//
//if (project.hasProperty('ossrhUser')) {
//  signing {
//    sign configurations.archives
//  }
//
//  nexusStaging {
//    username = ossrhUser
//    password = ossrhPassword
////  packageGroup = "org.mycompany.myproject" //optional if packageGroup == project.getGroup()
////  stagingProfileId = "yourStagingProfileId" //when not defined will be got from server using "packageGroup"
//  }
//
//  uploadArchives {
//    repositories {
//      mavenDeployer {
//        beforeDeployment { MavenDeployment deployment -> signing.signPom(deployment) }
//
//        repository(url: "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
//          authentication(userName: ossrhUser, password: ossrhPassword)
//        }
//
//        snapshotRepository(url: "https://oss.sonatype.org/content/repositories/snapshots/") {
//          authentication(userName: ossrhUser, password: ossrhPassword)
//        }
//
//        pom.project {
//          name project.name
//          group 'com.baulsupp'
//          description "Command Line Output Library"
//          url "https://github.com/yschimke/oksocial-output"
//
//          scm {
//            connection 'scm:git:https://github.com/yschimke/oksocial-output.git'
//            developerConnection 'scm:git:git@github.com:yschimke/oksocial-output.git'
//            url 'https://github.com/yschimke/oksocial-output.git'
//          }
//
//          licenses {
//            license {
//              name 'Apache License'
//              url 'http://opensource.org/licenses/apache-2.0'
//              distribution 'repo'
//            }
//          }
//
//          developers {
//            developer {
//              id = 'yschimke'
//              name = 'Yuri Schimke'
//              email = 'yuri@schimke.ee'
//            }
//          }
//
//          packaging 'jar'
//        }
//      }
//    }
//  }
//}
