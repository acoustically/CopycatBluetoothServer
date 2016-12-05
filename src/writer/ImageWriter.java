package writer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Created by acoustically on 16. 12. 5.
 */
public class ImageWriter extends DataWriter {
  private BufferedImage imageData;

  public ImageWriter(OutputStream outputStream, BufferedImage imageData) throws Exception{
    super(outputStream);
    this.imageData = imageData;
  }

  @Override
  public void write() {
    try {
      writer.write(2);
      writer.write(lengthToBytes(Files.readAllBytes(imageToFile(imageData).toPath()).length));
      writer.write(fileToBytes(imageToFile(imageData)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private File imageToFile(BufferedImage image) throws Exception{
    File imageFile = new File("TempImage.png");
    ImageIO.write(image, "PNG", imageFile);
    return imageFile;
  }
  private byte[] fileToBytes(File file) throws Exception{
    return Files.readAllBytes(file.toPath());
  }
}
