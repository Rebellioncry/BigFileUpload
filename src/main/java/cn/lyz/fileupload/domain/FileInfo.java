package cn.lyz.fileupload.domain;

import lombok.Data;

import java.util.Date;
@Data
public class FileInfo {
    /**
     * 主键
     */
    private String id;

    /**
     * 文件源名
     */
    private String originFileName;

    /**
     * 新存放文件名
     */
    private String fileName;

    /**
     * 文件存放路径
     */
    private String filePath;

    /**
     * 文件总大小
     */
    private Long fileSize;

    /**
     * 创建时间
     */
    private Date createTime;
}
