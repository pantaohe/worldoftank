package com.pt.gm.tank.util;

import com.jc.modules.recognition.FileReadUtils;
import com.jc.modules.recognition.mime.entry.FileContentEntry;
import com.pt.gm.tank.StartMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-25 17:08
 */
public class ImgUtils {


    public static String getString(BufferedImage subimage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(subimage, "jpg", baos);

        byte[] fileBytes = baos.toByteArray();

        FileContentEntry fce = new FileContentEntry(System.currentTimeMillis() + ".jpg", fileBytes);
        FileReadUtils.fileRead(fce);
        return fce.getFileContent();
    }

    /*截图*/
    public static BufferedImage screenshot() throws Exception {

        // 获取屏幕的大小
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        // 创建一个Rectangle对象，用于指定截屏的区域
        Rectangle rectangle = new Rectangle(dimension);
        // 捕获屏幕上的内容
        BufferedImage screenCapture = StartMain.robot.createScreenCapture(rectangle);
        screenCapture = ImageIO.read(new File("D:\\t\\ks.png"));
        return screenCapture;
    }

}
