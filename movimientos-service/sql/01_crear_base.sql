-- Solo necesario si NO usas Docker (MySQL instalado localmente).
CREATE DATABASE IF NOT EXISTS movimientos_db CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'demo'@'%' IDENTIFIED BY 'Demo123!';
GRANT ALL PRIVILEGES ON movimientos_db.* TO 'demo'@'%';
FLUSH PRIVILEGES;
