package com.artxp.artxp.infrastructure.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CloudinaryService {

    Cloudinary cloudinary;

    private Map<String, String> valuesMap = new HashMap<>();

    public CloudinaryService() {
/*
        Dotenv dotenv = Dotenv.load();
        //Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        this.cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        System.out.println(cloudinary.config.cloudName);
*/
        valuesMap.put("cloud_name", "dr1jbzn9r");
        valuesMap.put("api_key", "543737364491154");
        valuesMap.put("api_secret", "xBsiJ-roPIVBtMr07DIGO4EfHXU");
        cloudinary = new Cloudinary(valuesMap);

    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        String uniqueId = "artxp_" + UUID.randomUUID().toString();
        Map params1 = ObjectUtils.asMap(
                "display_name", file.getName(),
                "public_id", uniqueId,
                "unique_filename", false,
                "overwrite", false
        );

        Map result = cloudinary.uploader().upload(file, params1);
        file.delete();
        return result;
    }

    public Optional<Map> delete(String id) throws IOException {
        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return Optional.of(result);
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}