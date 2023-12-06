package cn.lyz.fileupload.mapper;

import cn.lyz.fileupload.domain.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileInfoMapper {
    int insert(FileInfo fileInfo);
}
