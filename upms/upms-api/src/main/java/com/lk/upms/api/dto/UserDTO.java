

package com.lk.upms.api.dto;

import com.lk.upms.api.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @date 2019/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

    /**
     * 角色ID
     */
    private List<Long> role;

    private Long deptId;

    /**
     * 岗位ID
     */
    private List<Long> post;

    /**
     * 新密码
     */
    private String newpassword1;

}
