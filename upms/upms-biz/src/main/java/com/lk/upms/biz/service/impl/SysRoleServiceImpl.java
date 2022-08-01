

package com.lk.upms.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lk.upms.api.entity.SysRole;
import com.lk.upms.api.entity.SysRoleMenu;
import com.lk.upms.api.vo.RoleExcelVO;
import com.lk.upms.biz.mapper.SysRoleMapper;
import com.lk.upms.biz.mapper.SysRoleMenuMapper;
import com.lk.upms.biz.service.SysRoleService;
import com.lk.common.core.constant.CacheConstants;
import com.lk.common.core.exception.ErrorCodes;
import com.lk.common.core.util.MsgUtils;
import com.lk.common.core.util.R;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @since 2019/2/1
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public Boolean removeRoleById(Long id) {
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>update().lambda().eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }

    /**
     * 查询全部的角色
     *
     * @return list
     */
    @Override
    public List<RoleExcelVO> listRole() {
        List<SysRole> roleList = this.list(Wrappers.emptyWrapper());
        // 转换成execl 对象输出
        return roleList.stream().map(role -> {
            RoleExcelVO roleExcelVO = new RoleExcelVO();
            BeanUtil.copyProperties(role, roleExcelVO);
            return roleExcelVO;
        }).collect(Collectors.toList());
    }

}
