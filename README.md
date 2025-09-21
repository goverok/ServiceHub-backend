# Booking Platform

Микросервисная система онлайн-записи (MVP).

## Сервисы
- `gateway` – входная точка (Spring Cloud Gateway)
- `user-service` – пользователи и авторизация
- `business-service` – бизнесы, филиалы
- `client-service` – клиенты и их записи
- `common` – общие DTO и утилиты

## Локальный запуск
```bash
docker-compose up --build