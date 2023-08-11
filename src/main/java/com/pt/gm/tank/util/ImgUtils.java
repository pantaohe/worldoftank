package com.pt.gm.tank.util;

import com.jc.modules.recognition.FileReadUtils;
import com.jc.modules.recognition.mime.entry.FileContentEntry;
import com.jeesite.modules.licence.util.ProjectPathUtils;
import com.pt.gm.tank.StartMain;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-25 17:08
 */
public class ImgUtils {
    public static String resourcePath;
    public static boolean notJarStart;
    static long timeT = System.currentTimeMillis();

    static {
        URL resource = ProjectPathUtils.class.getResource("/");
        if (resource == null) resource = ProjectPathUtils.class.getResource("");
        resourcePath = resource.getPath();
        notJarStart = resourcePath.contains("target/classes/");     //是否为测试环境
    }

    public static String getString(BufferedImage subimage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(subimage, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] fileBytes = baos.toByteArray();

        FileContentEntry fce = new FileContentEntry(System.currentTimeMillis() + ".jpg", fileBytes);
        FileReadUtils.fileRead(fce);
        String fileContent = fce.getFileContent();
        return fileContent == null ? "" : fileContent.replaceAll("\\s", "");
    }

    /*截图*/
    public static BufferedImage screenshot()  {

        // 获取屏幕的大小
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        // 创建一个Rectangle对象，用于指定截屏的区域
        Rectangle rectangle = new Rectangle(dimension);
        // 捕获屏幕上的内容
        BufferedImage screenCapture = StartMain.robot.createScreenCapture(rectangle);

        try {
            if (notJarStart) {
                screenCapture = ImageIO.read(new File("\\\\192.168.0.169\\tank\\photo\\1691742778796.png"));
            }else{
                long time = System.currentTimeMillis();
                if (time - timeT > 60000) { //一分钟
                    ImageIO.write(screenCapture, "png", new File("D:\\tank\\photo\\" + time + ".png"));
                    timeT = time;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenCapture;
    }

}
