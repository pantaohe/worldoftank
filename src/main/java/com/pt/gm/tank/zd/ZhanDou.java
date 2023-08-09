package com.pt.gm.tank.zd;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-25 17:07
 */
public class ZhanDou {

    private static Logger logger = LoggerFactory.getLogger(ZhanDou.class);
    static int MIN_MAP_W = StartMain.SCRN_SIZE[0] - StartMain.MAP_START[0];

    public static boolean zhandouFX(BufferedImage screenshot) throws IOException {
        //右上角时间
        BufferedImage subimage = screenshot.getSubimage(1860, StartMain.SCRN_SIZE[2], 50, 30);
        String fileContent = ImgUtils.getString(subimage);

        logger.debug("界面内容{}", fileContent.replaceAll("\\s", ""));
        //有时间表示战斗中
        if (StringUtils.isNotBlank(fileContent) && fileContent.contains(":")){
            logger.debug("进入交战中界面");
            ZhanDou.zhandou(fileContent, screenshot);
            logger.debug("交战界面分析完成");
            return true;
        }
        return false;
    }


    /**
     * 战斗界面
     * @param fileContent
     * @param screenshot
     */
    public static void zhandou(String fileContent, BufferedImage screenshot) {
        BufferedImage minMap = screenshot.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], MIN_MAP_W, StartMain.SCRN_SIZE[1] - StartMain.MAP_START[1] + StartMain.SCRN_SIZE[2]);


        String[] filecs = fileContent.trim().split(":");
        try {
            int m = Integer.parseInt(filecs[0]);
            int s = Integer.parseInt(filecs[1]);

            // 击毁
            if (ZhanDouFun.jihui(screenshot)) return;

            if ((m == 0 && s < 59) || (StartMain.TUPIAN_NEIRONG.contains("随机战"))){
                logger.debug("预读阶段，无需处理");
                return;     //倒计时，不需要操作
            }

            kaiche(minMap);

            // 火炮小地图框
//            int[] mouses = ZhanDouFun.mouseCenter(minMap);

            //所有坦克坐标
//            List<int[]> dfList = ZhanDouFun.dfTank(minMap);



        } catch (Exception e) {
            logger.debug("不是在战斗中{}", e);
        }
    }

    private static void kaiche(BufferedImage minMap) throws InterruptedException {
        if (StartMain.LU_XIAN == null) {    //如果没有读取到地图，则直接两次r
            logger.debug("没有读取到地图，则直接两次r");
            StartMain.robot.keyPress(KeyEvent.VK_R);
            StartMain.robot.keyRelease(KeyEvent.VK_R);
            Thread.sleep(150);
            StartMain.robot.keyPress(KeyEvent.VK_R);
            StartMain.robot.keyRelease(KeyEvent.VK_R);
            return;
        }
        int[] addrs = ZhanDouFun.myAddr(minMap);
        if (addrs == null) FangXiangKongZhi.xuyaoW = true;
        double lx0 = ZhanDouFun.dd2(StartMain.LU_XIAN.get(0), addrs);
        double lx1 = ZhanDouFun.dd2(StartMain.LU_XIAN.get(1), addrs);

        if (lx0 < lx1) {
            for (int i = 2; i < StartMain.LU_XIAN.size(); i++) {
                FangXiangKongZhi.kongzhi(addrs, i);
            }
        }else {
            for (int i = StartMain.LU_XIAN.size() - 1; i >= 2; i--) {
                FangXiangKongZhi.kongzhi(addrs, i);
            }
        }
    }



    public static void main(String[] args) {
        System.out.println(-2 % 10);
    }
}
