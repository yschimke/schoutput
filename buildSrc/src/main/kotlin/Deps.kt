object Versions {
  val junit = "5.7.1"
  val kotlin = "1.5.30"
  val kotlinCoroutines = "1.5.1"
  val okio = "3.0.0-alpha.9"
}

object Deps {
  val activation = "javax.activation:activation:1.1.1"
  val byteunits = "com.jakewharton.byteunits:byteunits:0.9.1"
  val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
  val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
  val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
  val kotlinTest = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
  val okio = "com.squareup.okio:okio:${Versions.okio}"
  val process = "com.github.pgreze:kotlin-process:1.2"
}
