-- Создаем таблицу бизнесов
CREATE TABLE IF NOT EXISTS businesses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP DEFAULT now() NOT NULL
);

-- Добавляем уникальное ограничение на имя бизнеса для одного владельца
ALTER TABLE businesses
ADD CONSTRAINT uq_business_name_owner UNIQUE (name, owner_id);

-- Добавляем внешний ключ на таблицу users
ALTER TABLE businesses
ADD CONSTRAINT fk_business_owner
FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE;