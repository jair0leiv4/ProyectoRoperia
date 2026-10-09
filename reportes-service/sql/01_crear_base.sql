-- Solo necesario si NO usas Docker (MySQL instalado localmente).
CREATE DATABASE IF NOT EXISTS reportes_db CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'demo'@'%' IDENTIFIED BY 'Demo123!';
GRANT ALL PRIVILEGES ON reportes_db.* TO 'demo'@'%';
FLUSH PRIVILEGES;
