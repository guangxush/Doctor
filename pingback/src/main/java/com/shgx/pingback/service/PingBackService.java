package com.shgx.pingback.service;

import com.shgx.pingback.model.PingBack;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
public interface PingBackService {
    /**
     * 查询信息
     * @param id
     * @return
     */
    PingBack queryPingBack(Long id);

    /**
     * 保存信息
     * @param pingBack
     * @return
     */
    Boolean savePingBack(PingBack pingBack);

    /**
     * 更新信息
     * @param pingBack
     * @return
     */
    Boolean updatePingBack(PingBack pingBack);
}
