---
tags: [tfg, callahan, diseño]
---

# Callahan — Pantallas (seguimiento de diseño)

Relacionado: [[Callahan - TFG]]

> [!info] Cómo usar esta lista
> Marca cada pantalla cuando tengas su diseño terminado. Prioridad: 🔴 imprescindible · 🟡 recomendable · ⚪ opcional.

## 1. Acceso

- [x] 🔴 Página inicial (bienvenida)
- [x] 🔴 Iniciar sesión
- [ ] 🔴 Recuperar contraseña — pedir email
- [ ] 🔴 Recuperar contraseña — nueva contraseña

## 2. Registro (onboarding)

- [x] 🔴 Paso 1 — Credenciales
- [x] 🔴 Paso 2 — Selector de películas (galería + buscador)
- [x] 🔴 Paso 3 — Métricas de resultados (expediente abierto)
- [ ] 🔴 Paso 4 — Selector de plataformas (con "Lo haré más tarde")

> [!note] Cambio de flujo
> La consulta de estado de ánimo sale del registro: al terminar el paso 4 el usuario va a la página principal y desde ahí lanza su primera consulta.

## 3. Núcleo de la app

- [ ] 🔴 Página principal — primera visita (llamada clara a lanzar la primera consulta)
- [x] 🔴 Página principal — visitas habituales
- [x] 🔴 Consulta de estado de ánimo
- [x] 🔴 Resultados / listado de películas recomendadas
- [x] 🔴 Detalle de película
- [x] 🔴 Valoración por estrellas (modal u hoja inferior)
- [] 🔴 Watchlist (con "ya la vi" → valorar)
- [ ] 🟡 Historial de películas vistas y valoradas
- [ ] 🟡 Perfil / expediente vivo (afinidades actuales)
- [ ] ⚪ Búsqueda fuera del onboarding

## 4. Premium (Detective IA + Stripe)

- [ ] 🔴 Paywall (usuario gratuito entrando en la sección IA)
- [ ] 🔴 Detective IA — petición en lenguaje natural
- [ ] 🔴 Detective IA — resultados
- [ ] 🔴 Pago completado
- [ ] 🔴 Pago cancelado o fallido
- [ ] 🔴 Gestión de suscripción (ver plan, cancelar)

## 5. Panel de administrador

- [ ] 🔴 Login de administrador (o login común con rol)
- [ ] 🔴 Dashboard de métricas
- [ ] 🔴 Configuración del algoritmo (pesos, factor de ajuste, clamp, corte)
- [ ] 🟡 Moderación de caché de películas
- [ ] 🟡 Listado de usuarios
- [ ] 🟡 Detalle del expediente de un usuario

## Pendiente de decidir

- [ ] ¿Las plataformas **filtran** las recomendaciones o solo **informan** ("disponible en…") en el detalle?
