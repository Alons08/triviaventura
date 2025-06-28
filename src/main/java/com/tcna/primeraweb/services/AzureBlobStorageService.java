package com.tcna.primeraweb.services;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class AzureBlobStorageService {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    private BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    public String uploadFile(MultipartFile file) {
        try {
            // Generar un nombre único para el archivo
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            // Obtener el cliente del contenedor
            BlobServiceClient blobServiceClient = getBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Crear el contenedor si no existe
            if (!containerClient.exists()) {
                containerClient.create();
                log.info("Contenedor '{}' creado", containerName);
            }
            
            // Obtener el cliente del blob
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            
            // Configurar headers HTTP para el tipo de contenido
            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());
            
            // Subir el archivo
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            blobClient.setHttpHeaders(headers);
            
            log.info("Archivo '{}' subido exitosamente", fileName);
            
            // Retornar la URL del archivo
            return blobClient.getBlobUrl();
            
        } catch (IOException e) {
            log.error("Error al subir archivo: {}", e.getMessage());
            throw new RuntimeException("Error al subir archivo a Azure Blob Storage", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            // Extraer el nombre del archivo de la URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            BlobServiceClient blobServiceClient = getBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            
            if (blobClient.exists()) {
                blobClient.delete();
                log.info("Archivo '{}' eliminado exitosamente", fileName);
            }
        } catch (Exception e) {
            log.error("Error al eliminar archivo: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar archivo de Azure Blob Storage", e);
        }
    }

    public boolean fileExists(String fileUrl) {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            BlobServiceClient blobServiceClient = getBlobServiceClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            
            return blobClient.exists();
        } catch (Exception e) {
            log.error("Error al verificar existencia del archivo: {}", e.getMessage());
            return false;
        }
    }
}
