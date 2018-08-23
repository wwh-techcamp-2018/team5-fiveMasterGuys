package com.woowahan.techcamp.recipehub.image.controller;

import com.woowahan.techcamp.recipehub.image.exception.FileNotFoundException;
import com.woowahan.techcamp.recipehub.image.service.LocalFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Profile("local")
@RequestMapping("${local.storage.imageStoragePath}/{imgName}")
public class LocalImageDownloadController {

    @Autowired
    private LocalFileUploadService localFileUploadService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String imgName) throws FileNotFoundException {
        Resource file = localFileUploadService.loadAsResource(imgName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
