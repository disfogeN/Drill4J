import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.zeroturnaround.gradle.jrebel") version ("1.1.8")
    id("org.springframework.boot") version ("2.0.0.RELEASE")
    id("org.jetbrains.kotlin.jvm") version ("1.3.0")
    id("org.jetbrains.kotlin.plugin.spring") version ("1.3.0")
    id("io.spring.dependency-management") version ("1.0.4.RELEASE")
    id("idea")
}

val boostrapVersion = "3.3.6"
val jQueryVersion = "2.2.4"
val jQueryUIVersion = "1.11.4"

version = "2.0.0"



repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.cache:cache-api")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars:jquery:$jQueryVersion")
    implementation("org.webjars:jquery-ui:$jQueryUIVersion")
    implementation("org.webjars:bootstrap:$boostrapVersion")

    testCompile("org.springframework.boot:spring-boot-starter-test") {
    }
    testCompile("org.springframework.boot:spring-boot-starter-webflux") {
        exclude(group = "org.junit")
    }
    testCompile("org.springframework.boot:spring-boot-starter-tomcat") {
        exclude(group = "org.junit")
    }
    testCompile("org.mock-server:mockserver-netty:3.9.1")
    runtime("org.hsqldb:hsqldb")
    runtime("mysql:mysql-connector-java")
}


tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    jvmArgs("-javaagent:${file("../../distr/drill-core-agent.jar")}")
}


val ex =
    if (Os.isFamily(Os.FAMILY_UNIX)) {
        "so"
    } else "dll"


val pref =
    if (Os.isFamily(Os.FAMILY_UNIX)) {
        "lib"
    } else ""


tasks.named<BootRun>("bootRun") {
    doFirst {
        jvmArgs("-Xmx2g")
        jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5007")
//        jvmArgs("-agentpath:${file("../../distr/main.dll")}=configsFolder=${file("../../src/nativeCommonMain/resources")},drillInstallationDir=${file("../../distr")}")
        jvmArgs(
            "-agentpath:${file("../../distr/${pref}main.$ex")}=configsFolder=${file("../../resources")},drillInstallationDir=${file(
                "../../distr"
            )}"
        )
    }

}

idea {
    module {
        inheritOutputDirs = false
        outputDir = file("build/classes/kotlin/main")
        testOutputDir = file("build/classes/kotlin/test")
    }
}

val jar by tasks
val bootRun: BootRun by tasks
jar.dependsOn("generateRebel")

//if (System.getenv("JREBEL_HOME") != null) {
//
//    val ext = when {
//        Os.isFamily(Os.FAMILY_MAC) -> "dylib"
//        Os.isFamily(Os.FAMILY_UNIX) -> "so"
//        Os.isFamily(Os.FAMILY_WINDOWS) -> "dll"
//        else -> {
//            throw RuntimeException("What is your OS???")
//        }
//    }
//    bootRun.jvmArgs("-agentpath:${System.getenv("JREBEL_HOME")}/lib/${if ("dll" == ext) {
//        ""
//    } else {
//        "lib"
//    }}jrebel64.$ext")
//}
rebel {
    //    showGenerated = true
//    rebelXmlDirectory = "build/classes"
//
//    classpath {
//        resource {
//            directory = "build/classes/kotlin/main"
//            includes = ["**/*"]
//        }
//
//
//        resource {
//            directory = "build/resources/main"
//        }
//    }
}
