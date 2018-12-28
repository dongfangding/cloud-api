package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.dataStructure.Tree;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/orgNexus")
public interface OrgNexusFeignService {

    /**
     * 根据公司代码创建公司层级结构
     *
     * @param orgKey  公司代码全匹配或公司名称模糊匹配
     * @param sysCode 系统代码，默认DEFAULT
     * @return 返回公司树形结构
     */
    @RequestMapping("/buildOrgNexus")
    List<Tree<Map<String, Object>>> buildOrgNexus(
            @RequestParam(value = "orgKey", required = false) String orgKey,
            @RequestParam(value = "sysCode", required = false) String sysCode);
}
