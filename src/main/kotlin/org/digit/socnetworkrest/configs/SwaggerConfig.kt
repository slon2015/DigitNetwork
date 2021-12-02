package org.digit.socnetworkrest.configs

import com.google.common.base.Predicates
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.AuthorizationCodeGrantBuilder
import springfox.documentation.builders.AuthorizationScopeBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.security.Principal
import java.time.LocalDate


@Configuration
@EnableSwagger2
class SwaggerConfig(
    @Value("\${keycloak.auth-server-url}") val keycloakUrl: String,
    @Value("\${keycloak.resource}") val clientId: String,
    @Value("\${keycloak.credentials.secret}") val clientSecret: String,
    @Value("\${keycloak.realm}") val realm: String
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val FEEDS_TAG = "Взаимодействие с постами"
        const val PROFILES_TAG = "Взаимодействие с профилями"
        const val SECURITY_SCHEME = "oauth2"
    }

    @Bean
    fun apiSpec(): Docket {

        return Docket(DocumentationType.SWAGGER_2)
            .securitySchemes(buildSecurityScheme())
            .select()
            .paths(PathSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("org.digit.socnetworkrest.api"))
            .build()
            .ignoredParameterTypes(Principal::class.java, Pageable::class.java)
            .directModelSubstitute(LocalDate::class.java, String::class.java)
            .genericModelSubstitutes(ResponseEntity::class.java)
            .useDefaultResponseMessages(true)
            .enableUrlTemplating(true)
            .tags(
                Tag(FEEDS_TAG, ""),
                Tag(PROFILES_TAG, "")
            )
    }

    @Bean
    fun securityConfiguration(): SecurityConfiguration {
        return SecurityConfigurationBuilder.builder()
            .clientId(clientId).realm(realm).appName("swagger-ui")
            .build()
    }

    private fun buildSecurityScheme(): List<SecurityScheme> {
        val lst: MutableList<SecurityScheme> = ArrayList()
        val gTypes: MutableList<GrantType> = ArrayList()
        gTypes.add(AuthorizationCodeGrantBuilder()
            .tokenRequestEndpoint {
                it.clientIdName(clientId)
                    .clientSecretName(clientSecret)
                    .url("$keycloakUrl/realms/$realm/protocol/openid-connect/auth")
            }
            .tokenEndpoint {
                it.url("$keycloakUrl/realms/$realm/protocol/openid-connect/token")
                    .tokenName("Keycloak token")
            }
            .build())
        lst.add(OAuth(SECURITY_SCHEME, scopes().toList(), gTypes))
        return lst
    }

    private fun scopes(): Array<AuthorizationScope> {
        val scopes: MutableList<AuthorizationScope> = ArrayList()
        for (scopeItem in arrayOf("openid=openid", "profile=profile")) {
            val scope = scopeItem.split("=").toTypedArray()
            if (scope.size == 2) {
                scopes.add(AuthorizationScopeBuilder().scope(scope[0]).description(scope[1]).build())
            } else {
                log.warn("Scope '{}' is not valid (format is scope=description)", scopeItem)
            }
        }
        return scopes.toTypedArray()
    }
}