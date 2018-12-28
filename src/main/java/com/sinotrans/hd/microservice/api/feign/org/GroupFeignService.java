package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author DDf on 2018/8/1
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/group")
public interface GroupFeignService {

    /**
     * 获得部门和部门所在公司列表
     *
     * @param groupKey 部门名称模糊匹配或部门代码全匹配
     * @param orgKey   公司代码全匹配或公司名称模糊匹配
     * @param page     当前页，
     * @param lines    每页显示条数
     * @return 返回部门和部门所在公司
     */
    @RequestMapping("/groups")
    Page<Object> queryGroups(
            @RequestParam(value = "groupKey", required = false) String groupKey,
            @RequestParam(value = "orgKey", required = false) String orgKey,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines,
            @RequestParam(value = "isLike", defaultValue = "1") boolean isLike);
}
