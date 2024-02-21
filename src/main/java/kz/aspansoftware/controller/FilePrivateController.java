package kz.aspansoftware.controller;

import io.micronaut.context.annotation.Value;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.transaction.annotation.Transactional;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import kz.aspansoftware.records.SantecFile;
import kz.aspansoftware.repository.FileRepository;
import net.coobird.thumbnailator.Thumbnailator;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static kz.aspansoftware.enums.ContainerClass.*;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/private/upload")
public class FilePrivateController {

    @Value("${upload.imagePath}")
    String imagePath;
    @Value("${upload.documentPath}")
    String documentPath;

    @Value("${apiUrl}")
    String apiUrl;

    @Inject
    FileRepository fileRepository;
    private Logger log = LoggerFactory.getLogger(FilePrivateController.class);

    @Post("/image")
    @PermitAll
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public SantecFile uploadImage(CompletedFileUpload file, Long container) {
        return handleImage(file, container);
    }

    @Connectable
    public SantecFile handleImage(CompletedFileUpload file, Long container) {
        try {

            File tempFile = File.createTempFile("image-", file.getFilename());
            Path path = Paths.get(tempFile.getAbsolutePath());
            Files.write(path, file.getBytes());

            log.info("abs path {}", tempFile.getAbsolutePath());

            var largeImage = new File(imagePath + "/" + tempFile.getName());
            var smallImage = new File(imagePath + "/thumbnail-" + tempFile.getName());

            Thumbnailator.createThumbnail(tempFile, largeImage, 1000, 1000);
            Thumbnailator.createThumbnail(tempFile, smallImage, 150, 150);

            fileRepository.create(container, IMAGE, largeImage.getName(), largeImage.getPath());
            var thumb = fileRepository.create(container, THUMBNAIL, smallImage.getName(), smallImage.getPath());
            log.info("updload image {}", largeImage.getName());
            return thumb;

        } catch (IOException e) {
            file.discard();
            throw new RuntimeException(e);
        }
    }

    @Post("/document")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @PermitAll
    @Transactional
    public SantecFile uploadDocument(CompletedFileUpload file, Long container) {
        return handleDocument(file, container);
    }

    @Put("/document/description")
    @Transactional
    public int updateDocumentDescription(Long id, String description) {
        return fileRepository.updateDocumentDescription(id, description);
    }

    private SantecFile handleDocument(CompletedFileUpload file, Long container) {
        try {
            var newFile = new File((documentPath + "/" + file.getFilename()));
            if (newFile.exists()) {
                newFile.delete();
            }
            Path path = Files.createFile(Path.of(documentPath + "/" + file.getFilename()));
            Files.write(path, file.getBytes());
            log.info("updload document {}", file.getFilename());
            return fileRepository.create(container, DOCUMENT, file.getFilename(), path.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Delete("/file/{id}")
    @Transactional
    public int delete(@PathVariable Long id) {
        return fileRepository.delete(id);
    }
}
