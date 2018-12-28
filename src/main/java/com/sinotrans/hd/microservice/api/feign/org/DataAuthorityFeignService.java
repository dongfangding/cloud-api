package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/dataAuthority")
public interface DataAuthorityFeignService {

    /**
     * 数据权限查询
     *
     * @param UID
     * @param sysCode
     * @return
     */
    @RequestMapping("/query")
    List<Map<String, Object>> query(
            @RequestParam(value = "UID") String UID,
            @RequestParam(value = "sysCode", required = false) String sysCode);


    /**
     * 查询用户指定系统的数据权限, 权限明细放入权限主表的一个属性中
     *
     * @param uid     用户uid
     * @param sysCode 系统代码
     * @return 返回指定系统的数据权限
     */
    @RequestMapping("/queryUserDataAuthList/{uid}/{sysCode}")
    List<Map<String, Object>> queryUserDataAuthList(
            @PathVariable("uid") String uid, @PathVariable("sysCode") String sysCode);
}
