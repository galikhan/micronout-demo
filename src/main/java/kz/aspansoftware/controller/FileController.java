package kz.aspansoftware.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller("/file")
public class FileController {

    private Logger log = LoggerFactory.getLogger(FileController.class);

    @Post("/upload")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    public void fileUpload(CompletedFileUpload file) throws IOException {
        log.info("filename {}", file.getFilename());
        log.info("getName {}", file.getName());
        log.info("getContentType {}", file.getContentType());
        Path filePath = Files.createTempFile(Path.of("."), "uploaded-file-", file.getFilename());
        File fileC = filePath.toFile();

        log.info("filePath {}",         fileC.getAbsolutePath());
    }
}
