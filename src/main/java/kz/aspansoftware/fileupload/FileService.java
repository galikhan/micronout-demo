//package kz.aspansoftware.fileupload;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class FileService {
//
//    @Value("${file.upload.path}")
//    private String FILE_UPLOAD;
//
//    private FileRepository fileRepository;
//
//    public FileService(FileRepository fileRepository) {
//        this.fileRepository = fileRepository;
//    }
//
//    public void saveFiles(List<MultipartFile> files, String commonUUID) {
//
//        files.stream().forEach(file -> {
//
//            try {
//
//                byte[] bytes = file.getBytes();
//                String pathUrl = FILE_UPLOAD + commonUUID + "_" + file.getOriginalFilename();
//                String ext = FileUtils.getExtension(file.getOriginalFilename());
//                Path path = Paths.get(pathUrl);
//                File fileCreated = Files.write(path, bytes).toFile();
//                System.out.println(fileCreated.getAbsolutePath());
//
//                pathUrl = FILE_UPLOAD + commonUUID + "_" + file.getOriginalFilename();
//                boolean compressedImage = FileUtils.createImageWithCustomHeight(fileCreated, 500, ext, pathUrl);
//
//                FileDto dto = new FileDto();
//                dto.commonUUID = commonUUID;
//                dto.created = LocalDateTime.now();
//                dto.filename = file.getOriginalFilename();
//                dto.path = FILE_UPLOAD;
//
//                save(dto, commonUUID);
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//
//    }
//
//    public FileDto save(FileDto file, String commonUUID) {
//        FileEntity fileEntity = new FileEntity();
//        fileEntity.commonUUID = commonUUID;
//        fileEntity.containerType = ContainerTypEnum.TASK;
//        fileEntity.filename = file.filename;
//        fileEntity.path = file.path;
//        fileRepository.save(fileEntity);
//        return new FileDto(fileEntity);
//    }
//
//    public List<FileEntity> findByCommonUUID(String uuid) {
//        return fileRepository.findByCommonUUID(uuid);
//    }
//
//    public void updateContainerByCommonUUID(Long id, String uuid) {
//        List<FileEntity> fileEntities = findByCommonUUID(uuid);
//        fileEntities.stream().forEach(file -> {
//            file.container = id;
//            fileRepository.save(file);
//        });
//    }
//
//    public List<FileDto> findByContainer(Long container) {
//        return fileRepository.findByContainer(container).stream().map(FileDto::new).collect(Collectors.toList());
//    }
//
//    public List<FileDto> findByContainerAndIsRemoved(Long container) {
//        return fileRepository.findByContainerAndIsRemoved(container, false).stream().map(FileDto::new).collect(Collectors.toList());
//    }
//
//    public FileDto findById(Long id) {
//        Optional<FileEntity> file = fileRepository.findById(id);
//        if(file.isPresent()) {
//            return new FileDto(file.get());
//        }
//        return null;
//    }
//
//    public int remove(Long id) {
//        return fileRepository.remove(id);
//    }
//}
