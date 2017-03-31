package cn.com.xl.common.intercept;

import cn.com.xl.core.intercept.SelectInterceptor;
import cn.com.xl.core.meta.IQuery;

public class DefaultSelectFactory extends SelectInterceptor {
	
	public IQuery deptIntercept() {
		return new SelectDeptIntercept();
	}
	
	public IQuery roleIntercept() {
		return new SelectRoleIntercept();
	}
	
}
