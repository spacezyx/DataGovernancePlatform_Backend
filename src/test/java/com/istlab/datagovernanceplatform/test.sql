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
