package businfo.lists;
import gui.ava.html.image.generator.HtmlImageGenerator;
import gui.ava.html.image.util.FormatNameUtil;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;


/**
 * Created by MIachal on 25.05.2017.
 */
public class  HtmlToImage extends HtmlImageGenerator {

        @Override
        public void saveAsImage(File file)
        {
            BufferedImage image = getBufferedImage();

            // Create a blank, RGB, same width and height, and a white background
            BufferedImage bufferedImageToWrite = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedImageToWrite.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);

            final String formatName = FormatNameUtil.formatForFilename(file.getName());

            try
            {
                // Write the image file in the appropriate format
                if (!ImageIO.write(bufferedImageToWrite, formatName, file))
                    throw new IOException("No formatter for specified file type ["
                            + formatName + "] available");
            }
            catch (IOException e)
            {
                throw new RuntimeException(String.format("Exception while saving '%s' image", file), e);
            }
        }

        public static void ImageGenerator(String html, String lineNumber)
        {
            HtmlToImage imageGenerator = new HtmlToImage();
            imageGenerator.loadHtml(html);
            imageGenerator.saveAsImage(lineNumber+".jpg");

        }
}
