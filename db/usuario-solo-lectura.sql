-- Usuario de BD que usa consulta-service sobre la base del monolito.
-- Regla de oro de la Fase 2: el monolito no se toca. Este usuario SOLO puede leer
-- las tablas que la consulta publica necesita; no ve users, roles, audits ni documentos.
-- Si el servicio tuviera un bug (o alguien lo atacara), la base no se puede modificar.
CREATE USER IF NOT EXISTS 'consulta_ro'@'%' IDENTIFIED BY 'consulta_ro_pass';

GRANT SELECT ON gestion_agua.contadores   TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.clientes     TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.predios      TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.sectores     TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.pajas        TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.periodos     TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.lecturas     TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.boletas      TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.pagos        TO 'consulta_ro'@'%';
GRANT SELECT ON gestion_agua.metodos_pago TO 'consulta_ro'@'%';

FLUSH PRIVILEGES;
