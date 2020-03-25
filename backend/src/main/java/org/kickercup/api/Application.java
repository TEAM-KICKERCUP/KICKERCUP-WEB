//----------------------------------------------------------------------------------
//     Created By: Christopher Heid (Project Scaffolding)
// Contributor(s): Johannes Schweer, Jonas Jahns, Moritz Lugbauer (Bean Registrations)
//    Description: Main Application, Springboot Initalizer,  Beans, CORS Filter 
//----------------------------------------------------------------------------------
package org.kickercup.api;

import java.util.Arrays;
import java.util.Collections;
import org.kickercup.api.services.impl.MatchService;
import org.kickercup.api.services.impl.TeamService;
import org.kickercup.api.services.impl.TournamentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ComponentScan(basePackages = "org.kickercup.api")
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    @Bean
    public CorsFilter corsFilter() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            final CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedOrigins(Collections.singletonList("*"));
            config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
            source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
    }

    @Bean 
    public TournamentService getTournamentService(){
        return new TournamentService();
    } 

    @Bean
    public MatchService getMatchService(){
        return new MatchService();
    }

    @Bean
    public TeamService getTeamService(){
        return new TeamService();
    }

}
    
    
    
