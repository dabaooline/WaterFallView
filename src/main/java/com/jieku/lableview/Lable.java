package com.jieku.lableview;

/**
 * Created by baoli on 2016/7/22.
 */
public class Lable {

    private int xPosition;
    private int yPosition;
    private int xDirection;
    private int yDirection;
    private int lableWidth;
    private int lableHeight;
    private String contents;
    private int fontSize;


    public Lable(String contents) {
        this.contents = contents;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getxDirection() {
        return xDirection;
    }

    public void setxDirection(int xDirection) {
        this.xDirection = xDirection;
    }

    public int getyDirection() {
        return yDirection;
    }

    public void setyDirection(int yDirection) {
        this.yDirection = yDirection;
    }

    public int getLableWidth() {
        return lableWidth;
    }

    public void setLableWidth(int lableWidth) {
        this.lableWidth = lableWidth;
    }

    public int getLableHeight() {
        return lableHeight;
    }

    public void setLableHeight(int lableHeight) {
        this.lableHeight = lableHeight;
    }
}
