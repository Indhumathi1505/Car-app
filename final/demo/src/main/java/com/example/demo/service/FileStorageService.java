/*
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



@Service
public class FileStorageService {

    private final GridFsTemplate gridFsTemplate;

    @Autowired
    public FileStorageService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

   
    public String saveFile(MultipartFile file) throws IOException {
        // Upload file to GridFS and return generated ObjectId as String
        return gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        ).toString();
    }
}
    */
    package com.example.demo.service;

import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.IOException;

@Service
public class FileStorageService {

    private final GridFsTemplate gridFsTemplate;

    public FileStorageService(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    public String saveFile(MultipartFile file) throws IOException {
        return gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        ).toString();
    }

    public byte[] getFile(String id) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(
                new org.springframework.data.mongodb.core.query.Query(
                        org.springframework.data.mongodb.core.query.Criteria.where("_id").is(id)
                )
        );
        return gridFsTemplate.getResource(file).getInputStream().readAllBytes();
    }
}

