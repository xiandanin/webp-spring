package com.dyhdyh.webp.model;

import java.io.Serializable;

/**
 * author  dengyuhan
 * created 2018/6/20 18:41
 */
public class WebPConfig  implements Serializable {
    private int quality;
    private String outputDir;
    private boolean anim;
    private long frameInterval;
    private int loopCount;

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    public long getFrameInterval() {
        return frameInterval;
    }

    public void setFrameInterval(long frameInterval) {
        this.frameInterval = frameInterval;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public boolean isAnim() {
        return anim;
    }

    public void setAnim(boolean anim) {
        this.anim = anim;
    }
}
