package org.digit.socnetworkrest.configs

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    @Value("\${keycloak.auth-server-url}") val keycloakUrl: String,
    @Value("\${keycloak.resource}") val clientId: String,
    @Value("\${keycloak.credentials.secret}") val clientSecret: String,
    @Value("\${keycloak.realm}") val realm: String
): KeycloakWebSecurityConfigurerAdapter() {

    @Autowired
    fun configureGlobal(
        auth: AuthenticationManagerBuilder
    ) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
            SimpleAuthorityMapper()
        )
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    fun keycloakConfigResolver(): KeycloakSpringBootConfigResolver? {
        return KeycloakSpringBootConfigResolver()
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy? {
        return RegisterSessionAuthenticationStrategy(
            SessionRegistryImpl()
        )
    }

    companion object {
        const val USER_ROLE = "RegularUser"
    }

    val publicResources = arrayOf(
        "/",
        "/h2-console/**",
        "/profile/public/*"
    )

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.authorizeRequests()
            .antMatchers(*publicResources)
                .permitAll()
            .antMatchers("/profile/register")
                .anonymous()
            .antMatchers("/profile/**")
                .hasRole(USER_ROLE)
            .antMatchers(HttpMethod.POST, "/feed")
                .hasRole(USER_ROLE)
            .anyRequest()
                .permitAll()
            .and()
            .csrf().disable()
            .logout {
                it.logoutUrl("$keycloakUrl/realms/$realm/protocol/openid-connect/logout")
            }
            .formLogin().disable()
            .httpBasic().disable()
            .headers().frameOptions().disable()
    }
}