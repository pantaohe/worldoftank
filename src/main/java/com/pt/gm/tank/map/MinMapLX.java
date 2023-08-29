package com.pt.gm.tank.map;

import com.pt.gm.tank.StartMain;
import com.pt.gm.tank.jr.JiaRuZD;
import com.pt.gm.tank.util.ImgUtils;
import com.pt.gm.tank.zhandou.ZhanDou;
import com.pt.gm.tank.zhandou.ZhanDouFun;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftankd
 * @description
 * @create 2023-08-09 10:25
 */
public class MinMapLX {
    private static Logger logger = LoggerFactory.getLogger(MinMapLX.class);
    public static final int QZB = 17;
    //旗子圈为 40 * 40 （半径20）      旗子 23 * 23（半径16左右）      自己 15 * 15（半径8）      坦克圈 38 * 38 （半径19） 坦克图标半径12
    public static List<int[]> AI_LI_HA_LUO_FU = Arrays.asList(new int[]{392, 89}, new int[]{78, 401},
            new int[]{383, 115}, new int[]{390, 178}, new int[]{308, 313}, new int[]{172, 371});
    public static List<int[]> HU_BIAN_DE_JUE_ZHU = Arrays.asList(new int[]{99, 42}, new int[]{352, 431},
            new int[]{156, 114}, new int[]{104, 200}, new int[]{100, 333}, new int[]{215, 347}, new int[]{239, 432});
    public static List<int[]> LA_SHI_WEI_LI = Arrays.asList(new int[]{140, 49}, new int[]{140, 440},
            new int[]{251, 54}, new int[]{338, 143}, new int[]{355, 300}, new int[]{367, 335}, new int[]{310, 400}, new int[]{213, 439});
    public static List<int[]> LU_BIE_KE = Arrays.asList(new int[]{206, 82}, new int[]{196, 426},
            new int[]{286, 88}, new int[]{350, 194}, new int[]{343, 329}, new int[]{280, 426});
    public static List<int[]> JI_DI_BING_YUAN = Arrays.asList(new int[]{78, 394}, new int[]{439, 100},
            new int[]{182, 419}, new int[]{335, 362}, new int[]{337, 344}, new int[]{354, 209}, new int[]{419, 167});
    public static List<int[]> AN_SI_KE = Arrays.asList(new int[]{260, 40}, new int[]{260, 448},
            new int[]{220, 64}, new int[]{220, 99}, new int[]{229, 193}, new int[]{219, 384}, new int[]{199, 426});
    public static List<int[]> KA_LEI_LI_A = Arrays.asList(new int[]{439, 47}, new int[]{47, 440},
            new int[]{275, 155}, new int[]{189, 257}, new int[]{145, 303});
    public static List<int[]> SHEN_LI_ZHI_MEN = Arrays.asList(new int[]{59, 62}, new int[]{413, 440},
            new int[]{57, 84}, new int[]{70, 119}, new int[]{94, 139}, new int[]{100, 178}, new int[]{148, 245}, new int[]{229, 355}, new int[]{308, 352}, new int[]{361, 401});
    public static List<int[]> HUANG_MO_XIAO_ZHEN = Arrays.asList(new int[]{45, 176}, new int[]{427, 331},
            new int[]{153, 475}, new int[]{355, 436}, new int[]{368, 344});
    public static List<int[]> HUANG_MAN_ZHI_DI = Arrays.asList(new int[]{200, 67}, new int[]{356, 412},
            new int[]{211, 147}, new int[]{330, 333});
    public static List<int[]> KAN_PA_NI_YA = Arrays.asList(new int[]{221, 95}, new int[]{242, 406},
            new int[]{264, 101}, new int[]{220, 375}, new int[]{167, 384});
    public static List<int[]> PU_LUO_HUO = Arrays.asList(new int[]{182, 25}, new int[]{269, 463},
            new int[]{56, 156}, new int[]{59, 330});
    public static List<int[]> MA_LI_NUO_FU_KA = Arrays.asList(new int[]{61, 191}, new int[]{281, 436},
            new int[]{66, 247}, new int[]{223, 405});
    public static List<int[]> MU_NI_HEI = Arrays.asList(new int[]{295, 46}, new int[]{176, 405},
            new int[]{255, 205}, new int[]{180, 376});
    public static List<int[]> BU_LA_GE = Arrays.asList(new int[]{210, 45}, new int[]{209, 446},
            new int[]{282, 80}, new int[]{302, 160}, new int[]{303, 361}, new int[]{287, 430});
    public static List<int[]> WU_MONG_XIONG_SHAN = Arrays.asList(new int[]{89, 97}, new int[]{391, 393},
            new int[]{153, 179}, new int[]{145, 287}, new int[]{264, 355}, new int[]{335, 294});
    public static List<int[]> WEI_SI_TE_FEI_ER_DE = Arrays.asList(new int[]{97, 411}, new int[]{410, 97},
            new int[]{273, 340}, new int[]{323, 277});
    public static List<int[]> MU_LEI_WAN_KA = Arrays.asList(new int[]{217, 40}, new int[]{217, 443},
            new int[]{331, 123}, new int[]{363, 352});
    public static List<int[]> HAI_AN_ZHENG_BA = Arrays.asList(new int[]{121, 32}, new int[]{110, 458},
            new int[]{48, 169}, new int[]{53, 353});
    public static List<int[]> JI_JING_HAI_AN = Arrays.asList(new int[]{349, 83}, new int[]{182, 394},
            new int[]{230, 102}, new int[]{178, 202}, new int[]{153, 253});
    public static List<int[]> WEN_HUA_ZHI_DU = Arrays.asList(new int[]{80, 286}, new int[]{401, 275},
            new int[]{61, 242}, new int[]{99, 413}, new int[]{410, 418});
    public static List<int[]> MU_LI_WAN = Arrays.asList(new int[]{365, 100}, new int[]{109, 374},
            new int[]{264, 125}, new int[]{124, 275});
    public static List<int[]> QI_GE_FEI_FANG_XIAN = Arrays.asList(new int[]{383, 31}, new int[]{369, 460},
            new int[]{132, 88}, new int[]{54, 242}, new int[]{192, 367});
    public static List<int[]> AI_LEI_SI_BAO = Arrays.asList(new int[]{196, 45}, new int[]{294, 446},
            new int[]{131, 81}, new int[]{146, 456}, new int[]{365, 22}, new int[]{372, 415});
    public static List<int[]> KA_QIU_SHA = Arrays.asList(new int[]{200, 158}, new int[]{296, 347},
            new int[]{127, 222}, new int[]{139, 315}, new int[]{183, 341});
    public static List<int[]> XIAO_ZHENG_ZHEN_DOU_ZHAN = Arrays.asList(new int[]{244, 50}, new int[]{254, 434},
            new int[]{241, 114}, new int[]{288, 182}, new int[]{289, 307}, new int[]{257, 386});
    public static List<int[]> LA_TE_SI_TE_FU = Arrays.asList(new int[]{244, 43}, new int[]{242, 453},
            new int[]{210, 174}, new int[]{202, 252});
    public static List<int[]> BI_FENG_GANG = Arrays.asList(new int[]{64, 168}, new int[]{409, 169},
            new int[]{53, 195}, new int[]{62, 217}, new int[]{117, 225}, new int[]{146, 269}, new int[]{353, 266});
    public static List<int[]> A_LA_MAN_JI_CHANG = Arrays.asList(new int[]{85, 330}, new int[]{421, 320},
            new int[]{26, 251}, new int[]{210, 266}, new int[]{279, 274}, new int[]{418, 230}, new int[]{477, 260});
    public static List<int[]> GANG_TIE_BI_LEI = Arrays.asList(new int[]{78, 54}, new int[]{438, 429},
            new int[]{31, 224}, new int[]{24, 449}, new int[]{242, 429}, new int[]{367, 465});
    public static List<int[]> QIAN_SHAO = Arrays.asList(new int[]{358, 108}, new int[]{98, 330},
            new int[]{239, 128}, new int[]{160, 160}, new int[]{126, 232});//126 232
    public static List<int[]> MEI_YING_XIAO_ZHENG = Arrays.asList(new int[]{244, 72}, new int[]{244, 416},
            new int[]{397, 108}, new int[]{406, 360});
    public static List<int[]> BEI_OU_HAI_WAN = Arrays.asList(new int[]{52, 182}, new int[]{440, 265},
            new int[]{92, 108}, new int[]{186, 128}, new int[]{285, 172}, new int[]{304, 229});
    public static List<int[]> FEI_SHE_ER_WAN = Arrays.asList(new int[]{202, 49}, new int[]{235, 438},
            new int[]{42, 119}, new int[]{28, 218}, new int[]{8, 319});
    public static List<int[]> LANG_MAN_ZHI_CHENG = Arrays.asList(new int[]{32, 258}, new int[]{453, 256},
            new int[]{94, 212}, new int[]{91, 139}, new int[]{383, 138}, new int[]{403, 212});
    public static List<int[]> LI_FU_AO_KE_SI = Arrays.asList(new int[]{391, 67}, new int[]{69, 405},
            new int[]{202, 148}, new int[]{116, 227});
    public static List<int[]> BING_CHUAN_ZHI_DI = Arrays.asList(new int[]{418, 123}, new int[]{95, 434},
            new int[]{360, 160}, new int[]{210, 225}, new int[]{147, 335});
    public static List<int[]> ZHOU_JI_GONG_LU = Arrays.asList(new int[]{340, 71}, new int[]{76, 404},
            new int[]{154, 204}, new int[]{386, 256});
    public static List<int[]> LUO_MAN_DI = Arrays.asList(new int[]{269, 49}, new int[]{267, 439},
            new int[]{280, 141}, new int[]{328, 250}, new int[]{289, 356});
    public static List<int[]> SI_DU_JI_ANG_QI = Arrays.asList(new int[]{52, 260}, new int[]{442, 265},
            new int[]{228, 258}, new int[]{287, 263});


    private static boolean luXian(String fileContent) {
        logger.debug("读取的地图名：" + fileContent);
        if (fileContent.contains("埃里-哈罗夫")){
            StartMain.LU_XIAN = AI_LI_HA_LUO_FU;
        }else if (fileContent.contains("湖边的角逐")){
            StartMain.LU_XIAN = HU_BIAN_DE_JUE_ZHU;
        }else if (fileContent.contains("拉斯威利")){
            StartMain.LU_XIAN = LA_SHI_WEI_LI;
        }else if (fileContent.contains("鲁别克")){
            StartMain.LU_XIAN = LU_BIE_KE;
        }else if (fileContent.contains("极地冰原")){
            StartMain.LU_XIAN = JI_DI_BING_YUAN;
        }else if (fileContent.contains("安斯克")){
            StartMain.LU_XIAN = AN_SI_KE;
        }else if (fileContent.contains("卡累利阿")){
            StartMain.LU_XIAN = KA_LEI_LI_A;
        }else if (fileContent.contains("卡果利阿")){
            StartMain.LU_XIAN = KA_LEI_LI_A;
        }else if (fileContent.contains("胜利之门")){
            StartMain.LU_XIAN = SHEN_LI_ZHI_MEN;
        }else if (fileContent.contains("荒漠小镇")){
            StartMain.LU_XIAN = HUANG_MO_XIAO_ZHEN;
        }else if (fileContent.contains("荒蛮之地")){
            StartMain.LU_XIAN = HUANG_MAN_ZHI_DI;
        }else if (fileContent.contains("坎帕尼亚")){
            StartMain.LU_XIAN = KAN_PA_NI_YA;
        }else if (fileContent.contains("普罗霍洛夫卡")){
            StartMain.LU_XIAN = PU_LUO_HUO;
        }else if (fileContent.contains("马利诺夫卡")){
            StartMain.LU_XIAN = MA_LI_NUO_FU_KA;
        }else if (fileContent.contains("慕尼黑")){
            StartMain.LU_XIAN = MU_NI_HEI;
        }else if (fileContent.contains("布拉格")){
            StartMain.LU_XIAN = BU_LA_GE;
        }else if (fileContent.contains("乌蒙雄山")){
            StartMain.LU_XIAN = WU_MONG_XIONG_SHAN;
        }else if (fileContent.contains("韦斯特菲尔德")){
            StartMain.LU_XIAN = WEI_SI_TE_FEI_ER_DE;
        }else if (fileContent.contains("穆勒万卡")){
            StartMain.LU_XIAN = MU_LEI_WAN_KA;
        }else if (fileContent.contains("海岸争霸")){
            StartMain.LU_XIAN = HAI_AN_ZHENG_BA;
        }else if (fileContent.contains("寂静海岸")){
            StartMain.LU_XIAN = JI_JING_HAI_AN;
        }else if (fileContent.contains("文化之都")){
            StartMain.LU_XIAN = WEN_HUA_ZHI_DU;
        }else if (fileContent.contains("牡蛎湾")){
            StartMain.LU_XIAN = MU_LI_WAN;
        }else if (fileContent.contains("齐格菲防线")){
            StartMain.LU_XIAN = QI_GE_FEI_FANG_XIAN;
        }else if (fileContent.contains("埃勒斯堡")){
            StartMain.LU_XIAN = AI_LEI_SI_BAO;
        }else if (fileContent.contains("喀秋莎")){
            StartMain.LU_XIAN = KA_QIU_SHA;
        }else if (fileContent.contains("小镇争夺战")){
            StartMain.LU_XIAN = XIAO_ZHENG_ZHEN_DOU_ZHAN;
        }else if (fileContent.contains("斯特拉特福")){
            StartMain.LU_XIAN = LA_TE_SI_TE_FU;
        }else if (fileContent.contains("避风港")){
            StartMain.LU_XIAN = BI_FENG_GANG;
        }else if (fileContent.contains("阿拉曼机场")){
            StartMain.LU_XIAN = A_LA_MAN_JI_CHANG;
        }else if (fileContent.contains("钢铁壁垒")){
            StartMain.LU_XIAN = GANG_TIE_BI_LEI;
        }else if (fileContent.contains("前哨")){
            StartMain.LU_XIAN = QIAN_SHAO;
        }else if (fileContent.contains("魅影小镇")){
            StartMain.LU_XIAN = MEI_YING_XIAO_ZHENG;
        }else if (fileContent.contains("北欧峡湾")){
            StartMain.LU_XIAN = BEI_OU_HAI_WAN;
        }else if (fileContent.contains("费舍尔湾")){
            StartMain.LU_XIAN = FEI_SHE_ER_WAN;
        }else if (fileContent.contains("浪漫之城")){
            StartMain.LU_XIAN = LANG_MAN_ZHI_CHENG;
        }else if (fileContent.contains("里夫奥克斯")){
            StartMain.LU_XIAN = LI_FU_AO_KE_SI;
        }else if (fileContent.contains("冰川之地")){
            StartMain.LU_XIAN = BING_CHUAN_ZHI_DI;
        }else if (fileContent.contains("州际公路")){
            StartMain.LU_XIAN = ZHOU_JI_GONG_LU;
        }else if (fileContent.contains("诺曼底")){
            StartMain.LU_XIAN = LUO_MAN_DI;
        }else if (fileContent.contains("斯杜季昂奇")){     //斯杜季昂奇、可以直接RR        //锡默尔斯多夫(ban掉)
            StartMain.LU_XIAN = SI_DU_JI_ANG_QI;
        }else{
            return false;
        }
        JiaRuZD.mapName = fileContent;
        return true;
    }





    public static void getXingJingLuXian(BufferedImage screenshot) throws InterruptedException {

        StartMain.robot.keyPress(KeyEvent.VK_N);
        Thread.sleep(1000);
        BufferedImage nImage = ImgUtils.screenshot();
        StartMain.robot.keyRelease(KeyEvent.VK_N);
        BufferedImage subimage = nImage.getSubimage(600, 40, 312, 100);
        String fileContent = ImgUtils.getString(subimage);
        luXian("N-" + fileContent);

//        StartMain.LU_XIAN = MU_LI_WAN;
        if (StartMain.LU_XIAN == null) {
            logger.debug("地图加载失败");
        } else {
            Thread.sleep(300);
            BufferedImage minMap;
            for (int i = 0; i < 8; i++) {
                screenshot = ImgUtils.screenshot();
                minMap = screenshot.getSubimage(StartMain.MAP_START[0], StartMain.MAP_START[1], ZhanDou.MIN_MAP_W, ZhanDou.MIN_MAP_W);
                if (getBsCount(minMap)) break;
                if (i < 6) {
                    StartMain.robot.keyPress(KeyEvent.VK_EQUALS);
                    StartMain.robot.keyRelease(KeyEvent.VK_EQUALS);
                }else {
                    StartMain.robot.keyPress(KeyEvent.VK_MINUS);     //减法
                    StartMain.robot.keyRelease(KeyEvent.VK_MINUS);
                }
                Thread.sleep(200);
            }
            logger.debug("地图加载成功:{}", fileContent);
        }
    }

    private static boolean getBsCount(BufferedImage minMap) {
        int bsCount = 0;
        for (int i = -12; i <= 12; i++) {
            for (int j = -12; j <= 12; j++) {
                if (ZhanDouFun.getBS(StartMain.LU_XIAN.get(0)[0]+i, StartMain.LU_XIAN.get(0)[1]+j, minMap)) bsCount++;
                if (ZhanDouFun.getBS(StartMain.LU_XIAN.get(1)[0]+i, StartMain.LU_XIAN.get(1)[1]+j, minMap)) bsCount++;
            }
        }
        logger.debug("白点个数：{}", bsCount);
        return bsCount > 50;
    }
}
