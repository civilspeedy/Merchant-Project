# Database Schema

Created by mistral vibe

## Users Table

| Column   | Type          | Constraints                 |
| -------- | ------------- | --------------------------- |
| id       | INT           | AUTO_INCREMENT, PRIMARY KEY |
| username | NVARCHAR(50)  | NOT NULL, UNIQUE            |
| password | NVARCHAR(100) | NOT NULL                    |

## Inventory Table

| Column   | Type            | Constraints                                          |
| -------- | --------------- | ---------------------------------------------------- |
| id       | INT             | AUTO_INCREMENT, PRIMARY KEY                          |
| user_id  | INT             | NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id) |
| code     | NVARCHAR(5)     | NOT NULL                                             |
| exchange | NVARCHAR(12)    | NOT NULL                                             |
| quantity | DECIMAL         | NOT NULL                                             |
| UNIQUE   | (user_id, code) |                                                      |

## Transactions Table

| Column    | Type         | Constraints                                          |
| --------- | ------------ | ---------------------------------------------------- |
| id        | INT          | AUTO_INCREMENT, PRIMARY KEY                          |
| user_id   | INT          | NOT NULL, FOREIGN KEY (user_id) REFERENCES users(id) |
| code      | NVARCHAR(5)  | NOT NULL                                             |
| exchange  | NVARCHAR(12) | NOT NULL                                             |
| quantity  | DECIMAL      | NOT NULL                                             |
| price     | DECIMAL      | NOT NULL                                             |
| timestamp | TIMESTAMP    |                                                      |
| buy       | BOOLEAN      | NOT NULL                                             |
