-- Usuario minimo para que las llaves foraneas (lecturas.usuario_id, pagos.usuario_id,
-- periodos.cerrado_por, boletas.anulada_por) tengan a quien apuntar.
-- NO se copia la tabla users del monolito: tiene hashes de contrasenas y la replica
-- de consulta publica no las necesita ni debe guardarlas.
INSERT INTO users (id, name, email, password, created_at, updated_at)
VALUES (1, 'Administrador', 'admin@oficina-agua.test', '!sin-acceso', NOW(), NOW());
