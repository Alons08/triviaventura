# Configuración de Azure Blob Storage

Este proyecto incluye integración con Azure Blob Storage para almacenar imágenes de categorías.

## Configuración requerida

### 1. Crear una cuenta de Azure Storage

1. Ve al portal de Azure (https://portal.azure.com)
2. Crea un nuevo "Storage Account"
3. Una vez creado, ve a "Access keys" en el menú de la izquierda
4. Copia el "Connection string" de la key1 o key2

### 2. Configurar el archivo application.properties

Reemplaza las siguientes líneas en `src/main/resources/application.properties`:

```properties
# CONFIGURACIÓN DE AZURE BLOB STORAGE
azure.storage.connection-string=TU_CONNECTION_STRING_AQUÍ
azure.storage.container-name=categoria-imagenes
```

### 3. Ejemplo de Connection String

Tu connection string debería verse así:
```
DefaultEndpointsProtocol=https;AccountName=tuStorageAccount;AccountKey=tuAccessKey==;EndpointSuffix=core.windows.net
```

### 4. Contenedor

El contenedor "categoria-imagenes" se creará automáticamente cuando subas la primera imagen.

## Funcionalidades implementadas

- ✅ Subida de imágenes al crear/editar categorías
- ✅ Validación de tipos de archivo (solo imágenes)
- ✅ Límite de tamaño de archivo (5MB)
- ✅ Eliminación automática de imágenes al eliminar categorías
- ✅ Vista previa de imágenes en el formulario
- ✅ Mostrar imágenes en el listado de categorías
- ✅ Mostrar imágenes en la selección de categorías para jugar

## Tipos de archivo soportados

- JPG/JPEG
- PNG
- GIF
- WebP

## Limitaciones

- Tamaño máximo: 5MB por imagen
- Solo se permite un archivo por categoría
- El contenedor debe ser público para acceso de lectura (configuración automática)

## Notas de seguridad

- Mantén tu connection string seguro y no lo subas al control de versiones
- Considera usar Azure Key Vault para production
- Las imágenes se almacenan con nombres únicos (UUID) para evitar conflictos
