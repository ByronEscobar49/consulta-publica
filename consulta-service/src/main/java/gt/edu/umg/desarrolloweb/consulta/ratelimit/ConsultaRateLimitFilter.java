package gt.edu.umg.desarrolloweb.consulta.ratelimit;

import gt.edu.umg.desarrolloweb.consulta.api.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Segunda barrera contra el abuso (la primera es el captcha, que valida el BFF).
 * Corre ANTES de llegar al controller y solo aplica a /api/. Si la IP se pasa del limite
 * responde 429 con la cabecera Retry-After, tal como lo declara el contrato.
 *
 * <p>Es un filtro (y no un @ExceptionHandler) porque se ejecuta antes de Spring MVC; por eso
 * arma el JSON de error a mano con el mismo esquema "Error" del contrato.
 */
@Component
public class ConsultaRateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ConsultaRateLimitFilter.class);

    private final RateLimiter rateLimiter;
    private final ObjectMapper objectMapper;
    private final boolean enabled;
    private final boolean trustForwardedFor;

    public ConsultaRateLimitFilter(RateLimiter rateLimiter,
                                   ObjectMapper objectMapper,
                                   @Value("${consulta.rate-limit.enabled:true}") boolean enabled,
                                   @Value("${consulta.rate-limit.trust-forwarded-for:true}") boolean trustForwardedFor) {
        this.rateLimiter = rateLimiter;
        this.objectMapper = objectMapper;
        this.enabled = enabled;
        this.trustForwardedFor = trustForwardedFor;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !enabled || !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String ip = ClientIp.resolve(request.getHeader("X-Forwarded-For"), request.getRemoteAddr(), trustForwardedFor);
        RateLimiter.Decision decision = rateLimiter.tryAcquire(ip);

        if (decision.allowed()) {
            chain.doFilter(request, response);
            return;
        }

        log.warn("Limite de consultas excedido para la IP {}", ip);
        response.setStatus(429);
        response.setHeader("Retry-After", String.valueOf(decision.retryAfterSeconds()));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorDto body = new ErrorDto(429, "Demasiadas consultas. Intenta de nuevo en unos segundos")
                .details(List.of());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
