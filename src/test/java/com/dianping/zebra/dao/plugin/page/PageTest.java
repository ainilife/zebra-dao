package com.dianping.zebra.dao.plugin.page;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianping.zebra.dao.AsyncDaoCallback;
import com.dianping.zebra.dao.entity.HeartbeatEntity;
import com.dianping.zebra.dao.mapper.HeartbeatMapper;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/spring/common/appcontext-*.xml")
public class PageTest {

	@Autowired
	private HeartbeatMapper dao;

	@Test
	public void testPhysicalPageWithPaginate() throws IOException {
		PageModel paginate = new PageModel(2, 100);
		dao.getAll(paginate);

		System.out.println(paginate.getRecordCount());
		Assert.assertEquals(100, paginate.getRecords().size());
		System.in.read();
	}

	@Test
	public void testPhysicalPageWithBound() throws IOException {
		List<HeartbeatEntity> rows = dao.getPage(new RowBounds(0, 100));

		System.out.println(rows.size());
		System.in.read();
	}

	@Test
	public void testPhysicalPageWithBound2() {
		PageModel paginate = new PageModel(1, 100);

		dao.getAllExcludeAppName("taurus-agent", paginate);

		System.out.println(paginate.getRecordCount());
		Assert.assertEquals(100, paginate.getRecords().size());
	}

	@Test
	public void testSelectBoundAsync() throws IOException {
		dao.getPage(new RowBounds(0, 100), new AsyncDaoCallback<List<HeartbeatEntity>>() {

			@Override
			public void onSuccess(List<HeartbeatEntity> result) {
				System.out.println(result.size());
			}

			@Override
			public void onException(Exception e) {
			}
		});
		System.in.read();
	}

	@Test
	public void testSelectBoundFuture() throws IOException, InterruptedException, ExecutionException {
		Future<List<HeartbeatEntity>> page1 = dao.getPage1(new RowBounds(0, 100));

		List<HeartbeatEntity> list = page1.get();
		
		System.out.println(list.size());
		System.in.read();
	}

	@Test
	public void testSelectPageModelAsync() throws IOException {
		final PageModel model = new PageModel(1, 100);
		dao.getAll(model, new AsyncDaoCallback<PageModel>() {

			@Override
			public void onSuccess(PageModel pageModel) {
				System.out.println(model.getRecordCount());
				System.out.println(model.getRecords().size());
			}

			@Override
			public void onException(Exception e) {
			}
		});

		System.in.read();
	}
}
