

package com.lk.upms.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.lk.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 部门管理
 * </p>
 *
 * @since 2019/2/1
 */
@Schema(description = "部门")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "dept_id", type = IdType.ASSIGN_ID)
    @Schema(description = "部门id")
    private Long deptId;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称", required = true)
    private String name;

    /**
     * 排序
     */
    @NotNull(message = "部门排序值不能为空")
    @Schema(description = "排序值", required = true)
    private Integer sortOrder;

    /**
     * 父级部门id
     */
    @Schema(description = "父级部门id")
    private Long parentId;

    /**
     * 是否删除 -1：已删除 0：正常
     */
    @TableLogic
    private String delFlag;

}
