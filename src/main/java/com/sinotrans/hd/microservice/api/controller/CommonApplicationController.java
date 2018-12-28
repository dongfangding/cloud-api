package com.sinotrans.hd.microservice.api.controller;

import com.sinotrans.hd.common.http.Page;
import com.sinotrans.hd.microservice.api.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author DDf on 2018/5/29
 */
@RestController
@RequestMapping("/apiCommon")
public class CommonApplicationController {
	@Autowired
	private RestTemplate restTemplate;


	/**
	 * 有一些通用的列表查询，查询的表不同，可能所在的服务也不同，但每个服务都有相同
	 * 的通用方法，为了方便调用指定服务下的通用方法查询某张表的列表数据，对原通用方法进行
	 * 稍微修改，使前端可以直接调用指定服务名下的通用列表查询方法
	 *
	 * @param applicationName 服务名
	 * @param JName           要查询的表名
	 * @param page            页数
	 * @param lines           条数
	 * @param httpHeaders     header
	 * @param whereColumns    列
	 * @param whereOprators   操作符
	 * @param whereValues     属性值
	 * @param whereRelations  两个属性之间的关系符
	 * @return
	 */
	@RequestMapping("/pagedList")
	public Page pagedList(
			@RequestParam String applicationName, @RequestParam String JName,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer lines,
			@RequestHeader HttpHeaders httpHeaders,
			@RequestParam(value = "WHERE_COLUMNS", required = false) String whereColumns,
			@RequestParam(value = "WHERE_OPRATORS", required = false) String whereOprators,
			@RequestParam(value = "WHERE_VALUES", required = false) String whereValues,
			@RequestParam(value = "WHERE_RELATIONS", required = false) String whereRelations) {
		String url = "http://" + applicationName + "/GetPagedJsonByName?JName=" + JName;
		if (page != null) {
			url += "&page=" + page;
		}
		if (lines != null) {
			url += "&lines=" + lines;
		}
		if (Constants.isNotNull(whereColumns)) {
			url += "&WHERE_COLUMNS=" + whereColumns;
		}
		if (Constants.isNotNull(whereOprators)) {
			url += "&WHERE_OPRATORS=" + whereOprators;
		}
		if (Constants.isNotNull(whereValues)) {
			url += "&WHERE_VALUES=" + whereValues;
		}
		if (Constants.isNotNull(whereRelations)) {
			url += "&WHERE_RELATIONS=" + whereRelations;
		}
		return restTemplate.postForObject(url, httpHeaders, Page.class);
	}
}
