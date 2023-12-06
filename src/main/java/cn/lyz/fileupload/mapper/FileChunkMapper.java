package cn.lyz.fileupload.mapper;

import cn.lyz.fileupload.domain.FileChunk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FileChunkMapper {
    public List<FileChunk> findFileChunkParamByMd5(String identifier);

    public Integer findCountByMd5(String identifier);

    public int insertFileChunk(FileChunk fileChunk);
}
