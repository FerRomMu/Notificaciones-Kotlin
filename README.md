# Notificaciones-Kotlin

## Enunciado

Se pide programar un sistema para enviar alertas a usuarios que tenga la siguiente funcionalidad:
Se pueden registrar usuarios que recibirán alertas. 
Se pueden registrar temas sobre los cuales se enviarán alertas.
Los usuarios pueden optar sobre cuales temas quieren recibir alertas.
Se puede enviar una alerta sobre un tema y lo reciben todos los usuarios que han optado recibir alertas de ese tema.
Se puede enviar una alerta sobre un tema a un usuario específico, solo lo recibe ese único usuario.
Una alerta puede tener una fecha y hora de expiración. 
Hay dos tipos de alertas: Informativas y Urgentes.
Un usuario puede marcar una alerta como leída.
Se pueden obtener todas las alertas no expiradas de un usuario que aún no ha leído. 
Se pueden obtener todas las alertas no expiradas para un tema. Se informa para cada alerta si es para todos los usuarios o para uno específico.
Tanto para el punto 9 como el 10, el ordenamiento de las alertas es el siguiente: las Urgentes van al inicio, siendo la última en llegar la primera en obtenerse (LIFO). Y a continuación las informativas, siendo la primera en llegar la primera en obtenerse (FIFO).
