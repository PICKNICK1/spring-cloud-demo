

package com.lk.upms.api.dto;

import com.lk.upms.api.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @date 2019/2/1 角色Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends SysRole {

    /**
     * 角色部门Id
     */
    private Long roleDeptId;

    /**
     * 部门名称
     */
    private String deptName;

}
