package com.legendmohe.circleview.model;

/**
 * Created by legendmohe on 16/5/22.
 */
public class Dot {
    public int alpha;
    public int baseAlpha;
    public int baseX;
    public int baseY;
    public int radius;
    public int velocity;
    public double angle;
    public int distance;
    public int baseDistance;

    public Dot(int baseY, int baseX) {
        this.baseY = baseY;
        this.baseX = baseX;
    }

    public int getCenterX() {
        return baseX + (int) (distance*Math.cos(angle));
    }

    public int getCenterY() {
        return baseY + (int) (distance*Math.sin(angle));
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getBaseY() {
        return baseY;
    }

    public void setBaseY(int baseY) {
        this.baseY = baseY;
    }

    public int getBaseX() {
        return baseX;
    }

    public void setBaseX(int baseX) {
        this.baseX = baseX;
    }

    public int getBaseAlpha() {
        return baseAlpha;
    }

    public void setBaseAlpha(int baseAlpha) {
        this.baseAlpha = baseAlpha;
    }

    public int getBaseDistance() {
        return baseDistance;
    }

    public void setBaseDistance(int baseDistance) {
        this.baseDistance = baseDistance;
    }
}
