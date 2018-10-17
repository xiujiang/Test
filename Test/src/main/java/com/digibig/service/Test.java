package com.digibig.service;

import com.digibig.service.service.Service1;
import com.digibig.service.service.Service2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * “<T>"和"<?>"，首先要区分开两种不同的场景：
 *
 *
 * 第一，声明一个泛型类或泛型方法。
 * 第二，使用泛型类或泛型方法。
 * 类型参数“<T>”主要用于第一种，声明泛型类或泛型方法。
 * 无界通配符“<?>”主要用于第二种，使用泛型类或泛型方法
 */
public class Test{

	public <A extends Serializable>void getClass1(A t){
		Collection<?> s ;
		List<A> list = new ArrayList<A>();      //只能是A类型的, 并且A是实现了Serializable和dao,继承和实现， 都是extends
		List<?> list2 = new ArrayList<String>();      //list2可以是任意类型

		Class<?> aclass = Service1.class;      //  可以变成service1的class ,也可以变成service2的class
		aclass = Service2.class;

		Class<? super A> bClass;
		//如果方法参数中使用了泛型，则在方法返回类型前加<T>
		//通配符? 可以指定多个类型，T只能指定一种类型
		//T和? 使用地方不同，T 使用在类或者方法上，?使用在引用变量上
		//T 只能extentds 而?可以extends 和super
		//T在方法中使用，只能代替一种类型，而？可以代替多种
	}
}