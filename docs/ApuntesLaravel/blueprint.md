# Laravel Blueprint: guía rápida

## ¿Qué es?
- Un generador de código para Laravel (`laravel-shift/blueprint`, ya está en `require-dev`).
- Escribes un fichero `draft.yaml` describiendo tus modelos y sus columnas, y Blueprint crea
  modelos, migraciones, factories y (si los pides) controladores y rutas.
- Es un atajo: lo que genera es el mismo código que escribirías a mano. No sustituye
  entender las migraciones; hay que leer y revisar lo que crea.

## ¿Cómo se usa?
1. Guarda `draft.yaml` en la raíz del proyecto (junto a `artisan`).
2. Define cada modelo en singular, con sus columnas. Sin `id`: Blueprint lo añade solo.
   - Tipo de columna: `nombre: string:50`, `score: float`, `sinopsis: text`.
   - Modificadores: `nullable`, `unique`, `default:0`.
   - Clave foránea: `user_id: id:user` (`id:` + nombre del modelo destino).
   - Muchos a muchos: `relationships: belongsToMany: Genre` (crea la tabla intermedia).
3. Genera el código: `php artisan blueprint:build`.
4. Revisa los ficheros creados en `app/Models/` y `database/migrations/`.
5. Crea las tablas: `php artisan migrate` (antes, `php artisan about` debe mostrar MySQL).
- No redefinas `User`: ya existe `users`. Las columnas extra van en una migración aparte.
- Define primero los modelos de los que dependen otros (las claves foráneas lo exigen).

## ¿Cómo solucionar errores?
- **`Method ...Blueprint::xxx does not exist`:** has usado el nombre de la columna como si
  fuera el tipo. El tipo va primero (`string`), el nombre es la clave del YAML.
- **El YAML no se lee:** revisa la sangría (espacios, nunca tabuladores) bajo cada modelo.
- **`id:xxx` falla o apunta a una tabla rara:** el nombre debe coincidir exactamente con
  el modelo destino. Cuidado con erratas y con el idioma del pluralizador (es inglés).
- **`table already exists`:** hay migraciones duplicadas (por ejemplo, al ejecutar
  `blueprint:build` dos veces) o intentas recrear `users`. Mira `database/migrations/`,
  borra la sobrante o usa `php artisan blueprint:erase` y vuelve a construir una sola vez.
- **Las tablas no aparecen en phpMyAdmin:** casi siempre el `.env` apunta a SQLite o la
  caché de configuración está vieja. Comprueba `php artisan about` y `php artisan config:clear`.
- **Ya has migrado y te equivocaste:** `php artisan migrate:rollback` deshace la última
  tanda; `php artisan migrate:fresh` reconstruye todo, pero borra todos los datos.
- **Qué no sabe hacer:** herencia y claves primarias que son a la vez foráneas
  (las subclases de `Preference`): esas migraciones se escriben a mano.