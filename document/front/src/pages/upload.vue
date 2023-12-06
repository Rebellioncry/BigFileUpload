<template>
    <div class="home">
      <uploader
          ref="uploader"
          :options="options"
          :autoStart="false"
          :file-status-text="fileStatusText"
          @file-added="onFileAdded"
          @file-success="onFileSuccess"
          @file-error="onFileError"
          @file-progress="onFileProgress"
          class="uploader-example"
      >
        <uploader-drop>
          <p>将文件拖放到此处以上传</p>
          <uploader-btn>选择文件</uploader-btn>
        </uploader-drop>
        <uploader-files> </uploader-files>
      </uploader>
      <br />
    </div>
  </template>
   
  <script>
  import SparkMD5 from "spark-md5";
  const CHUNK_SIZE = 50 * 1024 * 1024;
  export default {
    data() {
      return {
        options: {
          target: 'http://127.0.0.1:9090/file/upload',
          testChunks: true,
          uploadMethod: "post",
          chunkSize: CHUNK_SIZE,
          simultaneousUploads: 3,
          checkChunkUploadedByResponse: (chunk, message) => {
            let messageObj = JSON.parse(message);
            let dataObj = messageObj.data;
            if (dataObj.uploaded !== undefined) {
              return dataObj.uploaded;
            }
            return (dataObj.uploadedChunks || []).indexOf(chunk.offset + 1) >= 0;
          },
          parseTimeRemaining: function (timeRemaining, parsedTimeRemaining) {
            return parsedTimeRemaining
                .replace(/\syears?/, "年")
                .replace(/\days?/, "天")
                .replace(/\shours?/, "小时")
                .replace(/\sminutes?/, "分钟")
                .replace(/\sseconds?/, "秒");
          },
        },
        fileStatus: {
          success: "上传成功",
          error: "上传错误",
          uploading: "正在上传",
          paused: "停止上传",
          waiting: "等待中",
        },
        uploadFileList: [],
      };
    },
    methods: {
      onFileAdded(file) {
        this.uploadFileList.push(file);
        // 2. 计算文件 MD5 并请求后台判断是否已上传，是则取消上传
        this.getFileMD5(file, (md5) => {
          if (md5 != "") {
            // 修改文件唯一标识
            file.uniqueIdentifier = md5;
            // 恢复上传
            file.resume();
          }
        });
      },
      onFileSuccess(rootFile, file, response, chunk) {
        console.log("上传成功");
      },
      onFileError(rootFile, file, message, chunk) {
        console.log("上传出错：" + message);
      },
      onFileProgress(rootFile, file, chunk) {
        console.log(`当前进度：${Math.ceil(file._prevProgress * 100)}%`);
      },
      
      getFileMD5(file, callback) {
        let spark = new SparkMD5.ArrayBuffer();
        let fileReader = new FileReader();
        let blobSlice =
            File.prototype.slice ||
            File.prototype.mozSlice ||
            File.prototype.webkitSlice;
        let currentChunk = 0;
        let chunks = Math.ceil(file.size / CHUNK_SIZE);
        let startTime = new Date().getTime();
        file.pause();
        loadNext();
        fileReader.onload = function (e) {
          spark.append(e.target.result);
          if (currentChunk < chunks) {
            currentChunk++;
            loadNext();
          } else {
            let md5 = spark.end();
            console.log(
                `MD5计算完毕：${md5}，耗时：${new Date().getTime() - startTime} ms.`
            );
            callback(md5);
          }
        };
        fileReader.onerror = function () {
          this.$message.error("文件读取错误");
          file.cancel();
        };
   
        function loadNext() {
          const start = currentChunk * CHUNK_SIZE;
          const end =
              start + CHUNK_SIZE >= file.size ? file.size : start + CHUNK_SIZE;
          fileReader.readAsArrayBuffer(blobSlice.call(file.file, start, end));
        }
      },
      fileStatusText(status) {
        console.log(11111)
        console.log(status)
        if (status === "md5") {
          return "校验MD5";
        }
        return this.fileStatus[status];
      },
    },
  }
  </script>
  