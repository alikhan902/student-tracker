# DDL-скрипты (логическая схема)

> В production схема создаётся Hibernate (`ddl-auto` из конфигурации). Ниже — эталонная логическая DDL для документации.

```sql
CREATE TABLE app_group (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL
);

CREATE TABLE app_user (
    id                          BIGSERIAL PRIMARY KEY,
    name                        VARCHAR(255) NOT NULL,
    username                    VARCHAR(255) NOT NULL UNIQUE,
    password                    VARCHAR(255) NOT NULL,
    email                       VARCHAR(255) UNIQUE,
    student_type                VARCHAR(32) NOT NULL DEFAULT 'STANDARD',
    provider_type               VARCHAR(32) NOT NULL,
    provider_id                 VARCHAR(255),
    reset_token                 VARCHAR(255),
    reset_token_expiry          TIMESTAMP,
    last_password_reset_request TIMESTAMP,
    group_id                    BIGINT REFERENCES app_group(id)
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES app_user(id),
    roles   VARCHAR(32) NOT NULL,
    PRIMARY KEY (user_id, roles)
);

CREATE TABLE training_subject (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    photo_path  VARCHAR(512),
    group_id    BIGINT NOT NULL REFERENCES app_group(id)
);

CREATE TABLE educational_materials (
    id                  BIGSERIAL PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    description         TEXT,
    file_path           VARCHAR(512),
    training_subject_id BIGINT NOT NULL REFERENCES training_subject(id)
);

CREATE TABLE material_complete (
    user_id                 BIGINT NOT NULL REFERENCES app_user(id),
    educational_material_id BIGINT NOT NULL REFERENCES educational_materials(id),
    status                  VARCHAR(32) NOT NULL,
    PRIMARY KEY (user_id, educational_material_id)
);

CREATE INDEX idx_training_subject_group ON training_subject(group_id);
CREATE INDEX idx_material_subject ON educational_materials(training_subject_id);
```

## Файловое хранилище (вне БД)

| Каталог | Содержимое |
|---------|------------|
| `uploads/training-subjects/` | Обложки предметов |
| `uploads/educational-materials/` | PDF, TXT и др. |

URL в БД: `/uploads/...` — раздаётся Spring + Nginx.
