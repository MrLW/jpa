# jpa
JPA 是 hibernate 的一个抽象（就像JDBC和JDBC驱动的关系）

JPA 本质上就是一种  ORM 规范，不是ORM 框架 —— 因为 JPA 并未提供 ORM 实现，它只是制订了一些规范，提供了一些编程的 API 接口，但具体实现则由 ORM 厂商提供实现


## 快速入门

**1、persistence.xml配置数据库相关配置**

```

	<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" 
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
				xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
		<persistence-unit name="jpa-sample01" transaction-type="RESOURCE_LOCAL">
		
			<!-- 配置什么产品作为	jpa的实现 
				如果只有一个jpa的实现，则可以省略
			-->
			<provider>org.hibernate.ejb.HibernatePersistence</provider>
			<!-- 添加持久化类 -->
			<class>com.lw.jpa.helloword.Customer</class>
			<properties>
				<property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
				<property name="javax.persistence.jdbc.url" value="jdbc:mysql:///jpa"/>
				<property name="javax.persistence.jdbc.user" value="root"/>
				<property name="javax.persistence.jdbc.password" value="root"/>
				<!-- 配置jpa实现产品的基本属性 -->
				<property name="hibernate.format_sql" value="true"/>
				<property name="hibernate.show_sql" value="true"/>
				<property name="hibernate.hbm2ddl.auto" value="update"/>
			</properties>
			</persistence-unit>
	</persistence>
```

**2、创建实体类,并使用注解配置到数据库中的表**
```
	
	@Table(name="JPA_CUSTOMERS") // 不写默认为Customer
	@Entity
	public class Customer {

	private Integer id; 
	private String lastName; 
	private String email;
	private int age ;
	
	private Date createdTime; 
	private Date birth ;
	
	// 最好需要加在get方法里面
	@Column(name="ID") // 主键
	@GeneratedValue(strategy=GenerationType.AUTO) //生成主键的策略
	//	@TableGenerator(name="ID_GENERATOR",
	//					table="jpa_id_generators",
	//					pkColumnName="PK_NAME",
	//					pkColumnValue="CUSTOMER_ID",
	//					valueColumnName="PK_VALUE",
	//					allocationSize=100) // 每次添加100个
	//	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")
		@Id
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		@Column(name="LAST_NAME",length=50) // 还可以对数据列的长度限制
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		// 默认添加了@Base注解,添加了一样
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Temporal(TemporalType.TIMESTAMP) // 限制时间类型的格式
		public Date getCreatedTime() {
			return createdTime;
		}
		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}
		@Temporal(TemporalType.DATE) // 限制时间类型的格式
		public Date getBirth() {
			return birth;
		}
		public void setBirth(Date birth) {
			this.birth = birth;
		}
		//工具方法，不需要映射为数据表的一列
		//没有set方法，运行会报错
		@Transient
		public String getInfo(){
			return "lastName:" + lastName + ";email:" + email ;
		}
		@Override
		public String toString() {
			return "Customer [id=" + id + ", lastName=" + lastName + ", email=" + email + ", age=" + age + ", createdTime="
					+ createdTime + ", birth=" + birth + "]";
		}
	
	}
	
```

**3、使用EntityManager完成数据的curd**
```

	@Before
	public void init(){
		//jpa-sample01:在persistence.xml中persistence-unit的name属性
		entityManagerFactory = 			Persistence.createEntityManagerFactory("jpa-sample01");
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}
	// 相当于session的save方法
	// 注意：jpa的persist方法保存的对象不能设置id，否则报错,也就是说只能保存游离对象,不能保存持久化对象
	// 但是hibernate可以保存有id的,可以保存持久化对象
	@Test
	public void testPersistence(){
		Customer customer = new Customer() ;
		customer.setAge(11);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("bb@qq.com");
		customer.setLastName("bb");
		entityManager.persist(customer);
		System.out.println(customer.getId());
	}
	
	
	// 相当于session的delete方法
	// 但是jpa的remove只能移除持久化对象，而session的delete还可以移除游离状态对象
	@Test
	public void testRemove(){
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	}
	
	// 相当于session的load方法
	@Test
	public void testGetReference(){
		Customer customer = entityManager.getReference(Customer.class, 1) ;
		//System.out.println(customer.getClass().getName());
		System.out.println("-------------------");
		System.out.println(customer); // 只有在用到customer的时候才会从数据库中查询
	}
	
	// 相当于 session的get方法
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("--------------");
		System.out.println(customer);
	}
	
	@After
	public void destroy(){
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
	
	
```


```

	//Merge方法,类似session的saveOrUpdate方法
	// 若传入的是一个临时对象，会创建一个新的对象，把临时对象的属性复制到新的对象中，然后进行持久化操作，
	// 所以新对象和之前的对象不是同一个对象。
	@Test
	public void testMerge1(){
		Customer customer = new Customer();
		customer.setAge(33);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("cc@qq.com");
		customer.setLastName("cc");
		
		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer:" + customer.getId());
		System.out.println("customer2:" + customer2.getId());
	}
	
	// 若传入的是一个游离的对象，即传入的对象有Id	   *******
	//1、在EntityManager缓存中没有该对象
	//2、若在数据库中也咩有对应记录
	//3、jpa会创建一个新的对象，然后把当前游离对象的属性复制到新创建的对象中
	//4、对新创建的对象执行insert操作
	@Test
	public void testMerge2(){
		Customer customer = new Customer() ;
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");
		
		customer.setId(100);// 注意：这个id并不会存放到数据库中
		
		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer:" + customer);
		System.out.println("customer2:" + customer2);
		
	}
	
	
	// 若传入的是一个游离对象, 即传入的对象有 OID
	//1. 若在 EntityManager 缓存中没有该对象
	//2. 若在数据库中也有对应的记录 				*******
	//3. JPA 会查询对应的记录, 然后返回该记录对一个的对象, 再然后会把游离对象的属性复制到查询到的对象中.
	//4. 对查询到的对象执行 update 操作. 
	@Test
	public void testMerge3(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ee@163.com");
		customer.setLastName("EE");
		
		customer.setId(4);
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println(customer == customer2);
	}
	
	
	//若传入的是一个游离对象, 即传入的对象有 OID. 
	//1. 若在 EntityManager 缓存中有对应的对象
	//2. JPA 会把游离对象的属性复制到查询到EntityManager 缓存中的对象中.
	//3. EntityManager 缓存中的对象执行 UPDATE. 
	@Test
	public void testMerge4(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ccc@qq.com");
		customer.setLastName("ccc");
		
		customer.setId(4);
		
		// 和3类似，这里手工查询了一下，而3是使用meger查询
		// 这里先查询下，再到缓存中修改
		Customer customer2 = entityManager.find(Customer.class, 4); // 会有一个复制操作
		
		entityManager.merge(customer); // 这里相当于entityManager和两个id相同的对象关联
		
		System.out.println(customer == customer2); //false
	}

```

## 常用注解

- @Entity:添加到一个实体上,表明该实体对应于数据库中的一张表,没有没有添加@Table注解,则默认表名就是该实体的类名
- @Table：可以指定该实体对应到数据库表的表明
- @Column：作用到属性的get方法上,指定表的列名.
- @GeneratedValue:指定主键的生成策略
- @Base：默认的注解
- @Transient：希望该类的某个方法不要映射数据表的一部分
- @Temporal：指定日期的对应数据库的格式

**使用Table生成主键**
```
	
	//@TableGenerator:申明一个主键生成器
	@TableGenerator(name="ID_GENERATOR",                                     
					table="jpa_id_generators", // 生成策略持久化的表名                             
					pkColumnName="PK_NAME", // 指定持久化表中      主键生成策略对应键的名称              					  和pkColumnValue确定一行             
					pkColumnValue="CUSTOMER_ID",  //  pkColumnValue 属性的值表示在持久化表中策略所对应的主键           和pkColumnName确定一行
					valueColumnName="PK_VALUE", 表示在持久化表中，该主键当前所生成的值，它的值将会随着每次创建累加	    确定列
					allocationSize=100) // 每次添加100个           
	// strategy=GenerationType.TABLE:表明主键生成策略使用Table
	// generator:指定Table生成器             
	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")  
```

