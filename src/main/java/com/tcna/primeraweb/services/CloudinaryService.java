package com.tcna.primeraweb.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CloudinaryService {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Value("${cloudinary.folder}")
    private String folder;

    private Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public UploadResult uploadFile(MultipartFile file) throws IOException {
        // Validación: límite de 10MB por archivo
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IOException("El archivo " + file.getOriginalFilename() + " excede el límite de 10MB");
        }

        // Genera un nombre único para el archivo (UUID + nombre original)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Parámetros de subida
        Map<String, Object> params = new HashMap<>();
        params.put("folder", folder);

        // El public_id se construye quitando la extensión del archivo
        String publicId = fileName;
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            publicId = fileName.substring(0, dotIndex);
        }
        params.put("public_id", publicId);
        params.put("resource_type", "auto");

        try {
            Map uploadResult = getCloudinary().uploader().upload(file.getBytes(), params);
            log.info("Archivo '{}' subido correctamente a Cloudinary", fileName);
            // El public_id canónico que devuelve Cloudinary incluye la carpeta: "<folder>/<publicId>"
            String secureUrl = (String) uploadResult.get("secure_url");
            String canonicalPublicId = (String) uploadResult.get("public_id");
            return new UploadResult(secureUrl, canonicalPublicId);
        } catch (Exception e) {
            log.error("Error al subir archivo a Cloudinary: {}", e.getMessage());
            throw new IOException("Error al subir el archivo: " + e.getMessage());
        }
    }

    public void deleteFile(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            // destroy() recibe el public_id exacto (con carpeta, sin extensión)
            Map result = getCloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Archivo '{}' eliminado correctamente de Cloudinary (result: {})", publicId, result.get("result"));
        } catch (Exception e) {
            log.error("Error al eliminar archivo de Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar el archivo de Cloudinary", e);
        }
    }
}
