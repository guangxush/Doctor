package com.shgx.pingback.controller;

import com.shgx.common.model.ApiResponse;
import com.shgx.pingback.model.PingBack;
import com.shgx.pingback.service.PingBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: guangxush
 * @create: 2019/08/17
 */
@RestController
@Slf4j
public class PingBackController {
    @Autowired
    private PingBackService patientService;

    @RequestMapping(path = "/query/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<PingBack> query(@PathVariable("id") Long id) {
        if (id == null) {
            return new ApiResponse<PingBack>().fail(new PingBack());
        }
        PingBack pingBack = patientService.queryPingBack(id);
        return new ApiResponse<PingBack>().success(pingBack);
    }

    /**
     * 埋点接收数据
     * @param pingBack
     * @return
     */
    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> insert(@RequestBody PingBack pingBack) {
        Boolean result = patientService.savePingBack(pingBack);
        return new ApiResponse<Boolean>().success(result);
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Boolean> update(@RequestBody PingBack pingBack) {
        Boolean result = patientService.updatePingBack(pingBack);
        return new ApiResponse<Boolean>().success(result);
    }
}
