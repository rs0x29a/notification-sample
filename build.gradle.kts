import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  dependencies {
    classpath(libs.gradlePlugin.kotlin)
  }

  repositories {
    mavenLocal()
    mavenCentral()
  }
}

allprojects {
  group = "ru.rs0x29a.sample.notification"
  version = "1.0.0"

  repositories {
    mavenLocal()
    mavenCentral()
  }
}

subprojects {
  tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()

    options.isWarnings = false
    options.encoding = Charsets.UTF_8.toString()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = JavaVersion.VERSION_11.toString()
      freeCompilerArgs = listOf(
        "-Xjsr305=strict",
        "-Xjvm-default=enable"
      )
    }
  }
}

tasks.register("getAppVersion") {
  doLast {
    println("version: ${project.version}")
  }
}
