object Versions {
  val jackson = "2.12.0"
  val junit = "5.7.0"
  val kotlin = "1.4.30"
  val kotlinCoroutines = "1.4.2"
  val okio = "3.0.0-alpha.1"
}

object Deps {
  val activation = "javax.activation:activation:1.1.1"
  val byteunits = "com.jakewharton.byteunits:byteunits:0.9.1"
  val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  val jacksonCbor = "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor:${Versions.jackson}"
  val jacksonDatabind = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
  val jacksonJdk8 = "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.jackson}"
  val jacksonJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}"
  val jacksonParams = "com.fasterxml.jackson.module:jackson-module-parameter-names:${Versions.jackson}"
  val jacksonYaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jackson}"
  val jfreesvg = "org.jfree:jfreesvg:3.4"
  val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
  val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
  val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
  val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
  val kotlinTestJunit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
  val okio = "com.squareup.okio:okio:${Versions.okio}"
  val svgSalamander = "com.kitfox.svg:svg-salamander:1.0"
  val process = "com.github.pgreze:kotlin-process:1.2"
}
