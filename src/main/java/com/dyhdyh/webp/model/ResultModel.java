package com.dyhdyh.webp.model;

/**
 * author  dengyuhan
 * created 2018/6/20 17:34
 */
public class ResultModel {
    private boolean success;
    private String result;
    private String outputPath;
    private String outputTime;

    public ResultModel(boolean success, String result, String outputPath, String outputTime) {
        this.success = success;
        this.result = result;
        this.outputPath = outputPath;
        this.outputTime = outputTime;
    }

    public ResultModel() {
    }

    public String getOutputTime() {
        return outputTime;
    }

    public void setOutputTime(String outputTime) {
        this.outputTime = outputTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
