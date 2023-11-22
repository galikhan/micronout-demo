//package kz.aspansoftware.fileupload;
//
//import kz.hbscale.main.utils.StringUtils;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class FileUploadHelper {
//
//    public String DIRECTORY_PATH;
//
//    public FileUploadHelper(String DIRECTORY_PATH) {
//        this.DIRECTORY_PATH = DIRECTORY_PATH;
//    }
//
//    public void plainSaveFile(File temporaryFile, String originalFilename) throws IOException {
//
////            String extension = parseExtension(file.getName());
////            String contentType = MimeTypes.getContentType(file.getName());
////            int thumbnailHeight = Integer.parseInt(Play.configuration.getProperty("play.fileupload.thumbnail.height"));
////            int optimalHeight = Integer.parseInt(Play.configuration.getProperty("play.fileupload.optimal.height"));
//
////        String randomUUIDName = UUID.randomUUID().toString();
//        String extension = parseExtension(originalFilename);
//        FileInputStream fis = new FileInputStream(temporaryFile);
//        BufferedInputStream bis = new BufferedInputStream(fis);
//
////        FileOutputStream fos = new FileOutputStream(DIRECTORY_PATH + "/" + originalFilename + "." + extension);
//        FileOutputStream fos = new FileOutputStream(DIRECTORY_PATH + "/" + originalFilename);
//        BufferedOutputStream bos = new BufferedOutputStream(fos);
//
//        byte[] bytes = new byte[1024];
//        while ((bis.read(bytes)) != -1) {
//            bos.write(bytes);
//        }
//        bis.close();
//        bos.close();
//        fos.close();
//    }
//
//    public String parseExtension(String filename) {
//        String extension = "";
//        if (StringUtils.isNotEmpty(filename)) {
//            extension = filename.substring(filename.lastIndexOf(".") + 1);
//        }
//        return extension;
//    }
//
//    //
////    public static void uploadImage(Long fieldId, String title, File file) throws IOException {
////
////        Field field = Field.findById(fieldId);
////        List list = Image.find("byField", field).fetch();
////
////        /*
////         *   limit uploaded images count for each field(MAX_IMAGES_PER_FIELD)
////         * */
////        if(file!=null && MimeTypes.getContentType(file.getName()).startsWith("image/")){
////
////            if(list.size()>=MAX_IMAGES_PER_FIELD){
////                throw new PlaySportException("number of images exceeds limit");
////            }
////
////            String filename = file.getName();
////            String contentType = MimeTypes.getContentType(file.getName());
////
////            String appPath = Play.applicationPath.getAbsolutePath();
////            String originalPath = Play.configuration.getProperty("play.fileupload.original");
////            int thumbnailHeight = Integer.parseInt(Play.configuration.getProperty("play.fileupload.thumbnail.height"));
////            int optimalHeight = Integer.parseInt(Play.configuration.getProperty("play.fileupload.optimal.height"));
////
////            String randomUUIDName = UUID.randomUUID().toString();
////
////            FileInputStream fis = new FileInputStream(file);
////            BufferedInputStream bis = new BufferedInputStream(fis);
////
////            FileOutputStream fos = new FileOutputStream(appPath+"/"+originalPath+"/"+randomUUIDName+"."+extension);
////            BufferedOutputStream bos =new BufferedOutputStream(fos);
////
////            byte [] bytes = new byte [1024];
////            while((bis.read(bytes))!=-1){
////                bos.write(bytes);
////            }
////            bis.close();
////            bos.close();
////            fos.close();
////
////            String url = originalPath+"/"+randomUUIDName+"."+extension;
////
////            Long imageId = saveImage(field, filename, contentType, randomUUIDName, url, Image.ImageType.ORIGINAL);
////
////            File newFile = new File(appPath+"/"+originalPath+"/"+randomUUIDName);
////
////            if(newFile.exists()){
////                //create thumbnail
////                String thumbnailPath = Play.configuration.getProperty("play.fileupload.thumbnail") + "/"+randomUUIDName+"."+extension;
////                createImage(newFile, thumbnailHeight, extension, thumbnailPath);
////                saveImage(field, filename, contentType, randomUUIDName, thumbnailPath, Image.ImageType.THUMBNAIL);
////                //create optimalopt
////                String optimalPath = Play.configuration.getProperty("play.fileupload.optimal")+"/"+randomUUIDName+"."+extension;
////                createImage(newFile, optimalHeight, extension, optimalPath);
////                saveImage(field, filename, contentType, randomUUIDName, optimalPath, Image.ImageType.OPTIMAL);
////            }
////
////            renderJSON("{\"resultId\": \""+imageId+"\"}");
////        }else{
////
////            renderJSON("{\"result\": \"it's too bad\" "+fieldId+" | }");
//////            renderJSON();
//////            throw new PlaySportException("{\"result\": \"it's too bad\" "+fieldId+" | }");
////        }
////    }
//
//    private static boolean createImage(File file, int height, String extension, String filename) throws IOException {
//
//        BufferedImage image = ImageIO.read(file);
//        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB :image.getType();
//
//        double ratio = (double) image.getWidth() / image.getHeight();
//        int width  = (int) Math.ceil( height * ratio );
//
//        BufferedImage thumbnailImage = new BufferedImage(width, height, type);
//        Graphics2D graphics = thumbnailImage.createGraphics();
//        graphics.drawImage(image, 0, 0,width, height, null);
//        graphics.dispose();
//
//        File thumbnailFile = new File(filename);
//        return ImageIO.write(thumbnailImage, extension, thumbnailFile);
//    }
//
////
////    private static Long saveImage(Field field,
////                                  String filename,
////                                  String contentType,
////                                  String randomUUIDName,
////                                  String url,
////                                  Image.ImageType imageType){
////        Image image = new Image();
////        image.field = field;
////        image.name = filename;
////        image.contentType = contentType;
////        image.resourceId = randomUUIDName;
////        image.url = url;
////        image.imageType = imageType;
////        image.save();
////        return image.getId();
////    }
//}