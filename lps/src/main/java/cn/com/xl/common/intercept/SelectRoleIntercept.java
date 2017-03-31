package cn.com.xl.common.intercept;

import cn.com.xl.core.aop.AopContext;
import cn.com.xl.core.constant.ConstShiro;
import cn.com.xl.core.intercept.QueryInterceptor;
import cn.com.xl.core.shiro.ShiroKit;

public class SelectRoleIntercept extends QueryInterceptor {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "where id in (#{join(ids)})";
			ac.setCondition(condition);
			ac.getParam().put("ids", roles.split(","));
		}
	}

}
