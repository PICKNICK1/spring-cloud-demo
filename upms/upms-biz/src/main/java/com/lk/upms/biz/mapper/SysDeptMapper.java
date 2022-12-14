

package com.lk.upms.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lk.upms.api.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @since 2019/2/1
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 关联dept——relation
     *
     * @return 数据列表
     */
    List<SysDept> listDepts();

}
