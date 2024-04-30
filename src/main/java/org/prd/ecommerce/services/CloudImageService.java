package org.prd.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CloudImageService {

    public Map<String,Object> uploadImagesOnFolder(MultipartFile[] files, String folder,String principalUrl) throws Exception;
    public Map uploadImageOnFolder(MultipartFile file, String folder) throws Exception;
    public boolean dropImage(String publicId) throws Exception;
    public boolean dropImagesOnFolder(List<String> publicIds) throws Exception;
}
