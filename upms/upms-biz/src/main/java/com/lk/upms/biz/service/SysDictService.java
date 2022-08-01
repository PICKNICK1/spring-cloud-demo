
package com.lk.upms.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lk.upms.api.entity.SysDict;

/**
 * 字典表
 *
 * @date 2019/03/19
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 根据ID 删除字典
     *
     * @param id
     * @return
     */
    void removeDict(Long id);

    /**
     * 更新字典
     *
     * @param sysDict 字典
     * @return
     */
    void updateDict(SysDict sysDict);

    /**
     * 清除缓存
     */
    void clearDictCache();

}
