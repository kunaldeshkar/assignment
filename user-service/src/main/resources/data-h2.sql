DROP TABLE IF EXISTS user_profile;

CREATE TABLE user_profile (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_name VARCHAR(50) NOT NULL,
  address VARCHAR(250) NOT NULL,
  phone_number VARCHAR(20) NOT NULL
);

-- INSERT INTO user_profile (user_name, address, phone_number) VALUES
-- ('robert', 'aaa', '0123456789'),
-- ('peter', 'bbb', '01234543224'),
-- ('jason', 'ccc', '0111111111');