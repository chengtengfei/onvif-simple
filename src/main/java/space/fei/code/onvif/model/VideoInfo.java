package space.fei.code.onvif.model;

public class VideoInfo {

    private String videoEncoding;
    private Integer videoWidth;
    private Integer videoHeight;
    /**
     * 帧率(F/S)
     */
    private Integer frameRateLimit;
    /**
     * 码率(Kb/S)
     */
    private Integer bitrateLimit;
    private String streamUri;

    public String getVideoEncoding() {
        return videoEncoding;
    }

    public void setVideoEncoding(String videoEncoding) {
        this.videoEncoding = videoEncoding;
    }

    public Integer getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(Integer videoWidth) {
        this.videoWidth = videoWidth;
    }

    public Integer getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }

    public Integer getFrameRateLimit() {
        return frameRateLimit;
    }

    public void setFrameRateLimit(Integer frameRateLimit) {
        this.frameRateLimit = frameRateLimit;
    }

    public Integer getBitrateLimit() {
        return bitrateLimit;
    }

    public void setBitrateLimit(Integer bitrateLimit) {
        this.bitrateLimit = bitrateLimit;
    }

    public String getStreamUri() {
        return streamUri;
    }

    public void setStreamUri(String streamUri) {
        this.streamUri = streamUri;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "videoEncoding='" + videoEncoding + '\'' +
                ", videoWidth=" + videoWidth +
                ", videoHeight=" + videoHeight +
                ", frameRateLimit=" + frameRateLimit +
                ", bitrateLimit=" + bitrateLimit +
                ", streamUri='" + streamUri + '\'' +
                '}';
    }
}
