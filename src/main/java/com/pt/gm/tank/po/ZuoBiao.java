package com.pt.gm.tank.po;

import java.util.Objects;

/**
 * @author pantao
 * @version 1.0.0
 * @program worldoftank
 * @description
 * @create 2023-08-09 21:22
 */
public class ZuoBiao {
    private int x;
    private int y;

    public ZuoBiao(int x, int y) {
        this.x = x;
        this.y = y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZuoBiao)) return false;
        ZuoBiao zuoBiao = (ZuoBiao) o;
        return Math.abs(getX() - zuoBiao.getX()) < 3 &&
                Math.abs(getY() - zuoBiao.getY()) < 3;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX()/3, getY()/3);
    }

    @Override
    public String toString() {
        return "ZuoBiao{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
