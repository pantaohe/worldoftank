package com.pt.gm.tank.jr;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.mouse.MouseUtils;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-08 09:13
 */
public class JiaRuZD {
    private static Logger logger = LoggerFactory.getLogger(JiaRuZD.class);


    public static boolean jiarujiemian(BufferedImage screenshot) throws IOException, InterruptedException {
        BufferedImage subimage = screenshot.getSubimage(StartMain.IN_COMBAT[0], StartMain.IN_COMBAT[1], StartMain.IN_COMBAT[2], StartMain.IN_COMBAT[3]);
        String fileContent = ImgUtils.getString(subimage);
        logger.debug("加入战斗位置文字为：{}", fileContent);

        if (StringUtils.isBlank(fileContent) && !fileContent.contains("加入战斗")) {
            logger.error("加入战斗位置无文字，或不是加入战斗，结束");
        }else {
            return JiaRuZD.jiaRu(screenshot);
        }
        return false;
    }

    public static boolean jiaRu(BufferedImage screenshot) throws IOException, InterruptedException {
        BufferedImage subimage = screenshot.getSubimage(733, 300, 450, 460);
        String fileContent = ImgUtils.getString(subimage);
        if (StringUtils.isNotBlank(fileContent) && fileContent.contains("处罚")) MouseUtils.mouseDianJi(1090 + (int)(Math.random() * 90), 733 + (int)(Math.random() * 18));
        logger.debug("当前进入加入战斗界面");
        int index = 0, x, y = StartMain.TANK_ADDR[1];
        while(true) {
            x = StartMain.TANK_ADDR[0] + StartMain.TANK_ADDR[2] * index++;
            subimage = screenshot.getSubimage(x, y, StartMain.TANK_ADDR[2], StartMain.TANK_ADDR[3]);
            fileContent = ImgUtils.getString(subimage);

            if (StringUtils.isBlank(fileContent) || fileContent.contains("购买")) {
                logger.debug("准备换行，没进第二行则结束了");
                if (y == StartMain.TANK_ADDR[1] + StartMain.TANK_ADDR[3]) break;
                logger.debug("进入第二行");
                index = 0;
                y = StartMain.TANK_ADDR[1] + StartMain.TANK_ADDR[3];
                continue;
            }
            if (fileContent.contains("战斗中") || fileContent.contains("车组乘员不足")) continue;

            MouseUtils.mouseDianJi(x + (int)(Math.random() * StartMain.TANK_ADDR[2]), y + (int)(Math.random() * StartMain.TANK_ADDR[3]));
            logger.debug("选中：" + fileContent.replaceAll("\\s", "") + "坦克");
            Thread.sleep(500);
            MouseUtils.mouseDianJi(StartMain.IN_COMBAT[0] + (int)(Math.random() * StartMain.IN_COMBAT[2]), StartMain.IN_COMBAT[1] + (int)(Math.random() * StartMain.IN_COMBAT[3]));
            logger.debug("点击开始");

            return true;
        }
        return false;
    }

}
