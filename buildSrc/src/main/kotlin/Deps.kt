object Versions {
  val kotlin = "1.3.0-rc-198"
  val jackson = "2.9.7"
  val kotlinCoroutines = "1.0.0-RC1"
}

object Deps {
  val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
  val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
  val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  val jfreesvg = "org.jfree:jfreesvg:3.3"
  val svgSalamander = "com.kitfox.svg:svg-salamander:1.0"
  val ztExec = "org.zeroturnaround:zt-exec:1.10"
  val jacksonYaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jackson}"
  val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
  val jacksonParams = "com.fasterxml.jackson.module:jackson-module-parameter-names:${Versions.jackson}"
  val jacksonJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.jackson}"
  val jacksonJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}"
  val jacksonCbor = "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:${Versions.jackson}"
  val slf4jApi = "org.slf4j:slf4j-api:1.8.0-beta2"
  val byteunits = "com.jakewharton.byteunits:byteunits:0.9.1"
  val activation = "javax.activation:activation:1.1.1"
  val okio = "com.squareup.okio:okio:2.1.0"

  val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:5.3.1"
  val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:5.3.1"
  val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
  val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"

  val slf4jJdk14 = "org.slf4j:slf4j-jdk14:1.8.0-beta2"
}
