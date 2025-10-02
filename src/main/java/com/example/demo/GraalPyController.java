package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class GraalPyController {
    private final GraalPyService graalPyService;

    GraalPyController(GraalPyService graalPyService) {
        this.graalPyService = graalPyService;
    }

    @GetMapping(value = "/hello", produces = "text/plain")
    public String hello() {
        return graalPyService.hello("Spring Boot");
    }

    @PostMapping(value = "/convert", produces = "text/plain")
    public String convert(@RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload-", ".tmp");
        file.transferTo(tempFile);
        return graalPyService.convert(file.getOriginalFilename(), tempFile.getAbsolutePath());
    }

    @PostMapping(value = "/summarize", produces = "text/plain")
    public String summarize(@RequestParam("file") MultipartFile file) throws IOException {
        return graalPyService.summarize(convert(file));
    }
}
