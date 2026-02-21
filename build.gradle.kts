plugins {
    id("java")
}

group = "fr.lostera"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.java.dev.jna:jna:5.18.1")
    implementation("net.java.dev.jna:jna-platform:5.18.1")

    implementation("com.github.kwhat:jnativehook:2.2.2")

    implementation("net.engio:mbassador:1.3.2")

    implementation("io.github.spair:imgui-java-app:1.90.0")
    implementation("io.github.spair:imgui-java-binding:1.90.0")
    implementation("io.github.spair:imgui-java-lwjgl3:1.90.0")
    implementation("io.github.spair:imgui-java-natives-windows:1.90.0")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}