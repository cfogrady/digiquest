import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("io.toolebox.git-versioner")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DigiQuest"
            packageVersion = "1.0.0"
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":core"))
    implementation("com.fazecast:jSerialComm:[2.0.0,3.0.0)")
    implementation("net.harawata:appdirs:1.2.1")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation(compose.desktop.currentOs)

    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.slf4j:slf4j-simple:1.7.32")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testCompileOnly("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
}

versioner {
    pattern {
        pattern = "%M.%m.%p(.%c-%h)"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
