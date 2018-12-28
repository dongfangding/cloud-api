package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/groupMapping")
public interface GroupMappingFeignService {
    /**
     * 分页显示外部系统公司映射列表
     * @param sysCode 外部系统代码
     * @param orgKey 公司代码
     * @param q 查询参数
     * @param page 当前页
     * @param lines 每页显示条数
     * @return
     */
    @RequestMapping("/pagedList")
    Page pagedOrgMapping(
            @RequestParam(value = "sysCode") String sysCode,
            @RequestParam(value = "orgKey", required = false) String orgKey,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines);


    /**
     * 根据组织架构的公司代码指定系统对应的映射公司数据
     *
     * @param orgCode   公司代码
     * @param sysCode   系统代码
     * @return
     */
    @RequestMapping("/groupMapping")
    Map<String, Object> orgMapping(
            @RequestParam(value = "sysCode") String sysCode, @RequestParam(value = "orgCode") String orgCode,
            @RequestParam(value = "groupCode") String groupCode);
}
