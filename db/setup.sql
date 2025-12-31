CREATE DATABASE smartdrive_db;
GO

USE smartdrive_db;
GO

CREATE TABLE cars (
    id INT IDENTITY(1,1) PRIMARY KEY, -- AUTO_INCREMENT yerine bu kullanılır
    brand VARCHAR(50),
    model VARCHAR(50),
    year INT,
    price_per_day FLOAT -- MSSQL'de DOUBLE yerine FLOAT veya DECIMAL tercih edilir
);
GO