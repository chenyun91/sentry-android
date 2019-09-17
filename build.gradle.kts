import com.diffplug.spotless.LineEnding
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    id("com.diffplug.gradle.spotless") version "3.24.2" apply true
    jacoco
    `maven-publish`
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
    }
}

object Version {
    const val group = "io.sentry.android"
    const val version = "2.0.0-SNAPSHOT"
    const val artifact = "sentry-android"
}
subprojects {
    apply {
        plugin("maven-publish")
    }

    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
    }

//    val sourcesJar by tasks.creating(Jar::class) {
//        dependsOn(tasks.classes)
//        archiveClassifier.set("sources")
//        from(sourceSets.main.get().allSource)
//    }
//
//    val javadocJar by tasks.creating(Jar::class) {
//        from(tasks.javadoc)
//        archiveClassifier.set("javadoc")
//    }
//
//    artifacts {
//        archives(sourcesJar)
//        archives(javadocJar)
//    }
//    tasks.register<Jar>("sourcesJar") {
//        archiveClassifier.set("sources")
//        from(sourceSets.main.get().allJava)
//    }
//
//    tasks.register<Jar>("javadocJar") {
//        archiveClassifier.set("javadoc")
//        from(tasks.javadoc.get().destinationDir)
//    }

    publishing {

        publications.create<MavenPublication>("maven") {
            repositories {
                mavenLocal()
            }

            pom {
                description.set("Sentry SDK")
                name.set("sentry")
                url.set("https://sentry.io")
                licenses {
                    license {
                        name.set("MIT License - 2019 Sentry")
                        url.set("https://raw.githubusercontent.com/getsentry/sentry-android/master/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("bruno-garcia")
                        name.set("Bruno Garcia")
                        email.set("bruno@sentry.io")
                    }
                }
                scm {
                    url.set("https://github.com/getsentry/sentry-android")
                }
            }
        }
    }
}
allprojects {

    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    group = Version.group
    version = Version.version
    tasks {
        withType<Test> {
            testLogging.showStandardStreams = true
            testLogging.exceptionFormat = TestExceptionFormat.FULL
            testLogging.events = setOf(
                TestLogEvent.SKIPPED,
                TestLogEvent.PASSED,
                TestLogEvent.FAILED)
            dependsOn("cleanTest")
        }
        withType<JavaCompile> {
            options.compilerArgs.addAll(arrayOf("-Xlint:all", "-Werror"))
        }
    }
}

spotless {
    lineEndings = LineEnding.UNIX
    java {
        target("**/*.java")
        removeUnusedImports()
        googleJavaFormat()
    }

    kotlin {
        ktlint()
        target("**/*.kt")
    }
    kotlinGradle {
        ktlint()
    }
}
