package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/config/")
public interface ConfigFeignService {

    /**
     * 查询配置表
     * @param page
     * @param lines
     * @param uid
     * @param sysCode
     * @param type
     * @return
     */
    @RequestMapping("/queryConfig")
    Page queryConfig(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "sysCode", required = false) String sysCode,
            @RequestParam(value = "type", required = false) String type);


    /**
     * 查询配置子表
     * @param configId
     * @return
     */
    @RequestMapping("/queryItem")
    List<Map<String, Object>> queryItem(@RequestParam(value = "configId") String configId);


    /**
     * 查询配置范围
     *
     * @param UID        用户U_ID
     * @param SYS_CODE   系统代码
     * @param ORG_CODE   公司代码
     * @param GROUP_CODE 部门代码
     * @return
     */
    @RequestMapping("/queryRangeConfig")
    List<Map<String, Object>> queryRangeConfig(
            @RequestParam(value = "UID") String UID,
            @RequestParam(value = "SYS_CODE", required = false) String SYS_CODE,
            @RequestParam(value = "ORG_CODE", required = false) String ORG_CODE,
            @RequestParam(value = "GROUP_CODE", required = false) String GROUP_CODE);
}
