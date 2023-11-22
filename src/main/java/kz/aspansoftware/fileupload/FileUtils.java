//package kz.aspansoftware.fileupload;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.geom.AffineTransform;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
//public class FileUtils {
//
//    private static Logger log = LogManager.getLogger(FileUtils.class);
//    public static String generateUUID() {
//        return UUID.randomUUID().toString();
//    }
//
//    public static String getExtension(String filename) {
//
//        int indexOfDot = filename.lastIndexOf(".");
//        String ext = filename.substring(indexOfDot + 1);
//        System.out.println("et" + ext);
//
//        return ext;
//    }
//
//
//    public static boolean createImageWithCustomHeight(File file, int height, String extension, String filename) throws IOException {
//
//        BufferedImage image = ImageIO.read(file);
//        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_RGB :image.getType();
//
//        double ratio = (double) image.getWidth() / image.getHeight();
//        int width  = (int) Math.ceil( height * ratio );
//
//        BufferedImage thumbnailImage = new BufferedImage(width, height, type);
//        Graphics2D graphics = thumbnailImage.createGraphics();
//
//        if(height < width) {
//            double rotationRequired = Math.toRadians (90);
//            AffineTransform a = AffineTransform.getRotateInstance(rotationRequired, (double) width/2, (double) height/2);
//            //Set our Graphics2D object to the transform
//            graphics.setTransform(a);
//        }
//        //Draw our image like normal
//        log.info("widh {}, hieght {}", width, height);
//
//        graphics.drawImage(image, 0, 0,width, height, null);
//        graphics.dispose();
//
//        File thumbnailFile = new File(filename);
//        return ImageIO.write(thumbnailImage, extension, thumbnailFile);
//    }
//}
