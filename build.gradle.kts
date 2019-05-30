plugins {
    kotlin("multiplatform") version "1.3.31"
}

repositories {
    jcenter()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    mavenCentral()
}
val ktor_version = "1.1.3"
val logback_version = "1.2.3"

kotlin {
    jvm()
    js {
        compilations.all {
            kotlinOptions {
                moduleKind = "umd"
                sourceMap = true
                metaInfo = true
            }
        }
    }

    val commonMain by sourceSets.getting
    val commonTest by sourceSets.getting
    val jvmMain by sourceSets.getting
    val jvmTest by sourceSets.getting
    val jsMain by sourceSets.getting
    val jsTest by sourceSets.getting

    commonMain.dependencies {
        implementation(kotlin("stdlib-common"))
    }

    commonTest.dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    jvmMain.dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("io.ktor:ktor-server-netty:$ktor_version")
        implementation("io.ktor:ktor-html-builder:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
    }

    jvmTest.dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
    }

    jsMain.dependencies {
        implementation(kotlin("stdlib-js"))
    }

    jsTest.dependencies {
        implementation(kotlin("test-js"))
    }


    val webFolder = File(project.buildDir, "jsMain/web")
    val jsCompilations = kotlin.targets.getByName("js").compilations

    val populateWebFolder by tasks.creating(Sync::class) {
        group = "build"
        jsCompilations.forEach {
            dependsOn(it.compileAllTaskName)
            from(it.output)

            doFirst {
                it.compileDependencyFiles.forEach { f ->
                    if (f.exists() && !f.isDirectory) {
                        from(zipTree(f).matching { include("*.js") })
                    }
                }

            }
        }
        from(jsMain.resources)

        into(webFolder)
    }

    val jsJar by tasks.getting {
        dependsOn(populateWebFolder)
    }

    val run by tasks.creating(JavaExec::class) {
        group = "build"
        dependsOn(targets["jvm"].compilations["main"].compileAllTaskName, jsJar)
        main = "sample.SampleJvmKt"
        doFirst {
            classpath(
                kotlin.targets["jvm"].compilations["main"].output.allOutputs.files,
                configurations["jvmRuntimeClasspath"]
            )
        }
        systemProperty("jsWeb", webFolder)
        ///disable app icon on macOS
        systemProperty("java.awt.headless", "true")
    }
}
