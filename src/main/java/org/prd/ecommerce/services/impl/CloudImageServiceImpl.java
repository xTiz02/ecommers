package org.prd.ecommerce.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.prd.ecommerce.entities.entity.ProductImage;
import org.prd.ecommerce.services.CloudImageService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CloudImageServiceImpl implements CloudImageService {

    @Autowired
    private Cloudinary cloudinary;

    private final Logger log = org.slf4j.LoggerFactory.getLogger(CloudImageServiceImpl.class);
    @Override
    public Map<String,Object> uploadImagesOnFolder(MultipartFile[] files, String folder, String principalName) throws Exception {
        Map params = ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image"
        );
        Map<String,Object> result = new HashMap<>();
        List<ProductImage> images = new ArrayList<>();
        List<String> publicIds = new ArrayList<>();
        Map cloudResult;
        ProductImage productImage = null;
        for (MultipartFile file : files) {
            productImage = new ProductImage();
            cloudResult = cloudinary.uploader().upload(file.getBytes(), params);
            publicIds.add(cloudResult.get("public_id").toString());
            if(!cloudResult.containsKey("url")){
                cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
                //throw new CloudUploadImgException("Error uploading file");
            }
            productImage.setPublicId(cloudResult.get("public_id").toString());
            productImage.setName(file.getOriginalFilename());
            productImage.setUrl(cloudResult.get("url").toString());
            productImage.setTag(cloudResult.get("tags").toString());
            productImage.setSize(file.getSize());
            productImage.setFormat(cloudResult.get("format").toString());
            if (principalName!=null){
                if (Objects.equals(file.getOriginalFilename(), principalName)) {
                    result.put("principalUrl", productImage.getUrl());
                }
            }
            images.add(productImage);


        }
        result.put("publicIds",publicIds);
        result.put("imagesProducts",images);
        return result;
    }

    @Override
    public Map uploadImageOnFolder(MultipartFile file, String folder) throws Exception {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image"));

        if(!uploadResult.containsKey("url")){
            //throw new CloudUploadImgException("Error uploading file");
        }
        return  Map.of( "publicId",uploadResult.get("public_id").toString(),
                "url",uploadResult.get("url").toString());
    }

    @Override
    public boolean dropImage(String publicId) throws Exception {
        Map result = cloudinary.api().deleteResources(Arrays.asList(publicId), ObjectUtils.emptyMap());
        if(result.containsKey("deleted")){
            return true;
        }
        return false;
    }

    @Override
    public boolean dropImagesOnFolder(List<String> publicIds) throws Exception {
        Map result = cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
        if(result.containsKey("deleted")){
            return true;
        }
        return false;
    }
}
