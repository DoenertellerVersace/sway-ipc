import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    kotlin("jvm") version "2.0.21"
}

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(11))
    }
    sourceSets {
        main {
            kotlin.srcDir("src/main/kotlin")
            java.sourceSets.main {
                java.srcDir("src/main/java")
            }
        }
    }
    target {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
}

// Configurable directories
val jniHeaderDir = layout.buildDirectory.dir("generated/sources/headers/java/main")
val cSourceDir = layout.projectDirectory.dir("c")
val outputLibDir = layout.buildDirectory.dir("libs")
val javaFilesDir = layout.projectDirectory.dir("src/main/java/")

// Detect JAVA_HOME, fallback to system properties if not set
val javaHome: String = System.getenv("JAVA_HOME") ?: System.getProperty("java.home") ?: error("JAVA_HOME is not set")
val javac: String = "$javaHome/bin/javac"

val libName = if (Os.isFamily(Os.FAMILY_WINDOWS)) "swayipc.dll" else "libswayipc.so"

// Ensure directories exist
tasks.register("createDirs") {
    doFirst {
        jniHeaderDir.get().asFile.mkdirs()
        outputLibDir.get().asFile.mkdirs()
    }
}

// Task to generate JNI headers
tasks.register<Exec>("generateHeaders") {
    dependsOn("createDirs")
    group = "build"
    description = "Generates JNI headers for SwayIPC.java"

    workingDir = layout.projectDirectory.asFile
    commandLine(
        javac, "-h", jniHeaderDir.get().asFile,
        javaFilesDir.file("SwayIPC.java").asFile,
    )
//    commandLine(
//        "rm", javaFilesDir.file("SwayIPC.class").asFile
//    )
}

// Task to compile the native C code into a shared library
tasks.register<Exec>("compileNativeLib") {
    dependsOn("generateHeaders")
    group = "build"
    description = "Compiles the native C code into a shared library"

    workingDir = projectDir

    val includeDir = "$javaHome/include"
    val includeOsDir = if (Os.isFamily(Os.FAMILY_WINDOWS)) "$includeDir/win32" else "$includeDir/linux"

    commandLine(
        "/usr/bin/gcc", "-shared", "-fPIC",
        "-o", outputLibDir.get().file(libName).asFile,
        "-I", includeDir,
        "-I", includeOsDir,
        "-I", jniHeaderDir.get().asFile,
        cSourceDir.file("sway_ipc.c")
    )
}

tasks.test {
    dependsOn("compileNativeLib")
    useJUnitPlatform()
}
