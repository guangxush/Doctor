package com.shgx.message.service;

import com.shgx.message.model.Message;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
public interface MessageService {
    /**
     * 查询信息
     * @param id
     * @return
     */
    Message queryMessage(Long id);

    /**
     * 保存信息
     * @param message
     * @return
     */
    Boolean saveMessage(Message message);

    /**
     * 更新信息
     * @param message
     * @return
     */
    Boolean updateMessage(Message message);
}
