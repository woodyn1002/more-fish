import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    kotlin("jvm") version "1.4.32"
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

group = "me.elsiff"
version = "3.0.3-SNAPSHOT"

val pluginName = "MoreFish"
val mainPackage = "me.elsiff.morefish"
val mainClass = "$mainPackage.MoreFish"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("http://nexus.hc.to/content/repositories/pub_releases").isAllowInsecureProtocol = true
    maven("http://repo.citizensnpcs.co/").isAllowInsecureProtocol = true
    maven("http://repo.extendedclip.com/content/repositories/placeholderapi/").isAllowInsecureProtocol = true
    maven("http://repo.dmulloy2.net/nexus/repository/public/").isAllowInsecureProtocol = true
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:${getKotlinPluginVersion()}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${getKotlinPluginVersion()}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.1.0")
    testCompileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    implementation("com.github.elsiff:egui:1.0.2-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.4")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
    compileOnly("net.citizensnpcs:citizensapi:2.0.20-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.10.9")
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.195")
}

bukkit {
    main = mainClass
    softDepend = listOf("Vault", "Citizens", "PlaceholderAPI", "mcMMO", "WorldGuard")
    author = "elsiff"
    website = "https://elsiff.me"
    apiVersion = "1.16"
}

tasks {
    ktlintFormat
    compileKotlin {

        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    shadowJar {
        relocate("co.aikar.commands", "$mainPackage.acf")
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}

configurations {
    ktlint
}
