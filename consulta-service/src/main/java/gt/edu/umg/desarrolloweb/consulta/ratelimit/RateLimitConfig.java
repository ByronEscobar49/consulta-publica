package gt.edu.umg.desarrolloweb.consulta.ratelimit;

import java.time.Clock;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiter rateLimiter(Clock clock,
                                   @Value("${consulta.rate-limit.max-requests:30}") int maxRequests,
                                   @Value("${consulta.rate-limit.window-seconds:60}") int windowSeconds) {
        return new RateLimiter(maxRequests, Duration.ofSeconds(windowSeconds), clock);
    }
}
