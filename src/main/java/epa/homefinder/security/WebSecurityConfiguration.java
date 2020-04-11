package epa.homefinder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import epa.homefinder.security.service.JwtUserDetailsService;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .antMatchers(HttpMethod.POST,"/getAdImages").authenticated()
                .antMatchers(HttpMethod.GET,"/trainings").permitAll()
//                .antMatchers(HttpMethod.POST,"/getAdReviews").permitAll()
//                .antMatchers(HttpMethod.POST,"/saveAdReview").permitAll()
                .antMatchers(HttpMethod.POST,"/getUserData").authenticated()
                .antMatchers(HttpMethod.POST,"/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers(HttpMethod.POST,"/newAd").authenticated()
                .antMatchers(HttpMethod.POST, "/getUserAds").authenticated()
                .antMatchers(HttpMethod.POST, "/saveEvent").authenticated()
                .antMatchers(HttpMethod.POST, "/updateEvent").authenticated()
                .antMatchers(HttpMethod.POST, "/updateUser").authenticated()
                // TODO: for local testing
//                    .antMatchers("/crapa").permitAll()
//                    .antMatchers("/pendingTrainings").permitAll()
//                    .antMatchers("/pendingUsers").permitAll()
//                    .antMatchers("/approveList").permitAll()
//                    .antMatchers("/subordinates").permitAll()
//                    .antMatchers("/subordinatesResult").permitAll()
//                    .antMatchers("/recommend").permitAll()
////                 this should be set later, only for testing
//                    .antMatchers(HttpMethod.GET, "/**").permitAll()
//                    .antMatchers(HttpMethod.POST, "/**").permitAll()
//                    .antMatchers(HttpMethod.PUT, "/**").permitAll()
//                    .antMatchers(HttpMethod.PATCH, "/**").permitAll()
//                    .antMatchers(HttpMethod.DELETE, "/**").permitAll()
                .and()
                .logout().permitAll();

        // Custom JWT based security filter
        JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenUtil, tokenHeader);
        http
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "cache-ontrol"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        // leave only /home and auth after frontend login complete
        web
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        authenticationPath,
                        "/signup",
                        "/newAdImages",
                        "/adsWithImages",
                        "/getAdImages",
                        "/getAdInfo",
                        "/replaceAdImages",
                        "/getAdReviews",
                        "/updateUserData",
                        "/saveAdReview",
                        "/getAdEvents"
                )

                // allow anonymous resource requests
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/adsWithImages",
                        "/getUserEmails",
                        "/userList",
                        "/eventsReport",
                        "/home"
                );
    }
}
