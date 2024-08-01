package comp.finalproject.admin.config;

import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.service.web.UserServiceImpl;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class JaversConfig {
    private final UserServiceImpl userService;
    public JaversConfig(UserServiceImpl userService) {
        this.userService = userService;
    }
    @Bean
    public AuthorProvider provideJaversAuthor() {
        return new SpringSecurityAuthorProvider(userService);
    }
    private static class SpringSecurityAuthorProvider implements AuthorProvider {
        private final UserServiceImpl userService;
        public SpringSecurityAuthorProvider(UserServiceImpl userService) {
            this.userService = userService;
        }
        @Override
        public String provide() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || auth.getName() == null) {
                return "unauthenticated";
            }

            // Contoh: Menggunakan userService untuk mendapatkan informasi lebih lanjut
            String username = auth.getName();
            User user = userService.findUserByEmail(username);
            return user != null ? user.getName() : "unknown";
        }
    }
}
