package cn.lyz.fileupload.service;

import cn.lyz.fileupload.domain.AjaxResult;
import cn.lyz.fileupload.domain.FileChunk;
import cn.lyz.fileupload.domain.FileInfo;
import cn.lyz.fileupload.mapper.FileChunkMapper;
import cn.lyz.fileupload.mapper.FileInfoMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
@Slf4j
@Service
public class UploadService {
    /**
     * 默认的分片大小：50MB
     */
    public static final long DEFAULT_CHUNK_SIZE = 50 * 1024 * 1024;

    @Resource
    private FileChunkMapper fileChunkMapper;

    @Resource
    private FileInfoMapper fileInfoMapper;

    public AjaxResult checkUpload(FileChunk fileChunk) {
        List<FileChunk> list = fileChunkMapper.findFileChunkParamByMd5(fileChunk.getIdentifier());
        Map<String, Object> data = new HashMap<>(1);
        // 判断文件存不存在
        if (list == null || list.size() == 0) {
            data.put("uploaded", false);
            return AjaxResult.success("文件上传成功",data);
        }
        // 处理单文件
        if (list.get(0).getTotalChunks() == 1) {
            data.put("uploaded", true);
            data.put("url", "");
            return AjaxResult.success("文件上传成功",data);
        }
        // 处理分片
        int[] uploadedFiles = new int[list.size()];
        int index = 0;
        for (FileChunk fileChunkItem : list) {
            uploadedFiles[index] = fileChunkItem.getChunkNumber();
            index++;
        }
        data.put("uploadedChunks", uploadedFiles);
        return AjaxResult.success("文件上传成功",data);
    }

    /**
     * 上传分片文件
     * @param fileChunk
     * @return
     * @throws Exception
     */
    public AjaxResult uploadChunkFile(FileChunk fileChunk) throws IOException {

        String newFileName = fileChunk.getIdentifier() + fileChunk.getFilename();
        String filePath = "D:\\" + newFileName;
        uploadFileByRandomAccessFile(filePath, fileChunk);
        fileChunk.setCreateTime(new Date());
        fileChunkMapper.insertFileChunk(fileChunk);

        //数据库中已上传的分片总数
        Integer count = fileChunkMapper.findCountByMd5(fileChunk.getIdentifier());
        if(fileChunk.getTotalChunks().equals(count)){
            FileInfo fileInfo = new FileInfo();
            String originalFilename = fileChunk.getFile().getOriginalFilename();
            fileInfo.setId(UUID.randomUUID().toString());
            fileInfo.setOriginFileName(originalFilename);
            fileInfo.setFileName(newFileName);
            fileInfo.setFilePath(filePath);
            fileInfo.setFileSize(fileChunk.getTotalSize());
            fileInfo.setCreateTime(new Date());
            fileInfoMapper.insert(fileInfo);
        }
        return AjaxResult.success("文件上传成功");
    }

    private void uploadFileByRandomAccessFile(String filePath, FileChunk fileChunk) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
        // 分片大小必须和前端匹配，否则上传会导致文件损坏
        long chunkSize = fileChunk.getChunkSize() == 0L ? DEFAULT_CHUNK_SIZE : fileChunk.getChunkSize().longValue();
        // 偏移量
        long offset = chunkSize * (fileChunk.getChunkNumber() - 1);
        // 定位到该分片的偏移量
        randomAccessFile.seek(offset);
        // 写入
        randomAccessFile.write(fileChunk.getFile().getBytes());
        randomAccessFile.close();

    }
}
