package se.bth.pulse.security;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import javax.sql.DataSource;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import se.bth.pulse.service.UserDetailsServiceImpl;
import se.bth.pulse.utility.ReportJob;

/**
 * ApplicationSecurity class. Configures the security for the application. Also sets the password
 * encoder and user details service. This class configures CSRF protection.
 */
@Configuration
@EnableWebSecurity()
public class ApplicationSecurity {

  @Bean
  MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
    return new MvcRequestMatcher.Builder(introspector);
  }

  /**
   * Configures the security for the application. Including CSRF protection, authorization and
   * authentication. Using paths it sets who is allowed to access what.
   *
   * @param httpSecurity HttpSecurity
   * @param mvc          MvcRequestMatcher.Builder
   * @return SecurityFilterChain
   * @throws Exception Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc)
      throws Exception {
    httpSecurity
        .csrf((csrf) -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                antMatcher(HttpMethod.GET, "/api/admin/**"),
                antMatcher(HttpMethod.POST, "/api/admin/**"),
                antMatcher(HttpMethod.GET, "/swagger-ui/**"),
                antMatcher(HttpMethod.GET, "/swagger-ui"),
                mvc.pattern("/admin/**")

            )
            .hasAuthority("admin")
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                antMatcher("/api/setup/**"),
                antMatcher("/error*"),
                antMatcher("/login"),
                antMatcher("/WEB-INF/jsp/public/**"),
                antMatcher("/WEB-INF/jsp/include/**"),
                antMatcher("/WEB-INF/jsp/include/pages/**"),
                antMatcher("/WEB-INF/public/**"),
                antMatcher("/public/css/**"),
                antMatcher("/public/js/**"),
                mvc.pattern("/setup"),
                mvc.pattern("/login")
            )
            .permitAll()
        )
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/", false)
            .successHandler(new PasswordChangeAuthenticationSuccessHandler())
        )
        .httpBasic(withDefaults());

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  /**
   * Configures the authentication provider. Sets our user details service to be used and Bcrypt
   * password encoder. Returns a customized authentication provider.
   *
   * @return DaoAuthenticationProvider
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * This configures a spring bean to use our customized authentication provider. It needs to be set
   * as Primary to be used, otherwise it will use the default one.
   *
   * @param auth AuthenticationManagerBuilder
   * @return AuthenticationManagerBuilder
   */
  @Bean
  @Primary
  protected AuthenticationManagerBuilder configure(AuthenticationManagerBuilder auth) {
    return auth.authenticationProvider(authenticationProvider());
  }

}