

package com.lk.upms.biz.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lk.common.core.constant.CacheConstants;
import com.lk.common.core.constant.CommonConstants;
import com.lk.common.core.constant.enums.MenuTypeEnum;
import com.lk.common.core.exception.ErrorCodes;
import com.lk.common.core.util.MsgUtils;
import com.lk.common.core.util.R;
import com.lk.upms.api.convert.UserConvert;
import com.lk.upms.api.dto.UserDTO;
import com.lk.upms.api.dto.UserInfo;
import com.lk.upms.api.entity.*;
import com.lk.upms.api.util.ParamResolver;
import com.lk.upms.api.vo.UserVO;
import com.lk.upms.biz.mapper.*;
import com.lk.upms.biz.service.SysMenuService;
import com.lk.upms.biz.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @date 2019/2/1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final SysRoleMapper sysRoleMapper;

    private final SysDeptMapper sysDeptMapper;

    private final SysMenuService sysMenuService;

    private final SysPostMapper sysPostMapper;

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysUserPostMapper sysUserPostMapper;

    private final UserConvert userConvert;

    /**
     * ??????????????????
     *
     * @param userDto DTO ??????
     * @return success/fail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = userConvert.userDTO2User(userDto);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        baseMapper.insert(sysUser);
        userDto.getRole().stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            return userRole;
        }).forEach(sysUserRoleMapper::insert);
        // ????????????????????????
        Optional.ofNullable(userDto.getPost()).ifPresent(posts -> {
            posts.stream().map(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(sysUser.getUserId());
                userPost.setPostId(postId);
                return userPost;
            }).forEach(sysUserPostMapper::insert);
        });
        return Boolean.TRUE;
    }

    /**
     * ??????????????????????????????
     *
     * @param sysUser ??????
     * @return
     */
    @Override
    public UserInfo getUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        // ??????????????????
        List<SysRole> roleList = sysRoleMapper.listRolesByUserId(sysUser.getUserId());
        userInfo.setRoleList(roleList);
        // ?????????????????? ???ID???
        List<Long> roleIds = roleList.stream().map(SysRole::getRoleId).collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Long.class));
        // ??????????????????
        List<SysPost> postList = sysPostMapper.listPostsByUserId(sysUser.getUserId());
        userInfo.setPostList(postList);
        // ?????????????????????menu.permission???
        Set<String> permissions = roleIds.stream().map(sysMenuService::findMenuByRoleId).flatMap(Collection::stream)
                .filter(m -> MenuTypeEnum.BUTTON.getType().equals(m.getType())).map(SysMenu::getPermission)
                .filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));

        return userInfo;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param page    ????????????
     * @param userDTO ????????????
     * @return
     */
    @Override
    public IPage<UserVO> getUserWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * ??????ID??????????????????
     *
     * @param id ??????ID
     * @return ????????????
     */
    @Override
    public UserVO getUserVoById(Long id) {
        return baseMapper.getUserVoById(id);
    }

    /**
     * ????????????
     *
     * @param sysUser ??????
     * @return Boolean
     */
    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
    public Boolean removeUserById(SysUser sysUser) {
        sysUserRoleMapper.deleteByUserId(sysUser.getUserId());
        this.removeById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUserInfo(UserDTO userDto) {
        UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());

        Assert.isTrue(ENCODER.matches(userDto.getPassword(), userVO.getPassword()),
                MsgUtils.getMessage(ErrorCodes.SYS_USER_UPDATE_PASSWORDERROR));

        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getNewpassword1())) {
            sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVO.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return this.updateById(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto) {
        SysUser sysUser = userConvert.userDTO2User(userDto);
        sysUser.setUpdateTime(LocalDateTime.now());
        this.updateById(sysUser);

        sysUserRoleMapper
                .delete(Wrappers.<SysUserRole>update().lambda().eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery().eq(SysUserPost::getUserId, userDto.getUserId()));
        userDto.getPost().forEach(postId -> {
            SysUserPost userPost = new SysUserPost();
            userPost.setUserId(sysUser.getUserId());
            userPost.setPostId(postId);
            userPost.insert();
        });
        return Boolean.TRUE;
    }

    /**
     * ?????????????????????????????????
     *
     * @param username ?????????
     * @return R
     */
    @Override
    public List<SysUser> listAncestorUsersByUsername(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

        SysDept sysDept = sysDeptMapper.selectById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Long parentId = sysDept.getParentId();
        return this.list(Wrappers.<SysUser>query().lambda().eq(SysUser::getDeptId, parentId));
    }


    @Override
    public List<Long> listUserIdByDeptIds(Set<Long> deptIds) {
        return this.listObjs(
                Wrappers.lambdaQuery(SysUser.class).select(SysUser::getUserId).in(SysUser::getDeptId, deptIds),
                Long.class::cast);
    }

    /**
     * ???????????? ????????????????????????
     *
     * @param userDto ????????????
     * @return success/false
     */
    @Override
    public R<Boolean> registerUser(UserDTO userDto) {
        // ???????????????????????????
        SysUser sysUser = this.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userDto.getUsername()));
        if (sysUser != null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, userDto.getUsername()));
        }

        // ????????????????????????
        String defaultRole = ParamResolver.getStr("USER_DEFAULT_ROLE");
        // ????????????
        SysRole sysRole = sysRoleMapper
                .selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, defaultRole));

        if (sysRole == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_PARAM_CONFIG_ERROR, "USER_DEFAULT_ROLE"));
        }

        userDto.setRole(Collections.singletonList(sysRole.getRoleId()));
        return R.ok(saveUser(userDto));
    }

}
