plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

dependencies {
    // impl project
    implementation(project(":dms-persistence"))
    implementation(project(":dms-core"))
    implementation(project(":dms-presentation"))

    // validation
    implementation(Dependencies.SPRING_VALIDATION)

    // thymeleaf
    implementation(Dependencies.SPRING_THYMELEAF)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // jwt
    implementation(Dependencies.JWT)

    // aws
    implementation(Dependencies.AWS_SES)
    implementation(Dependencies.SPRING_AWS)

    // configuration
    kapt(Dependencies.CONFIGURATION_PROCESSOR)

    // excel
    implementation(Dependencies.APACHE_POI)
    implementation(Dependencies.APACHE_POI_OOXML)

    // open feign
    implementation(Dependencies.OPEN_FEIGN)

    // gson
    implementation(Dependencies.GSON)

    // jackson
    implementation(Dependencies.JACKSON_TYPE)

    // aop
    implementation(Dependencies.SPRING_AOP)

    // logging
    implementation(Dependencies.SENTRY)
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
        arg("mapstruct.unmappedTargetPolicy", "ignore")
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}