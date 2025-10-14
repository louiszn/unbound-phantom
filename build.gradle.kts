plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
}

group = project.properties["maven_group"] as String;
version = project.properties["mod_version"] as String;

repositories {
    mavenCentral()
}

loom {}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")
}

tasks.test {}
