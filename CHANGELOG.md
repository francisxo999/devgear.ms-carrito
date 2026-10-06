* v1.0.0: Inicialización del microservicio de carrito.
* v1.1.0: Creación de CarritoController, entidades, conexión a BD propia y seguridad JWT.
* v1.2.0: Creación de Dockerfile multi-etapa para despliegue en AWS.
* v1.2.1: Hotfix en EC2 - Inclusión del servicio app-carrito en docker-compose (puerto 8082), actualización de la conexión MySQL de localhost al contenedor db-carrito y corrección del emisor JWT a Azure AD v1.0.
* v1.2.2: Corrección definitiva del issuer JWT a Azure AD v2.0 (login.microsoftonline.com) y remoción del audience mal formado.
* v1.2.3: Forzar IPv4 en la JVM (java.net.preferIPv4Stack) para evitar fallo de validación del issuer por IPv6 no ruteable dentro del contenedor.
* v1.2.4: Fix de encoding UTF-8 en la conexión JDBC, por consistencia con ms-productos.
* v1.3.0: Arquitectura de DTOs y capa de servicio; valida existencia y stock del producto contra ms-productos antes de agregarlo al carrito (reenviando el JWT); nuevos endpoints PUT/DELETE para actualizar cantidad y eliminar items; fix de datasource que apuntaba por error al host de ms-productos.
* v1.3.1: Apunta a ms-productos por su IP pública de producción en vez del hostname interno de Docker.
* v1.4.0: Agrega endpoint para vaciar el carrito completo (lo usa ms-ordenes tras confirmar una compra).
* v1.4.1: Corrige TransactionRequiredException al vaciar el carrito — deleteByUsuarioId (método derivado) necesita @Transactional explícito, a diferencia de delete() heredado de JpaRepository que ya lo trae incorporado.