package com.pt.gm.tank.po;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-23 17:42
 */
public class DifangTank {
    private int x;
    private int y;

    private double waiquan;
    private double neiquan;

    public int[] getAddr(){
        return new int[]{x, y};
    }

    public DifangTank(int x, int y, double waiquan, double neiquan) {
        this.x = x;
        this.y = y;
        this.waiquan = waiquan;
        this.neiquan = neiquan;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getWaiquan() {
        return waiquan;
    }

    public void setWaiquan(double waiquan) {
        this.waiquan = waiquan;
    }

    public double getNeiquan() {
        return neiquan;
    }

    public void setNeiquan(double neiquan) {
        this.neiquan = neiquan;
    }

    @Override
    public String toString() {
        return "DifangTank{" +
                "x=" + x +
                ", y=" + y +
                ", waiquan=" + waiquan +
                ", neiquan=" + neiquan +
                '}';
    }
}
