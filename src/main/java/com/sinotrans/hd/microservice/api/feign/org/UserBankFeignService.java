package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/resource")
public interface UserBankFeignService {

    /**
     * 查询个人银行卡
     *
     * @param uid       邮箱前缀
     * @param orgCode   公司代码
     * @param groupCode 部门代码
     * @return
     */
    @RequestMapping("/list")
    List<Map<String, Object>> list(
            @RequestParam(value = "uid", required = false) String uid,
            @RequestParam(value = "orgCode", required = false) String orgCode,
            @RequestParam(value = "groupCode", required = false) String groupCode);
}
