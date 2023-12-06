package cn.lyz.fileupload.controller;

import cn.lyz.fileupload.domain.AjaxResult;
import cn.lyz.fileupload.domain.FileChunk;
import cn.lyz.fileupload.service.UploadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "*")
public class FileController {

    @Resource
    private UploadService uploadService;

    @GetMapping("/upload")
    public AjaxResult checkUpload(FileChunk fileChunk){
        return uploadService.checkUpload(fileChunk);
    }

    @PostMapping("/upload")
    public AjaxResult uploadChunkFile(FileChunk fileChunk) throws Exception {
        return uploadService.uploadChunkFile(fileChunk);
    }

}
