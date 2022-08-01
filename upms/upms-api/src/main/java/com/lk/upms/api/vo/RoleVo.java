

package com.lk.upms.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @date 2020/2/10
 */
@Data
@Schema(description = "前端角色展示对象")
public class RoleVo {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单列表
     */
    private String menuIds;

}
