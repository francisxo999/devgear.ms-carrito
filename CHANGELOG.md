* v1.0.0: Inicialización del microservicio de carrito.
* v1.1.0: Creación de CarritoController, entidades, conexión a BD propia y seguridad JWT.
* v1.2.0: Creación de Dockerfile multi-etapa para despliegue en AWS.
* v1.2.1: Hotfix en EC2 - Inclusión del servicio app-carrito en docker-compose (puerto 8082), actualización de la conexión MySQL de localhost al contenedor db-carrito y corrección del emisor JWT a Azure AD v1.0.
* v1.2.2: Corrección definitiva del issuer JWT a Azure AD v2.0 (login.microsoftonline.com) y remoción del audience mal formado.
* v1.2.3: Forzar IPv4 en la JVM (java.net.preferIPv4Stack) para evitar fallo de validación del issuer por IPv6 no ruteable dentro del contenedor.