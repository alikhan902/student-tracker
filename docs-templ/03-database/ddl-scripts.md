# DDL-скрипты создания таблиц

> Схема генерируется автоматически через Hibernate (`spring.jpa.hibernate.ddl-auto`).  
> Ниже приведены эквивалентные DDL-скрипты для документирования и ручной инициализации.

```sql
-- ============================================================
-- kanbaNice — DDL PostgreSQL 16
-- ============================================================

-- Компании
CREATE TABLE company (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Пользователи
CREATE TABLE app_user (
    id                            BIGSERIAL PRIMARY KEY,
    name                          VARCHAR(255) NOT NULL,
    username                      VARCHAR(255) NOT NULL UNIQUE,
    email                         VARCHAR(255) UNIQUE,
    password                      VARCHAR(255) NOT NULL,
    user_type                     VARCHAR(20),
    provider_type                 VARCHAR(20)  NOT NULL,
    provider_id                   VARCHAR(255),
    reset_token                   VARCHAR(255),
    reset_token_expiry            TIMESTAMP,
    last_password_reset_request   TIMESTAMP,
    pending_email                 VARCHAR(255),
    email_verification_token      VARCHAR(255),
    email_verification_expiry     TIMESTAMP,
    company_id                    BIGINT REFERENCES company(id) ON DELETE SET NULL
);

-- Роли пользователей (ElementCollection)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    roles   VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, roles)
);

-- Проекты
CREATE TABLE kanban_project (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    company_id BIGINT       NOT NULL REFERENCES company(id) ON DELETE CASCADE,
    created_at TIMESTAMP
);

-- Участники проектов
CREATE TABLE project_member (
    id         BIGSERIAL PRIMARY KEY,
    project_id BIGINT      NOT NULL REFERENCES kanban_project(id) ON DELETE CASCADE,
    user_id    BIGINT      NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    role       VARCHAR(20) NOT NULL,
    CONSTRAINT uq_project_member UNIQUE (project_id, user_id)
);

-- Канбан-доски
CREATE TABLE kanban_board (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    project_id BIGINT       NOT NULL REFERENCES kanban_project(id) ON DELETE CASCADE,
    created_at TIMESTAMP
);

-- Задачи
CREATE TABLE kanban_task (
    id                BIGSERIAL PRIMARY KEY,
    title             VARCHAR(255) NOT NULL,
    description       TEXT,
    status            VARCHAR(20)  NOT NULL DEFAULT 'TODO',
    status_changed_by VARCHAR(255),
    board_id          BIGINT       NOT NULL REFERENCES kanban_board(id) ON DELETE CASCADE,
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP
);

-- ============================================================
-- Индексы
-- ============================================================
CREATE INDEX idx_project_company   ON kanban_project(company_id);
CREATE INDEX idx_member_project    ON project_member(project_id);
CREATE INDEX idx_board_project     ON kanban_board(project_id);
CREATE INDEX idx_task_board        ON kanban_task(board_id);

-- ============================================================
-- Тестовые данные
-- ============================================================
INSERT INTO company (name) VALUES ('Demo Company');

INSERT INTO app_user (name, username, email, password, provider_type)
VALUES ('Admin User', 'admin', 'admin@demo.com',
        '$2a$12$placeholderHashForDocumentation', 'LOCAL');

INSERT INTO user_roles (user_id, roles) VALUES (1, 'ADMIN');
```
