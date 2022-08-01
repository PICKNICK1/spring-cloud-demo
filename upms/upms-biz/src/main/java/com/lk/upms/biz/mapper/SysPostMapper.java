
package com.lk.upms.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lk.upms.api.entity.SysPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 岗位管理表 mapper接口
 *
 * @date 2022-03-15 17:18:40
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * 通过用户ID，查询岗位信息
     *
     * @param userId 用户id
     * @return 岗位信息
     */
    List<SysPost> listPostsByUserId(Long userId);

}
