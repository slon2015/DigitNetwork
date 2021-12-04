# Зависимости

Для запуска необходимы установленный **Docker Desktop** версии 18.03 или выше и утилита **docker-compose**

# Локальный запуск

Для запуска приложения необходимо в корне репозитория выполнить команду

`docker compose up -d`

## Доступ к API

http://localhost:8080/swagger-ui/

## Запуск с keycloak хостом

Для запуска с выделенным keycloak хостом нужно изменить docker-compose.yaml файл добавив туда хост следующим образом

```
environment:
  KEYCLOAK_AUTHSERVERURL: http://yourhost:8081/auth
```
