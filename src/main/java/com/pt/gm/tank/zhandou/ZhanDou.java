package com.pt.gm.tank.zhandou;

import com.pt.gm.tank.config.CF;
import com.pt.gm.tank.kongzhi.FangXiangKongZhi;
import com.pt.gm.tank.util.ImgUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-07-25 17:07
 */
public class ZhanDou {

    private static Logger logger = LoggerFactory.getLogger(ZhanDou.class);
    public static int MIN_MAP_W = CF.SCRN_SIZE[0] - CF.MAP_START[0];

    public static int zhandouFX(BufferedImage screenshot) {
        //右上角时间
        BufferedImage subimage = screenshot.getSubimage(1860, CF.SCRN_SIZE[2], 50, 30);
        String fileContent = ImgUtils.getString(subimage);

        String tishi = ""; int flag = 0;
        //有时间表示战斗中
        if (StringUtils.isNotBlank(fileContent) && fileContent.contains(":")){

            String[] filecs = fileContent.trim().split(":");
            try {
                int m = Integer.parseInt(filecs[0]);
                int s = Integer.parseInt(filecs[1]);
                if ((m == 0 && s < 59) || (CF.TUPIAN_NEIRONG.contains("随机战"))) {
                    tishi = "预读无需处理";
                    flag =  1;     //倒计时，不需要操作
                }else {
                    tishi = "交战界面";
                    flag = 2;
                }
            } catch (Exception e) {
                tishi = "时间格式错误";
                logger.error(tishi, e);
            }
        }
        logger.debug("右上角时间处:{}，结果:{}", fileContent, tishi);
        return flag;
    }


    /**
     * 战斗界面
     * @param screenshot
     */
    public static void zhandou(BufferedImage screenshot) {
        BufferedImage minMap = screenshot.getSubimage(CF.MAP_START[0], CF.MAP_START[1], MIN_MAP_W, MIN_MAP_W);


        try {
            // 击毁
            if (ZhanDouFun.jihui(screenshot)) return;

            if (CF.OPEN_GUA_JI) kaiche(minMap);

            // 火炮小地图框
//            int[] mouses = ZhanDouFun.mouseCenter(minMap);

            //所有坦克坐标
//            List<int[]> dfList = ZhanDouFun.dfTank(minMap);



        } catch (Exception e) {
            logger.debug("不是在战斗中{}", e);
        }
    }

    private static void kaiche(BufferedImage minMap) throws InterruptedException {
        if (CF.LU_XIAN == null) {    //如果没有读取到地图，则直接两次r
            logger.debug("没有读取到地图，则直接两次r");
            CF.robot.keyPress(KeyEvent.VK_R);
            CF.robot.keyRelease(KeyEvent.VK_R);
            Thread.sleep(150);
            CF.robot.keyPress(KeyEvent.VK_R);
            CF.robot.keyRelease(KeyEvent.VK_R);
            return;
        }
        int[] addrs = ZhanDouFun.myAddr(minMap);
        logger.debug("进入开车界面");
//        StartMain.LU_XIAN = MinMapLX.SI_DU_JI_ANG_QI;
        //鼠标控制
//        Thread paotat = new Thread(new PaoTaKongZhi(minMap, addrs));
//        paotat.setName("paotaThread");
//        paotat.start();

        if (addrs == null) {
            FangXiangKongZhi.xuyaoW = true;
            return;
        }
        double lx0 = ZhanDouFun.dd2(CF.LU_XIAN.get(0), addrs);
        double lx1 = ZhanDouFun.dd2(CF.LU_XIAN.get(1), addrs);

        CF.robot.mouseMove(CF.SCRN_SIZE[0]/2, CF.CENTER_Y);     //鼠标移动到屏幕中间

        if (lx0 < lx1) {
            for (int i = 2; i < CF.LU_XIAN.size(); i++) {
                FangXiangKongZhi.kongzhi(i);
            }
            FangXiangKongZhi.kongzhi(1);
        }else {
            for (int i = CF.LU_XIAN.size() - 1; i >= 2; i--) {
                FangXiangKongZhi.kongzhi(i);
            }
            FangXiangKongZhi.kongzhi(0);
        }
    }



    public static void main(String[] args) {
        System.out.println(-2 % 10);
    }
}
