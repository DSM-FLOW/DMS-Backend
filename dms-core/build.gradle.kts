plugins {
    kotlin("plugin.allopen") version PluginVersions.ALLOPEN_VERSION
}

dependencies {

    // transaction
    implementation(Dependencies.SPRING_TRANSACTION)

    // aop
    implementation(Dependencies.AOP)



}

allOpen {
    annotation("team.aliens.dms.common.annotation.UseCase")
    annotation("team.aliens.dms.common.annotation.ReadOnlyUseCase")
    annotation("team.aliens.dms.common.annotation.SchedulerUseCase")
    annotation("team.aliens.dms.common.annotation.Service")
}