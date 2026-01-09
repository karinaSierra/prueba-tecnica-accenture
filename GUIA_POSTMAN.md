# 🚀 Guía para Probar la API en Postman

## 📋 Pasos para Importar la Colección

1. **Abrir Postman**
2. **Importar la colección:**
   - Click en `Import` (botón superior izquierdo)
   - Selecciona el archivo `Franquicias_API.postman_collection.json`
   - O arrastra y suelta el archivo en Postman

## 🔧 Configuración Inicial

### Variables de Entorno (Opcional pero Recomendado)

1. En Postman, crea un **Environment** llamado "Local"
2. Agrega las siguientes variables:
   - `base_url`: `http://localhost:8080`
   - `franquiciaId`: `1` (se actualizará después de crear una franquicia)
   - `sucursalId`: `1` (se actualizará después de crear una sucursal)
   - `productoId`: `1` (se actualizará después de crear un producto)

### Usar Variables en las Requests

En lugar de `http://localhost:8080`, usa `{{base_url}}` en las URLs.

## 📝 Flujo de Pruebas Recomendado

### Paso 1: Crear una Franquicia
```
POST {{base_url}}/api/franquicias
Body:
{
  "nombre": "McDonald's"
}
```
**Respuesta esperada:** `201 Created` con el ID de la franquicia creada.
**Acción:** Copia el `id` de la respuesta y actualiza la variable `franquiciaId` en Postman.

### Paso 2: Agregar una Sucursal
```
POST {{base_url}}/api/franquicias/{{franquiciaId}}/sucursales
Body:
{
  "nombre": "Sucursal Centro"
}
```
**Respuesta esperada:** `201 Created` con el ID de la sucursal creada.
**Acción:** Copia el `id` de la respuesta y actualiza la variable `sucursalId`.

### Paso 3: Agregar Productos a la Sucursal
```
POST {{base_url}}/api/sucursales/{{sucursalId}}/productos
Body:
{
  "nombre": "Big Mac",
  "stock": 50
}
```
**Respuesta esperada:** `201 Created` con el producto creado.
**Acción:** Copia el `id` del producto y actualiza la variable `productoId`.

### Paso 4: Agregar más Productos
Repite el Paso 3 con diferentes productos para tener datos de prueba:
- "McNuggets", stock: 30
- "Papas Fritas", stock: 100
- "Coca Cola", stock: 200

### Paso 5: Modificar Stock de un Producto
```
PUT {{base_url}}/api/sucursales/{{sucursalId}}/productos/{{productoId}}/stock
Body:
{
  "stock": 75
}
```
**Respuesta esperada:** `200 OK` con el producto actualizado.

### Paso 6: Obtener Productos con Mayor Stock por Franquicia
```
GET {{base_url}}/api/franquicias/{{franquiciaId}}/productos-maximo-stock
```
**Respuesta esperada:** `200 OK` con un array de productos, cada uno con el mayor stock de su sucursal.

### Paso 7: Actualizar Nombres (Endpoints PLUS)
```
PUT {{base_url}}/api/franquicias/{{franquiciaId}}/nombre
Body:
{
  "nombre": "McDonald's Actualizado"
}

PUT {{base_url}}/api/franquicias/{{franquiciaId}}/sucursales/{{sucursalId}}/nombre
Body:
{
  "nombre": "Sucursal Centro Actualizada"
}

PUT {{base_url}}/api/sucursales/{{sucursalId}}/productos/{{productoId}}/nombre
Body:
{
  "nombre": "Big Mac Actualizado"
}
```

### Paso 8: Eliminar un Producto
```
DELETE {{base_url}}/api/sucursales/{{sucursalId}}/productos/{{productoId}}
```
**Respuesta esperada:** `204 No Content`

## 🔍 Códigos de Estado HTTP Esperados

- **201 Created**: Recursos creados exitosamente (POST)
- **200 OK**: Operaciones exitosas (GET, PUT)
- **204 No Content**: Eliminación exitosa (DELETE)
- **400 Bad Request**: Datos inválidos o faltantes
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error del servidor

## ⚠️ Errores Comunes

### Error: "Connection refused"
- **Causa:** La aplicación no está corriendo
- **Solución:** Verifica que Docker esté ejecutándose y los contenedores estén activos:
  ```powershell
  docker ps
  ```

### Error: "404 Not Found"
- **Causa:** ID incorrecto o recurso no existe
- **Solución:** Verifica que los IDs en las variables sean correctos

### Error: "400 Bad Request"
- **Causa:** Datos inválidos en el body
- **Solución:** Verifica que el JSON esté bien formado y contenga los campos requeridos

## 📊 Ejemplo de Respuestas

### Crear Franquicia (201 Created)
```json
{
  "id": 1,
  "nombre": "McDonald's",
  "createdAt": "2026-01-09T15:00:00Z",
  "updatedAt": "2026-01-09T15:00:00Z"
}
```

### Obtener Productos Máximo Stock (200 OK)
```json
[
  {
    "productoId": 1,
    "productoNombre": "Big Mac",
    "stock": 50,
    "sucursalId": 1,
    "sucursalNombre": "Sucursal Centro"
  },
  {
    "productoId": 2,
    "productoNombre": "McNuggets",
    "stock": 30,
    "sucursalId": 1,
    "sucursalNombre": "Sucursal Centro"
  }
]
```

## 🎯 Tips Adicionales

1. **Usa Tests en Postman:** Puedes agregar scripts de prueba para validar automáticamente las respuestas
2. **Guarda las Respuestas:** Usa el historial de Postman para revisar las respuestas anteriores
3. **Variables Dinámicas:** Puedes usar scripts de Postman para extraer IDs automáticamente de las respuestas

## 🔗 URL Base

```
http://localhost:8080
```

¡Listo para probar! 🚀

