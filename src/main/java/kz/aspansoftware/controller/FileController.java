package kz.aspansoftware.controller;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
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

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/upload")
@Introspected
public class FileController {

    @Value("${upload.imagePath}")
    String imagePath;
    @Value("${upload.documentPath}")
    String documentPath;

    @Value("${apiUrl}")
    String apiUrl;

    @Inject
    FileRepository fileRepository;
    private Logger log = LoggerFactory.getLogger(FileController.class);

//    @Get
//    @View("/upload.html")
//    public HttpResponse<?> index() {
//        return HttpResponse.ok(CollectionUtils.mapOf("apiUrl", apiUrl));
//    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/image")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Connectable
    @PermitAll
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
    @Transactional
    public SantecFile uploadDocument(CompletedFileUpload file, Long container) {
        return handleDocument(file, container);
    }

    @Get("/image/{containerId}")
    @Transactional
    public List<SantecFile> findByContainer(Long containerId) {
        return fileRepository.findByContainer(containerId);
    }

    @Get("/image/id/{id}")
    @Transactional
    public SantecFile findById(Long id) {
        return fileRepository.findById(id);
    }

    @Get("/file/container/{containerId}/container-class/{containerClass}")
    @Transactional
    public List<SantecFile> findByContainer(Long containerId, String containerClass) {
        return fileRepository.findByContainerAndClass(containerId, containerClass);
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

    @Post("/images")
    @Consumes(value = MediaType.MULTIPART_FORM_DATA)
    @Transactional
    public List<SantecFile> uploadImages(Publisher<CompletedFileUpload> files, Long container) throws ExecutionException, InterruptedException {

        List<SantecFile> images = new ArrayList<>();
        CompletableFuture<List<SantecFile>> completableFuture = new CompletableFuture<>();

        log.info("files come {}", files);
        Set<CompletedFileUpload> setFiles = new HashSet<>();
        Flowable
                .fromPublisher(files)
                .repeat(1)
                .map(i -> setFiles.add(i))
                .doOnTerminate(() -> {
                    setFiles.forEach(j -> {
                        images.add(handleImage(j, container));
                    });
//                    System.out.println(" subscribe :" + s);
                    completableFuture.complete(images);
                    log.info("completableFuture");


                }).subscribe().dispose();
//                .subscribe((s) -> {
//
//                    setFiles.forEach(j -> {
//                        images.add(handleImage(j, container));
//                    });
//                    System.out.println(" subscribe :" + s);
//                    completableFuture.complete(images);
//
//                }, throwable -> {
//                    log.error("upload error", throwable);
//                }).dispose();

//                .forEach(i -> {
////                    log.info("thread inner {}", Thread.currentThread().getName());
//                    if ((i.isComplete())) {
//                        images.add(handleImage(i, container));
//                        log.info("finished upload");
//                    } else {
//                        i.discard();
//                    }
//                });

        log.info("sending result");
        return completableFuture.get();
    }


    @Delete
    @Transactional
    public int delete(Long id) {
        return fileRepository.delete(id);
    }
}
