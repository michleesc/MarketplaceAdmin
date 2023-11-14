package comp.finalproject.admin.config;

import org.javers.spring.auditable.AuthorProvider;
import org.javers.spring.auditable.SpringSecurityAuthorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class JaversConfig {
    @Bean
    public AuthorProvider provideJaversAuthor() {
        return new SpringSecurityAuthorProvider();
    }

    private static class SpringSecurityAuthorProvider implements AuthorProvider {
        @Override
        public String provide() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return "unauthenticated";
            }
            return auth.getName();
        }
    }
}
