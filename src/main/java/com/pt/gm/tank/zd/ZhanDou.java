package com.pt.gm.tank.zd;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-25 17:07
 */
public class ZhanDou {

    private static Logger logger = LoggerFactory.getLogger(ZhanDou.class);

    /**
     * 战斗界面
     * @param fileContent
     * @param screenshot
     */
    public static void zhandou(String fileContent, BufferedImage screenshot) {
        BufferedImage minMap;



        String[] filecs = fileContent.trim().split(":");
        try {
            int m = Integer.parseInt(filecs[0]);
            int s = Integer.parseInt(filecs[1]);

//            if (m == 0 && s < 31) return;     //倒计时，不需要操作

            if (ZhanDouFun.jihui(screenshot)) return;

            minMap = screenshot.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], StartMain.SCRN_SIZE[0]-StartMain.MAP_START[0], StartMain.SCRN_SIZE[1]-StartMain.MAP_START[1]);
            fileContent = ImgUtils.getString(minMap);


            int[] mouses = ZhanDouFun.mouseCenter(minMap);
            List<int[]> dfList = ZhanDouFun.dfTank(minMap);



            System.out.println(fileContent);
        } catch (Exception e) {
            logger.debug("不是在战斗中{}", e);
        }
    }


}
