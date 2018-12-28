package com.sinotrans.hd.microservice.api.feign.org;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author DDf on 2018/6/19
 */
@FeignClient(value = Constants.ORG_APPLICATION_NAME, path = "/HDOrg/user")
public interface UserFeignService {

    /**
     * 查询用户的基本组织架构信息和操作公司列表、菜单权限列表、数据权限列表
     *
     * @param uid     用户uid
     * @param sysCode 系统代码
     * @param imgFlag 是否需要用户头像
     * @return 返回用户的基本组织架构信息和操作公司列表、菜单权限列表、数据权限列表
     */
    @RequestMapping("/userPermissions/{uid}/{sysCode}")
    Map<String, Object> queryUserPermissionList(
            @PathVariable("uid") String uid, @PathVariable("sysCode") String sysCode,
            @RequestParam(value = "imgFlag", required = false) boolean imgFlag);

    /**
     * 查询用户列表以及用户在组织架构中的部门和公司
     *
     * @param orgKey   公司代码全匹配或公司名称模糊匹配
     * @param groupKey 部门名称模糊匹配或部门代码全匹配
     * @param userKey  匹配用户基本条件，userKey 关键字，目前支持 CODE, NAME, EMAIL
     * @param page     当前页，
     * @param lines    每页显示条数，
     * @return 返回用户列表以及用户在组织架构中的部门和公司
     */
    @RequestMapping("/users")
    Page<Object> queryUsers(
            @RequestParam(value = "orgKey", required = false) String orgKey,
            @RequestParam(value = "groupKey", required = false) String groupKey,
            @RequestParam(value = "userKey", required = false) String userKey,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines);


    /**
     * 不分页查询用户在组织架构中的部门和公司
     *
     * @param uid 必传字段，用户唯一识别码
     * @param key 关键字，目前支持 CODE, ID, NAME, EMAIL
     * @return 返回用户在公司架构中的用户基本信息和部门、公司信息
     */
    @RequestMapping(value = "/{uid:.+}")
    Map<String, Object> getUserByKey(
            @PathVariable("uid") String uid, @RequestParam(value = "key", required = false) String key);

    /**
     * 返回指定公司下的所有员工列表以及可以操作该公司的员工列表
     *
     * @param orgId     公司id
     * @param sysCode   系统代码
     * @param page      当前页
     * @param lines     每页显示条数
     * @param userKey   用户关键字， 目前支持用户名称，用户代码，用户u_id模糊查询
     * @return
     */
    @RequestMapping("/orgUserContactDelegate")
    Page<Object> queryOrgUserContactDelegate(
            @RequestParam(value = "orgId") Integer orgId, @RequestParam(value = "sysCode") String sysCode,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "lines", required = false) Integer lines,
            @RequestParam(value = "userKey", required = false) String userKey);

    /**
     * 查询指定用户所在部门的领导的用户的基本信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("userGroupLeaderUser/{uid:.+}")
    Map<String, Object> queryUserGroupLeaderUser(@PathVariable(value = "uid") String uid);
}
