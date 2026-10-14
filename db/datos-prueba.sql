-- ============================================================================
-- Datos de prueba para consulta-publica (Feature 1)
-- Se carga UNA sola vez sobre la base del monolito recien instalada
-- (migrate:fresh --seed). No es idempotente: para repetirlo, reinstala la base.
--
-- Casos que cubre:
--   CTR-0001  al dia (pagado jul/ago/sep)
--   CTR-0002  pago de agosto REVERTIDO -> boleta pendiente/vencida
--   CTR-0003  agosto sin pagar -> VENCIDA
--   CTR-0004  agosto con pago PARCIAL
--   CTR-0005  septiembre ANULADA; mismo cliente que CTR-0001 (2 predios)
--   CTR-0006  contador inactivo, sin lecturas, cliente inactivo
--   CTR-0007  contador BORRADO logicamente (debe dar 404)
-- Todos los NIT/DPI/telefonos son inventados.
-- ============================================================================

SET NAMES utf8mb4;

-- Referencias a catalogos que ya siembra el monolito (no dependemos de sus ids)
SET @admin  = (SELECT id FROM users ORDER BY id LIMIT 1);
SET @paja1  = (SELECT id FROM pajas ORDER BY id LIMIT 1);
SET @paja2  = (SELECT id FROM pajas ORDER BY id LIMIT 1 OFFSET 1);
SET @paja3  = (SELECT id FROM pajas ORDER BY id LIMIT 1 OFFSET 2);
SET @mp1    = (SELECT id FROM metodos_pago ORDER BY id LIMIT 1);
SET @mp2    = (SELECT id FROM metodos_pago ORDER BY id LIMIT 1 OFFSET 1);
SET @serie_boleta = (SELECT id FROM series_documento WHERE tipo_documento = 'boleta'      AND activa = 1 ORDER BY id LIMIT 1);
SET @serie_recibo = (SELECT id FROM series_documento WHERE tipo_documento = 'recibo_pago' AND activa = 1 ORDER BY id LIMIT 1);
SET @ej_boleta = (SELECT ejercicio FROM series_documento WHERE id = @serie_boleta);
SET @ej_recibo = (SELECT ejercicio FROM series_documento WHERE id = @serie_recibo);
SET @b0 = (SELECT siguiente_numero FROM series_documento WHERE id = @serie_boleta) - 1;
SET @r0 = (SELECT siguiente_numero FROM series_documento WHERE id = @serie_recibo) - 1;

-- ---------------------------------------------------------------------------
-- Sectores y periodos (el seeder solo abre el periodo del mes en curso)
-- ---------------------------------------------------------------------------
INSERT IGNORE INTO sectores (nombre, descripcion, orden, activo, created_at, updated_at) VALUES
 ('Sector Centro',     'Casco urbano',          1, 1, NOW(), NOW()),
 ('Aldea El Porvenir', 'Zona rural al norte',   2, 1, NOW(), NOW());

INSERT IGNORE INTO periodos (anio, mes, fecha_inicio, fecha_fin, created_at, updated_at) VALUES
 (2026, 7, '2026-07-01', '2026-07-31', NOW(), NOW()),
 (2026, 8, '2026-08-01', '2026-08-31', NOW(), NOW()),
 (2026, 9, '2026-09-01', '2026-09-30', NOW(), NOW());

-- ---------------------------------------------------------------------------
-- Tarifas: vigentes desde enero; la paja 1 sube de precio el 1 de septiembre
-- ---------------------------------------------------------------------------
INSERT INTO tarifas (paja_id, monto_base, precio_m3_excedente, vigente_desde, created_at, updated_at) VALUES
 (@paja1, 60.00, 3.0000, '2026-01-01', NOW(), NOW()),
 (@paja2, 35.00, 3.0000, '2026-01-01', NOW(), NOW()),
 (@paja3, 20.00, 3.0000, '2026-01-01', NOW(), NOW()),
 (@paja1, 66.00, 3.3000, '2026-09-01', NOW(), NOW());

-- ---------------------------------------------------------------------------
-- Clientes (NIT/DPI/telefono/email existen pero la consulta publica NO los expone)
-- ---------------------------------------------------------------------------
INSERT INTO clientes (codigo, nombre, nit, dpi, telefono, email, direccion_notificacion, estado, created_at, updated_at) VALUES
 ('CLI-0001', 'María Fernanda López Castillo', '9000001-1', '2000000010101', '50255550001', 'maria.lopez@example.com',  '4a calle 5-20 zona 1',        'activo',   NOW(), NOW()),
 ('CLI-0002', 'José Antonio Pérez Morales',    '9000002-2', '2000000020101', '50255550002', 'jose.perez@example.com',   '10a avenida 3-45 zona 10',    'activo',   NOW(), NOW()),
 ('CLI-0003', 'Ana Lucía García Ramos',        '9000003-3', '2000000030101', '50255550003', NULL,                       'Aldea El Porvenir, casa 12',  'activo',   NOW(), NOW()),
 ('CLI-0004', 'Carlos Eduardo Ramírez Solís',  '9000004-4', '2000000040101', '50255550004', 'carlos.r@example.com',     '2a calle 1-10 zona 3',        'activo',   NOW(), NOW()),
 ('CLI-0005', 'Luisa Marina Méndez Ortiz',     '9000005-5', '2000000050101', NULL,          NULL,                       'Sector Centro, casa 7',       'inactivo', NOW(), NOW());

-- Predios (CLI-0001 tiene dos)
INSERT INTO predios (sector_id, aldea, zona, calle, numero_casa, referencia, created_at, updated_at) VALUES
 ((SELECT id FROM sectores WHERE nombre = 'Sector Centro'),     NULL,         '1', '4a calle',  '5-20', 'frente al parque',        NOW(), NOW()),
 ((SELECT id FROM sectores WHERE nombre = 'Sector Centro'),     NULL,         '10','10a avenida','3-45','esquina con tienda',     NOW(), NOW()),
 ((SELECT id FROM sectores WHERE nombre = 'Aldea El Porvenir'), 'El Porvenir','0', NULL,        '12',   'junto a la escuela',     NOW(), NOW()),
 ((SELECT id FROM sectores WHERE nombre = 'Sector Centro'),     NULL,         '3', '2a calle',  '1-10', NULL,                     NOW(), NOW()),
 ((SELECT id FROM sectores WHERE nombre = 'Aldea El Porvenir'), 'El Porvenir','0', NULL,        '31',   'lote de la familia López', NOW(), NOW()),
 ((SELECT id FROM sectores WHERE nombre = 'Sector Centro'),     NULL,         '1', '6a calle',  '7-02', NULL,                     NOW(), NOW());

-- Contadores (predios en el orden de insercion: 1..6)
SET @p1 = (SELECT MIN(id) FROM predios);
INSERT INTO contadores (predio_id, cliente_id, paja_id, codigo, fecha_instalacion, estado, deleted_at, created_at, updated_at) VALUES
 (@p1,     (SELECT id FROM clientes WHERE codigo = 'CLI-0001'), @paja1, 'CTR-0001', '2024-03-10', 'activo',   NULL,  NOW(), NOW()),
 (@p1 + 1, (SELECT id FROM clientes WHERE codigo = 'CLI-0002'), @paja1, 'CTR-0002', '2024-04-02', 'activo',   NULL,  NOW(), NOW()),
 (@p1 + 2, (SELECT id FROM clientes WHERE codigo = 'CLI-0003'), @paja2, 'CTR-0003', '2024-06-18', 'activo',   NULL,  NOW(), NOW()),
 (@p1 + 3, (SELECT id FROM clientes WHERE codigo = 'CLI-0004'), @paja2, 'CTR-0004', '2025-01-20', 'activo',   NULL,  NOW(), NOW()),
 (@p1 + 4, (SELECT id FROM clientes WHERE codigo = 'CLI-0001'), @paja3, 'CTR-0005', '2025-05-05', 'activo',   NULL,  NOW(), NOW()),
 (@p1 + 5, (SELECT id FROM clientes WHERE codigo = 'CLI-0005'), @paja1, 'CTR-0006', '2023-11-30', 'inactivo', NULL,  NOW(), NOW()),
 (@p1,     (SELECT id FROM clientes WHERE codigo = 'CLI-0003'), @paja1, 'CTR-0007', '2022-08-01', 'activo',   NOW(), NOW(), NOW());

-- Que el monolito siga numerando sin chocar con estos codigos
UPDATE correlativos SET siguiente_numero = 6 WHERE clave = 'cliente';
UPDATE correlativos SET siguiente_numero = 8 WHERE clave = 'contador';

-- ---------------------------------------------------------------------------
-- Lecturas: julio, agosto y septiembre para CTR-0001..0005
-- (lectura_anterior de cada mes = lectura_actual del mes anterior)
-- consumo_m3 es columna generada: NO se inserta
-- ---------------------------------------------------------------------------
INSERT INTO lecturas (contador_id, periodo_id, usuario_id, lectura_anterior, lectura_actual, fecha_lectura, created_at, updated_at)
SELECT c.id, p.id, @admin, v.anterior, v.actual, DATE_ADD(p.fecha_inicio, INTERVAL 28 DAY), NOW(), NOW()
FROM (
  SELECT 'CTR-0001' codigo, 7 mes, 100.00 anterior, 118.00 actual UNION ALL
  SELECT 'CTR-0001', 8, 118.00, 139.00 UNION ALL
  SELECT 'CTR-0001', 9, 139.00, 160.00 UNION ALL
  SELECT 'CTR-0002', 7, 250.00, 275.00 UNION ALL
  SELECT 'CTR-0002', 8, 275.00, 302.00 UNION ALL
  SELECT 'CTR-0002', 9, 302.00, 331.00 UNION ALL
  SELECT 'CTR-0003', 7,  40.00,  52.00 UNION ALL
  SELECT 'CTR-0003', 8,  52.00,  70.00 UNION ALL
  SELECT 'CTR-0003', 9,  70.00,  85.00 UNION ALL
  SELECT 'CTR-0004', 7, 500.00, 540.00 UNION ALL
  SELECT 'CTR-0004', 8, 540.00, 585.00 UNION ALL
  SELECT 'CTR-0004', 9, 585.00, 641.00 UNION ALL
  SELECT 'CTR-0005', 7,  10.00,  22.00 UNION ALL
  SELECT 'CTR-0005', 8,  22.00,  40.00 UNION ALL
  SELECT 'CTR-0005', 9,  40.00,  61.00
) v
JOIN contadores c ON c.codigo = v.codigo
JOIN periodos   p ON p.anio = 2026 AND p.mes = v.mes;

-- ---------------------------------------------------------------------------
-- Boletas: una por lectura, con la tarifa vigente a la fecha de lectura.
-- excedente = max(consumo - m3 incluidos en la paja, 0) * precio por m3
-- ---------------------------------------------------------------------------
INSERT INTO boletas (serie_id, ejercicio, numero, folio, cliente_id, lectura_id, tarifa_id, periodo_id,
                     consumo_m3, monto_base, monto_excedente, monto, fecha_emision, fecha_vencimiento, created_at, updated_at)
SELECT @serie_boleta, @ej_boleta,
       @b0 + ROW_NUMBER() OVER (ORDER BY l.periodo_id, l.contador_id),
       CONCAT('B-', @ej_boleta, '-', LPAD(@b0 + ROW_NUMBER() OVER (ORDER BY l.periodo_id, l.contador_id), 6, '0')),
       c.cliente_id, l.id, t.id, l.periodo_id,
       l.consumo_m3, t.monto_base,
       ROUND(GREATEST(l.consumo_m3 - pj.equivalencia_m3, 0) * t.precio_m3_excedente, 2),
       t.monto_base + ROUND(GREATEST(l.consumo_m3 - pj.equivalencia_m3, 0) * t.precio_m3_excedente, 2),
       DATE_ADD(l.fecha_lectura, INTERVAL 1 DAY),
       DATE_ADD(l.fecha_lectura, INTERVAL 16 DAY),
       NOW(), NOW()
FROM lecturas l
JOIN contadores c  ON c.id = l.contador_id
JOIN pajas      pj ON pj.id = c.paja_id
JOIN tarifas    t  ON t.id = (SELECT t2.id FROM tarifas t2
                              WHERE t2.paja_id = c.paja_id AND t2.vigente_desde <= l.fecha_lectura
                              ORDER BY t2.vigente_desde DESC LIMIT 1)
ORDER BY l.periodo_id, l.contador_id;

UPDATE series_documento SET siguiente_numero = siguiente_numero + (SELECT COUNT(*) FROM boletas) WHERE id = @serie_boleta;

-- Boleta anulada: septiembre de CTR-0005
UPDATE boletas b
JOIN lecturas l ON l.id = b.lectura_id
JOIN contadores c ON c.id = l.contador_id
JOIN periodos p ON p.id = l.periodo_id
SET b.anulada_en = NOW(), b.anulada_por = @admin, b.motivo_anulacion = 'Lectura mal digitada, se emitira de nuevo'
WHERE c.codigo = 'CTR-0005' AND p.mes = 9;

-- ---------------------------------------------------------------------------
-- Pagos
--   julio: todo pagado | agosto: todos menos CTR-0003; CTR-0004 paga la mitad
--   septiembre: solo CTR-0001
-- ---------------------------------------------------------------------------
INSERT INTO pagos (serie_id, ejercicio, numero, folio, boleta_id, metodo_pago_id, usuario_id,
                   monto, fecha_pago, referencia, created_at, updated_at)
SELECT @serie_recibo, @ej_recibo,
       @r0 + ROW_NUMBER() OVER (ORDER BY b.numero),
       CONCAT('R-', @ej_recibo, '-', LPAD(@r0 + ROW_NUMBER() OVER (ORDER BY b.numero), 6, '0')),
       b.id,
       CASE WHEN p.mes = 7 THEN @mp1 ELSE @mp2 END,
       @admin,
       CASE WHEN p.mes = 8 AND c.codigo = 'CTR-0004' THEN ROUND(b.monto / 2, 2) ELSE b.monto END,
       DATE_ADD(b.fecha_emision, INTERVAL 3 DAY),
       CASE WHEN p.mes = 7 THEN NULL ELSE CONCAT('REF-', b.folio) END,
       NOW(), NOW()
FROM boletas b
JOIN lecturas   l ON l.id = b.lectura_id
JOIN contadores c ON c.id = l.contador_id
JOIN periodos   p ON p.id = b.periodo_id
WHERE b.anulada_en IS NULL
  AND (   p.mes = 7
       OR (p.mes = 8 AND c.codigo <> 'CTR-0003')
       OR (p.mes = 9 AND c.codigo = 'CTR-0001'))
ORDER BY b.numero;

UPDATE series_documento SET siguiente_numero = siguiente_numero + (SELECT COUNT(*) FROM pagos) WHERE id = @serie_recibo;

-- Pago revertido: agosto de CTR-0002 (la boleta vuelve a deberse)
UPDATE pagos pg
JOIN boletas b ON b.id = pg.boleta_id
JOIN lecturas l ON l.id = b.lectura_id
JOIN contadores c ON c.id = l.contador_id
JOIN periodos p ON p.id = b.periodo_id
SET pg.revertido_en = NOW(), pg.revertido_por = @admin, pg.motivo_reverso = 'Cheque rechazado por el banco'
WHERE c.codigo = 'CTR-0002' AND p.mes = 8;

-- Cierre de periodos viejos (julio y agosto)
UPDATE periodos SET cerrado_en = NOW(), cerrado_por = @admin WHERE anio = 2026 AND mes IN (7, 8);

-- ---------------------------------------------------------------------------
-- Resumen para verificar: estado de cada boleta (derivado, igual que hara el servicio)
-- ---------------------------------------------------------------------------
SELECT c.codigo AS contador, p.mes, b.folio, b.consumo_m3, b.monto,
       COALESCE((SELECT SUM(pg.monto) FROM pagos pg WHERE pg.boleta_id = b.id AND pg.revertido_en IS NULL), 0) AS pagado,
       CASE
         WHEN b.anulada_en IS NOT NULL THEN 'ANULADA'
         WHEN COALESCE((SELECT SUM(pg.monto) FROM pagos pg WHERE pg.boleta_id = b.id AND pg.revertido_en IS NULL), 0) >= b.monto THEN 'PAGADA'
         WHEN b.fecha_vencimiento < CURDATE() THEN 'VENCIDA'
         ELSE 'PENDIENTE'
       END AS estado
FROM boletas b
JOIN lecturas   l ON l.id = b.lectura_id
JOIN contadores c ON c.id = l.contador_id
JOIN periodos   p ON p.id = b.periodo_id
ORDER BY c.codigo, p.mes;
