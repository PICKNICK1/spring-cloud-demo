

package com.lk.upms.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.upms.api.entity.SysRole;
import com.lk.upms.api.vo.RoleExcelVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @since 2019/2/1
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 通过角色ID，删除角色
     *
     * @param id
     * @return
     */
    Boolean removeRoleById(Long id);


    /**
     * 查询全部的角色
     *
     * @return list
     */
    List<RoleExcelVO> listRole();

}
