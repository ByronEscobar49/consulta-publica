package gt.edu.umg.desarrolloweb.consulta.ratelimit;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Limitador por clave (aqui, la IP) con VENTANA DESLIZANTE: guarda la hora de cada consulta
 * reciente y permite como maximo {@code maxRequests} dentro de los ultimos {@code window}.
 *
 * <p>Se eligio ventana deslizante y no contador fijo por minuto porque este ultimo deja pasar
 * el doble (rafaga al final de un minuto + rafaga al inicio del siguiente).
 *
 * <p>Es en memoria: sirve para UNA instancia del servicio. Si se escalara a varias
 * instancias habria que mover el contador a algo compartido (por ejemplo Redis).
 * No depende de Spring: se prueba con un reloj falso.
 */
public class RateLimiter {

    /** Resultado de un intento. {@code retryAfterSeconds} solo importa si no fue permitido. */
    public record Decision(boolean allowed, long retryAfterSeconds) {
    }

    private static final int PURGE_EVERY = 500;

    private final int maxRequests;
    private final long windowMillis;
    private final Clock clock;
    private final Map<String, Deque<Long>> hitsByKey = new ConcurrentHashMap<>();
    private final AtomicLong calls = new AtomicLong();

    public RateLimiter(int maxRequests, Duration window, Clock clock) {
        if (maxRequests < 1 || window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException("maxRequests debe ser >= 1 y la ventana positiva");
        }
        this.maxRequests = maxRequests;
        this.windowMillis = window.toMillis();
        this.clock = clock;
    }

    public Decision tryAcquire(String key) {
        long now = clock.millis();
        if (calls.incrementAndGet() % PURGE_EVERY == 0) {
            purge(now);
        }

        Deque<Long> hits = hitsByKey.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (hits) {
            while (!hits.isEmpty() && hits.peekFirst() <= now - windowMillis) {
                hits.pollFirst();
            }
            if (hits.size() >= maxRequests) {
                long msUntilFree = hits.peekFirst() + windowMillis - now;
                return new Decision(false, Math.max(1, (msUntilFree + 999) / 1000));
            }
            hits.addLast(now);
            return new Decision(true, 0);
        }
    }

    /** Borra las IPs sin actividad reciente para que el mapa no crezca sin limite. */
    private void purge(long now) {
        hitsByKey.entrySet().removeIf(entry -> {
            Deque<Long> hits = entry.getValue();
            synchronized (hits) {
                return hits.isEmpty() || hits.peekLast() <= now - windowMillis;
            }
        });
    }
}
