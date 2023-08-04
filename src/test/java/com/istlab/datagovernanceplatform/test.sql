-- 创建数据库
CREATE DATABASE test_database0804;
-- 切换到新创建的数据库
\c test_database0804;

-- 创建表：Department
CREATE TABLE Department (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

-- 创建表：Employee
CREATE TABLE Employee (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          department_id INT NOT NULL,
                          FOREIGN KEY (department_id) REFERENCES Department(id)
);

-- 创建表：Project
CREATE TABLE Project (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL
);

-- 创建表：EmployeeProject (用于建立多对多关联)
CREATE TABLE EmployeeProject (
                                 employee_id INT NOT NULL,
                                 project_id INT NOT NULL,
                                 PRIMARY KEY (employee_id, project_id),
                                 FOREIGN KEY (employee_id) REFERENCES Employee(id),
                                 FOREIGN KEY (project_id) REFERENCES Project(id)
);
