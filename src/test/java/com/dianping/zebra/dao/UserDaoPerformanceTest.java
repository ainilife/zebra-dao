package com.dianping.zebra.dao;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianping.zebra.dao.entity.UserEntity;
import com.dianping.zebra.dao.mapper.UserMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/spring/common/appcontext-*.xml")
public class UserDaoPerformanceTest {

	@Autowired
	private UserMapper dao;

	private FindUserCallback findUserCallback = new FindUserCallback();

	@Test
	public void test() throws InterruptedException, IOException {
		int[] ids = new int[] { 1, 2, 3, 4, 5, 6, 11 };

		for (int i = 0; i < 100000; i++) {
			try{
				dao.findUserById(ids[i % ids.length], findUserCallback);
			}catch(Exception e){
				System.out.println(e);
			}

			TimeUnit.MILLISECONDS.sleep(2);
		}

		System.in.read();
	}

	public static class FindUserCallback implements AsyncDaoCallback<UserEntity> {

		@Override
		public void onSuccess(UserEntity result) {
			if (result == null) {
				System.out.println("cannot find user");
			} else {
				System.out.println("find user " + result.getName());
			}
		}

		@Override
		public void onException(Exception e) {

		}
	}
}
