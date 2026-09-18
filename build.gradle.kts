group = "dev.artsman.poc"
version = "0.0.1-SNAPSHOT"
description = "PoC Gradle Project"

val projectJdkFullVersion = file(".java-version").readText().trim()
val projectJdkMajorVersion = projectJdkFullVersion.substringBefore('.').toInt()

plugins {
	java
}

repositories {
	mavenCentral()
}

dependencies {
	annotationProcessor("org.projectlombok:lombok:1.18.42")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	compileOnly("org.projectlombok:lombok:1.18.42")
	implementation("org.mapstruct:mapstruct:1.6.3")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.0")
	testCompileOnly("org.junit.jupiter:junit-jupiter:6.0.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter:6.0.0")
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(projectJdkMajorVersion))
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

tasks.withType<JavaCompile>().configureEach {
	doFirst {
		val selectedJdkVersion = javaCompiler.get().metadata.javaRuntimeVersion
		if (selectedJdkVersion.substringBefore('+') != projectJdkFullVersion) {
			throw GradleException("Java compilation requires JDK $projectJdkFullVersion, but the selected Java toolchain is JDK $selectedJdkVersion.")
		}
	}
}

tasks.test {
	useJUnitPlatform()
}
