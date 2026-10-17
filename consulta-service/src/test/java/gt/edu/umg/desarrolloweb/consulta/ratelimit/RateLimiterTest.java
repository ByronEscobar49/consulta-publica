package gt.edu.umg.desarrolloweb.consulta.ratelimit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class RateLimiterTest {

    private final MutableClock clock = new MutableClock();
    private final RateLimiter limiter = new RateLimiter(3, Duration.ofSeconds(60), clock);

    @Test
    void permiteHastaElMaximoYLuegoBloquea() {
        assertEquals(true, limiter.tryAcquire("1.1.1.1").allowed());
        assertEquals(true, limiter.tryAcquire("1.1.1.1").allowed());
        assertEquals(true, limiter.tryAcquire("1.1.1.1").allowed());
        assertEquals(false, limiter.tryAcquire("1.1.1.1").allowed());
    }

    @Test
    void cadaIpTieneSuPropioContador() {
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire("1.1.1.1");
        }
        assertEquals(false, limiter.tryAcquire("1.1.1.1").allowed());
        assertEquals(true, limiter.tryAcquire("2.2.2.2").allowed());
    }

    @Test
    void retryAfterIndicaCuantoFaltaParaLiberarseElCupo() {
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire("1.1.1.1");
        }
        clock.advance(Duration.ofSeconds(20));
        RateLimiter.Decision d = limiter.tryAcquire("1.1.1.1");
        assertEquals(false, d.allowed());
        assertEquals(40L, d.retryAfterSeconds());
    }

    @Test
    void trasLaVentanaVuelveAPermitir() {
        for (int i = 0; i < 3; i++) {
            limiter.tryAcquire("1.1.1.1");
        }
        clock.advance(Duration.ofSeconds(61));
        assertEquals(true, limiter.tryAcquire("1.1.1.1").allowed());
    }

    @Test
    void laVentanaEsDeslizanteLiberaDeAUno() {
        limiter.tryAcquire("1.1.1.1");              // t = 0
        clock.advance(Duration.ofSeconds(30));
        limiter.tryAcquire("1.1.1.1");              // t = 30
        limiter.tryAcquire("1.1.1.1");              // t = 30  (3 de 3)
        assertEquals(false, limiter.tryAcquire("1.1.1.1").allowed());
        clock.advance(Duration.ofSeconds(31));      // t = 61: solo expiro la de t = 0
        assertEquals(true, limiter.tryAcquire("1.1.1.1").allowed());
        assertEquals(false, limiter.tryAcquire("1.1.1.1").allowed());
    }
}
