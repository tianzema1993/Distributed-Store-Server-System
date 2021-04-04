DROP TABLE IF EXISTS StoreItemRecord;

CREATE TABLE StoreItemRecord (
  id INT AUTO_INCREMENT PRIMARY KEY,
  storeId INT,
  itemId INT,
  amount INT
);