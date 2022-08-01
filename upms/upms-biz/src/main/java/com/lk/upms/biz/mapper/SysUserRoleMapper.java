

package com.lk.upms.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lk.upms.api.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @since 2019/2/1
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     * @date 2017年12月7日 16:31:38
     */
    Boolean deleteByUserId(@Param("userId") Long userId);

}
