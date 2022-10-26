@Suppress(
  "DSL_SCOPE_VIOLATION",
  "MISSING_DEPENDENCY_CLASS",
  "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
  "FUNCTION_CALL_EXPECTED"
)
plugins {
  id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"

	kotlin("jvm")
	kotlin("plugin.spring") version "1.6.21"
}

val jarName = "notification-route"

dependencies {
  // Spring Boot
  implementation("org.springframework.boot:spring-boot-starter-web")

  // Camel Spring Boot
	implementation("org.apache.camel.springboot:camel-spring-boot-starter:3.19.0")
  implementation("org.apache.camel.springboot:camel-jackson-starter:3.19.0")
  implementation("org.apache.camel.springboot:camel-jsonpath-starter:3.19.0")
  implementation("org.apache.camel.springboot:camel-springdoc-starter:3.19.0")
  implementation("org.apache.camel.springboot:camel-velocity-starter:3.19.0")
  implementation("org.apache.camel.springboot:camel-aws2-sqs-starter:3.19.0")

  // Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  // Compile & Runtime & Annotation & Development & Test
  annotationProcessor(libs.spring.boot.processor)
  developmentOnly(libs.spring.boot.devtools)
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Jar>("jar") {
  enabled = false
}

springBoot {
  buildInfo()
}

tasks {
  val sourcesJar by creating(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveBaseName.set(jarName)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
  }

  artifacts {
    add("archives", sourcesJar)
  }
}
