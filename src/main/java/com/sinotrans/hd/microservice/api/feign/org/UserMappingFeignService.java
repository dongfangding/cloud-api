package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microService.util.SqlObject;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/userMapping")
public interface UserMappingFeignService {

    /**
     * 查询用户映射列表
     *
     * @param email     邮箱
     * @param sysCode   系统代码
     * @param page      页数
     * @param lines     条数
     * @return
     */
    @RequestMapping("/list")
    Page listUserMappint(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "sysCode") String sysCode,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines);

    /**
     * 找到指定的用户映射
     * @param email     邮箱
     * @param sysCode   系统代码
     * @return
     */
    @RequestMapping("/userMapping")
    Map<String, Object> userMapping(
            @RequestParam(value = "email") String email, @RequestParam("sysCode") String sysCode);
}
