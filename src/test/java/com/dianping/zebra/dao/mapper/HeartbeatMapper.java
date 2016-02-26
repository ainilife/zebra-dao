package com.dianping.zebra.dao.mapper;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.dianping.zebra.dao.AsyncDaoCallback;
import com.dianping.zebra.dao.annotation.TargetMethod;
import com.dianping.zebra.dao.entity.HeartbeatEntity;
import com.dianping.zebra.dao.plugin.page.PageModel;

public interface HeartbeatMapper {

	// RowBound
	List<HeartbeatEntity> getPage(RowBounds rb);

	@TargetMethod(name = "getPage")
	Future<List<HeartbeatEntity>> getPage1(RowBounds rb);

	List<HeartbeatEntity> getPage(RowBounds rb, AsyncDaoCallback<List<HeartbeatEntity>> callback);

	// PageModel
	// PageModel dese not support Future
	void getAll(PageModel page);

	void getAll(PageModel page, AsyncDaoCallback<PageModel> callback);

	void getAllExcludeAppName(@Param("appName") String appName, PageModel page);
}
