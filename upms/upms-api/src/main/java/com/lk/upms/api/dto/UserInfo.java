

package com.lk.upms.api.dto;

import com.lk.upms.api.entity.SysPost;
import com.lk.upms.api.entity.SysRole;
import com.lk.upms.api.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2019/2/1
 * <p>
 * commit('SET_ROLES', data) commit('SET_NAME', data) commit('SET_AVATAR', data)
 * commit('SET_INTRODUCTION', data) commit('SET_PERMISSIONS', data)
 */
@Data
public class UserInfo implements Serializable {

    /**
     * 用户基本信息
     */
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Long[] roles;

    /**
     * 角色集合
     */
    private List<SysRole> roleList;

    /**
     * 岗位集合
     */
    private Long[] posts;

    /**
     * 岗位集合
     */
    private List<SysPost> postList;

}
