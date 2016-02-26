# zebra-dao

## 简介
`zebra-dao`是在`mybatis`基础上进一步封装的`异步数据源`,它是在要求服务全异步化的背景下产生的。目前，已有多个业务接入使用，并在线上环境接受了大并发的考验。
它有以下的功能点：

1. 实现`回调`和`Future`两种方式的异步dao
2. 在mybatis基础上支持分页功能
3. 使用方式和mybatis一致，有关mybati的使用问题请参考文档https://mybatis.github.io/mybatis-3/zh/

## 如何配置
配置pom，引入zebra-dao和其他zebra组件

	<dependency>
		<groupId>com.dianping.zebra</groupId>
		<artifactId>zebra-dao</artifactId>
		<version>0.1.5</version>
	</dependency>

	<!--Spring 相关依赖，请指定版本-->
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-context</artifactId>
		<version>${spring.version}</version>
	</dependency>
	<dependency>
		<groupId>org.springframework</groupId>
		<artifactId>spring-jdbc</artifactId>
		<version>${spring.version}</version>
	</dependency>

在spring的appcontext-dao.xml文件中配置一个如下的bean。因为zebra-dao背后异步的实现方式，是使用线程池执行的方式的，所以需要在这里设置线程池的大小。该部分配置与原生的mybatis有差异，也是唯一差异的地方。

	<bean class="com.dianping.zebra.dao.mybatis.ZebraMapperScannerConfigurer">
        <property name="basePackage" value="com.dianping.zebra.dao.mapper" />
        <!--可不配，默认值为20 -->
        <property name="initPoolSize" value="20"></property>
        <!--可不配，默认值为200-->
        <property name="maxPoolSize" value="200"></property>
        <!--可不配，默认值为500-->
        <property name="queueSize" value="500"></property>
    </bean>


在spring的appcontext-db.xml文件中配置一个如下的bean，该部分配置和原生的mybatis一致，如果不明白，请参考mybatis使用的相关文档。
		
	<bean id="datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource"          
        destroy-method="close">         
    	<property name="driverClass" value="com.mysql.jdbc.Driver"/>         
    	<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/zebra?characterEncoding=UTF8"/>         
    	<property name="user" value="admin"/>         
    	<property name="password" value="123456"/>
	</bean>   

	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!--指定数据源-->
		<property name="dataSource" ref="datasource"/>
		<!--指定Mapper文件地址，需要改写-->
		<property name="mapperLocations" value="classpath*:config/sqlmap/**/*.xml" />
		<!--指定entity的package,需要改写-->
		<property name="typeAliasesPackage" value="com.dianping.zebra.dao.entity" />
	</bean>

	<!--事务管理器，如不需要，可不定义-->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="datasource" />
	</bean>

    
## 如何使用

### 同步访问的使用方式
就是mybatis的使用方式，如不清楚，请参考mybatis的相关文档~

### 异步访问的Callback方式
1.举例来说，在UserMapper中，有一个同步方法`findUserById`,如果想要有异步的通过回调方式的接口，则可以增加一个相同方法名，并且参数列表中多一个`AsyncDaoCallback`的参数的方法。

	public interface UserMapper {
		/**
		*正常的Mapper Class的同步写法
		*/
		public UserEntity findUserById(@Param("userId") int userId);
		
		/**
		*Zebra-dao支持的异步回调写法，返回值为void，仅支持一个回调方法
		*/
		public void findUserById(@Param("userId") int userId, AsyncDaoCallback<UserEntity> callback);
	}

2.在业务代码中使用`UserMapper`时，需要在调用时实现自己的`AsyncDaoCallback`，例如：

	@Autowired
	private UserMapper dao;
	
	......
	
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


### 异步访问的Future方式
1.举例来说，在UserMapper中，有一个同步方法`getAll`,如果想要有异步的通过`Future`方式的接口，则可以增加一个方法，并使用`@TargetMethod`指定新方法需要对应到哪一个同步方法。

	public interface UserMapper {
		/**
 		*正常的Mapper Class的同步写法
 		*/
		public List<UserEntity> getAll();

		/**
 		*Zebra-dao支持的异步回调写法，返回值为Future。需要通过annotation指定原始的同步方法名是哪一个
 		*/
		@TargetMethod(name = "getAll")
		public Future<List<UserEntity>> getAll1();
	}

2.在业务代码中使用`UserMapper`时，需要通过Future接口获取到返回值，例如：

	@Autowired
	private UserMapper dao;
	
	......

	Future<List<UserEntity>> future = dao.getAll1();
	List<UserEntity> list = future.get();
	
	for(UserEntity user : list){
		System.out.println(user);
	}

## 分页功能
#### 逻辑分页
逻辑分页是指将数据库中的所有数据取出，然后通过Java代码控制分页。一般是通过JDBC协议中定位游标的位置进行操作的，使用`absolute`方法。`MyBatis`中原生也是通过这种方式进行分页的。下面举例说明：

HeartbeatMapper文件中mapper是这样定义的：

    <select id="getAll" parameterType="map" resultType="HeartbeatEntity">
       	SELECT * FROM heartbeat
    </select>

HeartbeatMapper Java文件是这么定义的，其中`RowBounds`中定义分页的`offset`和`limit`：

	List<HeartbeatEntity> getAll(RowBounds rb);
	
#### 物理分页
理分页指的是在SQL查询过程中实现分页，依托与不同的数据库厂商，实现也会不同。zebra-dao扩展了一个拦截器，实现了改写SQL达到了物理分页的功能。下面举例说明如何使用：

在Spring的配置中修改sqlSessionFactory，添加configLocation

	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="configLocation" value="classpath:config/mybatis/mybatis-configuration.xml" />
	</bean>

`mybatis-configuration.xml`文件如下，目前zebra-dao只实现了MySQLDialect。

	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
	<configuration>
		<plugins>
			<plugin interceptor="com.dianping.zebra.dao.plugin.page.PageInterceptor">
				<property name="dialectClass" value="com.dianping.zebra.dao.dialect.MySQLDialect"/>
			</plugin>
		</plugins>
	</configuration>

如此配置后，所有的分页查询都变成物理分页了。

#### 高级物理分页
zebra-dao支持同时获得总条数(totalRecord)和数据(records)。配完上述物理分页的配置后，在mapper中定义如下：

	void getAll(PageModel page);

注意这里没有任何返回值，返回的值在`PageModel`对象里面。一旦使用了PageModel的方式，必须是配置了物理分页，并且方法的返回值必须为`void`。

#### 分页功能的异步支持
使用RowBounds的方式，对于回调和Future都支持。
使用PageModel的方式，仅仅支持回调的方式，不支持Future的方式，这是因为该方式强制要求方法没有返回值。在回调方式使用中，有以下的注意点：

	dao.getAll(model, new AsyncDaoCallback<PageModel>() {
		@Override
		public void onSuccess(PageModel pageModel) {
			//回调传入的pageModel为null，请使用model，因为结果在model中
			System.out.println(model.getRecordCount());
			System.out.println(model.getRecords().size());
		}

		@Override
		public void onException(Exception e) {
		}
	});

## 常见问题
TODO
Q:如何Handle异常
















 
    
