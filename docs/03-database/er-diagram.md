# ER-диаграмма (логическая модель данных)

## Диаграмма

**PlantUML:** [`images/03/ER.puml`](../images/03/ER.puml)

## Описание таблиц

| Таблица | PK | Описание |
|---------|-----|---------|
| `app_user` | id | Учётные записи |
| `user_roles` | (user_id, roles) | ElementCollection ролей `RoleType` |
| `app_group` | id | Учебные группы |
| `training_subject` | id | Предметы группы |
| `educational_materials` | id | Материалы предмета |
| `material_complete` | (user_id, educational_material_id) | Статус выполнения |

## Индексы

| Таблица | Поле(я) | Назначение |
|---------|---------|------------|
| `app_user` | `username` | UNIQUE, логин |
| `app_user` | `email` | UNIQUE, OAuth и сброс пароля |
| `training_subject` | `group_id` | Фильтр по группе |
| `educational_materials` | `training_subject_id` | Материалы предмета |

## Нормализация

- **1NF:** атомарные поля; роли в `user_roles`
- **2NF:** нет частичных зависимостей от составного PK (кроме `material_complete`)
- **3NF:** FK ссылаются на PK родительских таблиц
