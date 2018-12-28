package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author DDf on 2018/11/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/resource")
public interface ResourceFeignService {

    /**
     * 查询用户菜单权限
     *
     * @param uid     用户唯一标识符
     * @param sysCode 系统代码
     * @return 返回用户默认菜单和角色菜单
     */
    @RequestMapping("/resources/{uid}/{sysCode}")
    List<Map<String, Object>> queryResources(
            @PathVariable(value = "uid") String uid, @PathVariable(value = "sysCode") String sysCode);
}
