

package com.lk.upms.api.feign;

import com.lk.common.core.constant.SecurityConstants;
import com.lk.common.core.constant.ServiceNameConstants;
import com.lk.common.core.util.R;
import com.lk.upms.api.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @date 2019/2/1
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UMPS_SERVICE)
public interface RemoteUserService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param username 用户名
     * @param from     调用标志
     * @return R
     */
    @GetMapping("/user/info/{username}")
    R<UserInfo> info(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 通过手机号码查询用户、角色信息
     *
     * @param phone 手机号码
     * @param from  调用标志
     * @return R
     */
    @GetMapping("/app/info/{phone}")
    R<UserInfo> infoByMobile(@PathVariable("phone") String phone, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 根据部门id，查询对应的用户 id 集合
     *
     * @param deptIds 部门id 集合
     * @param from    调用标志
     * @return 用户 id 集合
     */
    @GetMapping("/user/ids")
    R<List<Long>> listUserIdByDeptIds(@RequestParam("deptIds") Set<Long> deptIds,
                                      @RequestHeader(SecurityConstants.FROM) String from);

}
