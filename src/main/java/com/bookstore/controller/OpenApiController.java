package com.bookstore.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@Hidden
public class OpenApiController {
    @RequestMapping(path = "/openapi.yaml", method = RequestMethod.GET)
    @ResponseBody
    public String getApiDocs() throws IOException {
        File resource = new ClassPathResource("openapi.yaml").getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }
}
