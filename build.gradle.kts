plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
}

group = "de.chojo"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://eldonexus.de/repository/maven-public")
    maven("https://eldonexus.de/repository/maven-proxies")
}

dependencies {
    //discord
    implementation("de.chojo", "cjda-util", "2.7.8+beta.2") {
        exclude(group = "club.minnced", module = "opus-java")
    }

    implementation("de.chojo.universalis", "universalis", "1.2.7")

    // database
    implementation("org.postgresql", "postgresql", "42.5.1")
    implementation("de.chojo.sadu", "sadu-queries", "1.2.0")
    implementation("de.chojo.sadu", "sadu-updater", "1.2.0")
    implementation("de.chojo.sadu", "sadu-postgresql", "1.2.0")
    implementation("de.chojo.sadu", "sadu-datasource", "1.2.0")

    // Logging
    implementation("org.slf4j", "slf4j-api", "2.0.6")
    implementation("org.apache.logging.log4j", "log4j-core", "2.19.0")
    implementation("org.apache.logging.log4j", "log4j-slf4j2-impl", "2.19.0")
    implementation("de.chojo", "log-util", "1.0.1"){
        exclude("org.apache.logging.log4j")
    }

    // unit testing
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter", "junit-jupiter")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {
    processResources {
        from(sourceSets.main.get().resources.srcDirs) {
            filesMatching("version") {
                expand(
                    "version" to project.version
                )
            }
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    shadowJar {
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "de.chojo.lolorito.Lolorito"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
