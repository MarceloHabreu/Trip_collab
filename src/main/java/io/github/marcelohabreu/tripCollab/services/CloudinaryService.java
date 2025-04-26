package io.github.marcelohabreu.tripCollab.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile image, String userId) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String publicId = "posts/" + userId + "/" + UUID.randomUUID();
        Map<String, Object> uploadResult = cloudinary.uploader().upload
                (image.getBytes(),
                        ObjectUtils.asMap(
                                "public_id", publicId,
                                "resource_type", "image",
                                "folder", "tripcollab/posts"
                        ));
        return uploadResult.get("secure_url").toString();
    }
    // Recebe um MultipartFile e o userId para organizar imagens por usuário.
    //Gera um public_id único (ex.: posts/user123/uuid).
    //Usa secure_url para HTTPS, conforme recomendado.
    //Retorna null se não houver imagem (você pode ajustar para uma URL padrão).

    public void deleteImage(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromUrl(imageUrl);
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    private String extractPublicIdFromUrl(String imageUrl){
        String[] parts = imageUrl.split("/tripcollab/posts/");
        if (parts.length > 1) {
            String filePart = parts[1];
            return "tripcollab/posts/" + filePart.substring(0, filePart.lastIndexOf('.'));
        }
        throw new IllegalArgumentException("Invalid Cloudinary URL");
    }
}
