

package com.lk.upms.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.upms.api.dto.SysLogDTO;
import com.lk.upms.api.entity.SysLog;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @since 2019/2/1
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询日志
     *
     * @param page
     * @param sysLog
     * @return
     */
    Page<SysLog> getLogByPage(Page page, SysLogDTO sysLog);

    /**
     * 列表查询日志
     *
     * @param sysLog 查询条件
     * @return List
     */
    List<SysLog> getLogList(SysLogDTO sysLog);

}
