package com.artxp.artxp.infrastructure.services;

import com.artxp.artxp.domain.entities.ImagenEntity;
import com.artxp.artxp.domain.repositories.ImagenRepository;
import com.artxp.artxp.domain.repositories.ObraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImagenService {
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private ObraRepository obraRepository;

    public List<ImagenEntity> list(){
        return imagenRepository.findByOrderById();
    }
    public Optional<ImagenEntity> getOne(Integer id){
        return imagenRepository.findById(id);
    }
    public void save(ImagenEntity imagen){
        imagenRepository.save(imagen);
    }
    public void delete(Integer id){
        imagenRepository.deleteById(id);
    }
    public boolean exists(Integer id){
        return imagenRepository.existsById(id);
    }
    public  Optional<ImagenEntity> getByCloudId(String imagenId){return imagenRepository.findFirstByImagenId(imagenId);}

}
