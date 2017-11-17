package com.xu.baselib.utils;

/**
 * Created by scorpio on 15/9/16.
 */
public class Gps {
    private float Radius;
    private double wgLat;
    private double wgLon;

    public Gps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    public Gps(double wgLat, double wgLon, float radius) {
        this.wgLat = wgLat;
        this.wgLon = wgLon;
        Radius = radius;
    }

    public float getRadius() {
        return Radius;
    }

    public void setRadius(float radius) {
        Radius = radius;
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public String toString() {
        return wgLon + "," + wgLat;
    }
}


