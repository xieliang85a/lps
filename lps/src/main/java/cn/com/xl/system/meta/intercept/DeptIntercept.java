package cn.com.xl.system.meta.intercept;

import cn.com.xl.core.aop.AopContext;
import cn.com.xl.core.constant.ConstShiro;
import cn.com.xl.core.meta.PageIntercept;
import cn.com.xl.core.shiro.ShiroKit;
import cn.com.xl.core.toolbox.support.Convert;

public class DeptIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "and id in (#{join(ids)})";
			ac.setCondition(condition);
			ac.getParam().put("ids", Convert.toIntArray(depts));
		}
	}

}
