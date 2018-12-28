package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/org")
public interface OrgFeignService {
    /**
     * 获取公司信息
     *
     * @param key   查询条件
     * @param page  当前页
     * @param lines 每页显示条数
     * @param isLike 是否模糊查询
     * @return 返回公司列表数据
     */
    @RequestMapping("/orgs")
    Page<Object> queryOrgs(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines,
            @RequestParam(value = "isLike", required = false, defaultValue = "1") boolean isLike);

    /**
     * 获得组织架构中公司树结构，同时返回组织架构中公司下部门和用户信息
     *
     * @param orgKey   公司其余查询条件
     * @param groupKey 部门名称模糊匹配或部门代码全匹配
     * @param uid      用户UID，用来确定唯一一个用户
     * @param isLike   是否模糊匹配
     * @return 返回组织架构中公司下部门和用户信息
     */
    @RequestMapping("/treeOrgs")
    List<Map<String, Object>> treeOrgs(
            @RequestParam(value = "orgKey", required = false) String orgKey,
            @RequestParam(value = "groupKey", required = false) String groupKey,
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "isLike", required = false, defaultValue = "1") boolean isLike);
}
