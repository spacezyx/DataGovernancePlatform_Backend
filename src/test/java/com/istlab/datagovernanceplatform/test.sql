-- 创建数据库
CREATE DATABASE your_database_name;
-- 切换到新创建的数据库
\c your_database_name;

-- 创建表：Department
CREATE TABLE Department (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

-- 添加表备注
COMMENT ON TABLE Department IS 'This table stores information about departments.';

-- 创建表：Employee
CREATE TABLE Employee (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          department_id INT NOT NULL
);

-- 添加表备注
COMMENT ON TABLE Employee IS 'This table stores information about employees.';

-- 创建表：Project
CREATE TABLE Project (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL
);

-- 添加表备注
COMMENT ON TABLE Project IS 'This table stores information about projects.';

-- 创建表：EmployeeProject (用于建立多对多关联)
CREATE TABLE EmployeeProject (
                                 employee_id INT NOT NULL,
                                 project_id INT NOT NULL,
                                 PRIMARY KEY (employee_id, project_id)
);

-- 添加表备注
COMMENT ON TABLE EmployeeProject IS 'This table stores the many-to-many relationship between employees and projects.';

-- 添加外键约束
ALTER TABLE Employee
    ADD CONSTRAINT fk_department_id
        FOREIGN KEY (department_id) REFERENCES Department(id);

ALTER TABLE EmployeeProject
    ADD CONSTRAINT fk_employee_id
        FOREIGN KEY (employee_id) REFERENCES Employee(id);

ALTER TABLE EmployeeProject
    ADD CONSTRAINT fk_project_id
        FOREIGN KEY (project_id) REFERENCES Project(id);

-- 插入 Department 表数据
INSERT INTO Department (name) VALUES
                                  ('Human Resources'),
                                  ('Finance'),
                                  ('Marketing'),
                                  ('Engineering'),
                                  ('Sales');

-- 插入 Employee 表数据
INSERT INTO Employee (name, department_id) VALUES
                                               ('John Doe', 1),
                                               ('Jane Smith', 2),
                                               ('Michael Johnson', 3),
                                               ('Emily Brown', 4),
                                               ('David Lee', 5),
                                               ('Sarah Clark', 1),
                                               ('Daniel White', 2),
                                               ('Olivia Wilson', 3),
                                               ('James Davis', 4),
                                               ('Linda Martinez', 5),
                                               ('William Anderson', 1),
                                               ('Emma Garcia', 2),
                                               ('Noah Lopez', 3),
                                               ('Ava Hall', 4),
                                               ('Sophia Young', 5),
                                               ('Logan King', 1),
                                               ('Mia Wright', 2),
                                               ('Ethan Harris', 3),
                                               ('Isabella Turner', 4),
                                               ('Lucas Scott', 5),
                                               ('Amelia Allen', 1),
                                               ('Liam Green', 2),
                                               ('Harper Baker', 3),
                                               ('Benjamin Adams', 4),
                                               ('Evelyn Mitchell', 5);

-- 插入 Project 表数据
INSERT INTO Project (name) VALUES
                               ('Project A'),
                               ('Project B'),
                               ('Project C'),
                               ('Project D'),
                               ('Project E'),
                               ('Project F'),
                               ('Project G'),
                               ('Project H'),
                               ('Project I'),
                               ('Project J'),
                               ('Project K'),
                               ('Project L'),
                               ('Project M'),
                               ('Project N'),
                               ('Project O'),
                               ('Project P'),
                               ('Project Q'),
                               ('Project R'),
                               ('Project S'),
                               ('Project T'),
                               ('Project U'),
                               ('Project V'),
                               ('Project W'),
                               ('Project X'),
                               ('Project Y'),
                               ('Project Z');

-- 插入 EmployeeProject 表数据
INSERT INTO EmployeeProject (employee_id, project_id) VALUES
                                                          (1, 1),
                                                          (2, 1),
                                                          (2, 2),
                                                          (3, 2),
                                                          (3, 3),
                                                          (4, 3),
                                                          (4, 4),
                                                          (5, 4),
                                                          (5, 5),
                                                          (6, 5),
                                                          (7, 1),
                                                          (8, 1),
                                                          (8, 2),
                                                          (9, 2),
                                                          (9, 3),
                                                          (10, 3),
                                                          (10, 4),
                                                          (11, 4),
                                                          (11, 5),
                                                          (12, 5),
                                                          (13, 1),
                                                          (14, 1),
                                                          (14, 2),
                                                          (15, 2),
                                                          (15, 3),
                                                          (16, 3),
                                                          (16, 4),
                                                          (17, 4),
                                                          (17, 5),
                                                          (18, 5),
                                                          (19, 1),
                                                          (20, 1),
                                                          (20, 2),
                                                          (21, 2),
                                                          (21, 3),
                                                          (22, 3),
                                                          (22, 4),
                                                          (23, 4),
                                                          (23, 5),
                                                          (24, 5),
                                                          (25, 1);
