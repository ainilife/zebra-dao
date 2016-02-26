package com.dianping.zebra.dao;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianping.zebra.dao.entity.UserEntity;
import com.dianping.zebra.dao.mapper.UserMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:config/spring/common/appcontext-*.xml")
public class UserDaoTest {

	@Autowired
	private UserMapper dao;

	@Test
	public void testSelect() {
		UserEntity user = dao.findUserById(1);

		System.out.println(user);
	}

	@Test
	public void testInsert() {
		UserEntity user = new UserEntity();

		user.setTel("123412412");
		user.setName("peter");
		user.setRole(1);
		user.setEmail("hao.zhu@dianping.com");
		user.setAlias("hao.zhu");
		
		int insertUser = dao.insertUser(user);
		System.out.println(insertUser);
	}
	
	@Test
	public void testInsertAsync() throws IOException {
		UserEntity user = new UserEntity();

		user.setTel("123412412");
		user.setName("peter");
		user.setRole(1);
		user.setEmail("hao.zhu@dianping.com");
		user.setAlias("hao.zhu");
		
		System.out.println("before: " + System.currentTimeMillis());

		dao.insertUser1(user,new AsyncDaoCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer result) {
				System.out.println(result);
			}

			@Override
         public void onException(Exception e) {
         }
		});
		
		System.out.println("after: " + System.currentTimeMillis());
		
		System.in.read();
	}
	
	@Test
	public void testInsertFuture() throws IOException, InterruptedException, ExecutionException {
		UserEntity user = new UserEntity();

		user.setTel("123412412");
		user.setName("peter");
		user.setRole(1);
		user.setEmail("hao.zhu@dianping.com");
		user.setAlias("hao.zhu");

		System.out.println("before: " + System.currentTimeMillis());

		Future<Integer> future = dao.insertUser2(user);
		
		System.out.println("after: " + System.currentTimeMillis());
		
		Integer integer = future.get();
		System.out.println(integer);
		
		System.in.read();
	}
	
	@Test
	public void testAsync() throws IOException {
		System.out.println("before " + System.currentTimeMillis());

		dao.findUserById(1, new AsyncDaoCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity user) {
				System.out.println("current " + System.currentTimeMillis());
				System.out.println(user);
				
				dao.findUserById(2, new AsyncDaoCallback<UserEntity>() {
					
					@Override
					public void onSuccess(UserEntity user) {
						System.out.println("current " + System.currentTimeMillis());
						System.out.println(user);
					}

					@Override
               public void onException(Exception e) {
               }
				});
				
				UserEntity entity = dao.findUserById(3);
				System.out.println(entity);
			}

			@Override
         public void onException(Exception e) {
         }
		});
		
		System.out.println("after " + System.currentTimeMillis());
		
		System.in.read();
	}
	
	@Test
	public void testFuture() throws IOException, InterruptedException, ExecutionException {
		System.out.println("before " + System.currentTimeMillis());
		Future<List<UserEntity>> future = dao.getAll1();
		System.out.println("after " + System.currentTimeMillis());

		List<UserEntity> list = future.get();
		
		for(UserEntity user : list){
			System.out.println(user);
		}
		
		System.in.read();
	}
}
