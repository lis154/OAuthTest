package ru.lis154.OAuthTest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.lis154.OAuthTest.services.AuthProvider;
import javax.servlet.Filter;


@EnableOAuth2Client
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthProvider authProvider;

    @Bean
    PasswordEncoder passwordEncoder()
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/", "/login**", "/registration").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/notes").failureUrl("/login?error").permitAll()
                .and().logout().logoutSuccessUrl("/").permitAll();
        http
                .addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter)
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);
        return registration;
    }
    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google()
    {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource()
    {
        return new ResourceServerProperties();
    }

    private Filter ssoFilter()
    {
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        CustomUserInfoTokenServices tokenServices = new CustomUserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        tokenServices.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(tokenServices);
        tokenServices.setUserRepo(userRepo);
        tokenServices.setPasswordEncoder(passwordEncoder);
        return googleFilter;
    }

}
