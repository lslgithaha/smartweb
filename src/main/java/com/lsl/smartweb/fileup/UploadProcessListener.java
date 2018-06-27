package com.lsl.smartweb.fileup;

import org.apache.commons.fileupload.ProgressListener;

/**
 * Create by LSL on 2018\6\27 0027
 * 描述：
 * 版本：1.0.0
 */
public class UploadProcessListener implements ProgressListener {
    private UpStatus status;
    public UploadProcessListener(UpStatus upStatus,String sessionId) {
        this.status = upStatus;
        this.status.setStartTime(System.currentTimeMillis());
        this.status.setPercentage(0);
        this.status.setUploaded(0);
        ProcessController.setStatus(sessionId,status);

    }

    @Override
    public void update(long done, long total, int fileNum) {
        this.status.setTotalSize(total);
        this.status.setFilenum(fileNum);
        this.status.setUploaded(done);
    }
}
