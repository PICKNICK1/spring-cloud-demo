

package com.lk.common.log.event;

import com.lk.upms.api.entity.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 *
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLog source) {
        super(source);
    }

}
