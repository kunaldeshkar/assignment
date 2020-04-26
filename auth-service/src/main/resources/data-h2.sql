DROP TABLE IF EXISTS user_credentials;

CREATE TABLE user_credentials (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_name VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL
);

INSERT INTO user_credentials (user_name, password) VALUES
('robert', '123456'),
('peter', '111111'),
('jason', '666666');