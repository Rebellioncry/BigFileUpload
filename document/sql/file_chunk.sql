CREATE TABLE `file_chunk`  (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `identifier` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件md5',
                               `chunk_number` int NULL DEFAULT NULL COMMENT '当前分块序号',
                               `chunk_size` bigint NULL DEFAULT NULL COMMENT '分块大小',
                               `current_chunk_size` bigint NULL DEFAULT NULL COMMENT '当前分块大小',
                               `total_size` bigint NULL DEFAULT NULL COMMENT '文件总大小',
                               `total_chunks` int NULL DEFAULT NULL COMMENT '分块总数',
                               `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
                               `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件分片表' ROW_FORMAT = DYNAMIC;
