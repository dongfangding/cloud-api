package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/dataAuthorityItem")
public interface DataAuthorityItemFeignService {

    /**
     * 数据权限明细查询
     *
     * @param dataAuthId 数据权限主表id
     * @param sysCode    系统代码
     */
    @RequestMapping("/query")
    List<Map<String, Object>> query(
            @RequestParam(value = "DATA_AUTH_ID") String dataAuthId,
            @RequestParam(value = "sysCode", required = false) String sysCode);
}
