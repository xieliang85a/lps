/**
 * Copyright (c) 2015-2017, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.xl.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.xl.common.base.BaseController;
import cn.com.xl.common.tool.SysCache;
import cn.com.xl.core.annotation.Before;
import cn.com.xl.core.annotation.Json;
import cn.com.xl.core.annotation.Permission;
import cn.com.xl.core.aop.AopContext;
import cn.com.xl.core.constant.ConstShiro;
import cn.com.xl.core.meta.IQuery;
import cn.com.xl.core.plugins.dao.Blade;
import cn.com.xl.core.plugins.dao.Db;
import cn.com.xl.core.shiro.ShiroKit;
import cn.com.xl.core.toolbox.CMap;
import cn.com.xl.core.toolbox.Func;
import cn.com.xl.core.toolbox.ajax.AjaxResult;
import cn.com.xl.core.toolbox.cache.CacheKit;
import cn.com.xl.core.toolbox.cache.ILoader;
import cn.com.xl.core.toolbox.kit.CollectionKit;
import cn.com.xl.core.toolbox.kit.StrKit;
import cn.com.xl.core.toolbox.support.Convert;
import cn.com.xl.system.meta.intercept.PasswordValidator;
import cn.com.xl.system.meta.intercept.UserIntercept;
import cn.com.xl.system.meta.intercept.UserValidator;
import cn.com.xl.system.model.RoleExt;
import cn.com.xl.system.model.User;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController implements ConstShiro{
	private static String LIST_SOURCE = "user.list";
	private static String BASE_PATH = "/system/user/";
	private static String CODE = "user";
	private static String PREFIX = "blade_user";

	@RequestMapping("/")
	@Permission({ ADMINISTRATOR, ADMIN })
	public String index(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "user.html";
	}
	
	/**
	 * 分页aop
	 * 普通用法
	 */
	@Json
	@RequestMapping(KEY_LIST)
	@Permission({ ADMINISTRATOR, ADMIN })
	public Object list() {
		Object gird = paginate(LIST_SOURCE, new UserIntercept());
		return gird;
	}
	
	@RequestMapping(KEY_ADD)
	@Permission({ ADMINISTRATOR, ADMIN })
	public String add(ModelMap mm) {
		mm.put("code", CODE);
		return BASE_PATH + "user_add.html";
	}
	
	@RequestMapping(KEY_EDIT + "/{id}")
	@Permission({ ADMINISTRATOR, ADMIN })
	public String edit(@PathVariable Integer id, ModelMap mm) {
		User user = Blade.create(User.class).findById(id);
		CMap cmap = CMap.parse(user);
		cmap.set("roleName", SysCache.getRoleName(user.getRoleid()));
		mm.put("user", cmap);
		mm.put("code", CODE);
		return BASE_PATH + "user_edit.html";
	}
	
	@RequestMapping("/editMySelf/{id}")
	public String editMySelf(@PathVariable Integer id, ModelMap mm) {
		User user = Blade.create(User.class).findById(id);
		CMap cmap = CMap.parse(user);
		cmap.set("roleName", SysCache.getRoleName(user.getRoleid()));
		mm.put("user", cmap);
		mm.put("code", CODE);
		mm.put("methodName", "editMySelf");
		return BASE_PATH + "user_edit.html";
	}
	
	@RequestMapping("/editPassword/{id}")
	public String editPassword(@PathVariable Integer id, ModelMap mm){
		User user = Blade.create(User.class).findById(id);
		mm.put("user", user);
		mm.put("code", CODE);
		return BASE_PATH + "user_edit_password.html";
	}

	@Json
	@Before(PasswordValidator.class)
	@RequestMapping("/updatePassword")
	public AjaxResult updatePassword(){
		Blade blade = Blade.create(User.class);
		String userId = getParameter("user.id");
		String password = getParameter("user.newPassword");
		User user = blade.findById(userId);
		String salt = user.getSalt();
		user.setPassword(ShiroKit.md5(password, salt));
		user.setVersion(user.getVersion() + 1);
		boolean temp = blade.update(user);
		if (temp) {
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@RequestMapping(KEY_VIEW + "/{id}")
	@Permission({ ADMINISTRATOR, ADMIN })
	public String view(@PathVariable Integer id, ModelMap mm) {
		User user = Blade.create(User.class).findById(id);
		CMap cmap = CMap.parse(user);
		cmap.set("deptName", SysCache.getDeptName(user.getDeptid()))
			.set("roleName", SysCache.getRoleName(user.getRoleid()))
			.set("sexName", SysCache.getDictName(101, user.getSex()));
		mm.put("user", cmap);
		mm.put("code", CODE);
		return BASE_PATH + "user_view.html";
	}
	
	
	@Json
	@Before(UserValidator.class)
	@RequestMapping(KEY_SAVE)
	@Permission({ ADMINISTRATOR, ADMIN })
	public AjaxResult save() {
		User user = mapping(PREFIX, User.class);
		String pwd = user.getPassword();
		String salt = ShiroKit.getRandomSalt(5);
		String pwdMd5 = ShiroKit.md5(pwd, salt);
		user.setPassword(pwdMd5);
		user.setSalt(salt);
		user.setStatus(3);
		user.setCreatetime(new Date());
		boolean temp = Blade.create(User.class).save(user);
		if (temp) {
			CacheKit.removeAll(SYS_CACHE);
			return success(SAVE_SUCCESS_MSG);
		} else {
			return error(SAVE_FAIL_MSG);
		}
	}
	
	@Json
	@Before(UserValidator.class)
	@RequestMapping(KEY_UPDATE)
	public AjaxResult update() {
		User user = mapping(PREFIX, User.class);
		if(StrKit.notBlank(PREFIX + "PASSWORD")){
			String pwd = user.getPassword();
			User oldUser = Blade.create(User.class).findById(user.getId());
			if(!pwd.equals(oldUser.getPassword())){
				String salt = oldUser.getSalt();
				String pwdMd5 = ShiroKit.md5(pwd, salt);
				user.setPassword(pwdMd5);
			}
		}
		boolean temp = Blade.create(User.class).update(user);
		if (temp) {
			CacheKit.removeAll(SYS_CACHE);
			return success(UPDATE_SUCCESS_MSG);
		} else {
			return error(UPDATE_FAIL_MSG);
		}
	}

	@Json
	@RequestMapping(KEY_DEL)
	@Permission({ ADMINISTRATOR, ADMIN })
	public AjaxResult del() {
		boolean temp = Blade.create(User.class).updateBy("status = #{status}", "id in (#{join(ids)})", CMap.init().set("status", 5).set("ids", Convert.toIntArray(getParameter("ids"))));
		if (temp) {
			return success(DEL_SUCCESS_MSG);
		} else {
			return error(DEL_FAIL_MSG);
		}
	}
	
	@Json
	@RequestMapping("/reset")
	@Permission({ ADMINISTRATOR, ADMIN })
	public AjaxResult reset() {
		String ids = getParameter("ids");
		Blade blade = Blade.create(User.class);
		Integer[] idArr = Convert.toIntArray(ids);
		int cnt = 0;
		for(Integer id : idArr){
			User user = blade.findById(id);
			String pwd = "111111";
			String salt = user.getSalt();
			String pwdMd5 = ShiroKit.md5(pwd, salt);
			user.setVersion(((user.getVersion() == null) ? 0 : user.getVersion()) + 1);
			user.setPassword(pwdMd5);
			boolean temp = blade.update(user);
			if(temp){
				cnt++;
			}
		}
		if (cnt == idArr.length) {
			return success("重置密码成功");
		} else {
			return error("重置密码失败");
		}
	}
	
	@Json
	@RequestMapping("/auditOk")
	public AjaxResult auditOk() {
		String ids = getParameter("ids");
		Blade blade = Blade.create(User.class);
		CMap countMap = CMap.init().set("ids", Convert.toIntArray(ids));
		int cnt = blade.count("id in (#{join(ids)}) and (roleId='' or roleId is null)", countMap);
		if (cnt > 0) {
			return warn("存在没有分配角色的账号!");
		}
		CMap updateMap = CMap.init().set("status", 1).set("ids", Convert.toIntArray(ids));
		boolean temp = blade.updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			return success("审核成功!");
		} else {
			return error("审核失败!");
		}
	}
	
	@Json
	@RequestMapping("/auditRefuse")
	public AjaxResult auditRefuse() {
		String ids = getParameter("ids");
		CMap updateMap = CMap.init().set("status", 4).set("ids", Convert.toIntArray(ids));
		boolean temp = Blade.create(User.class).updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			return success("审核拒绝成功!");
		} else {
			return error("审核拒绝失败!");
		}
	}
	
	@Json
	@RequestMapping("/ban")
	public AjaxResult ban() {
		String ids = getParameter("ids");
		CMap updateMap = CMap.init().set("ids", Convert.toIntArray(ids));
		boolean temp = Blade.create(User.class).updateBy("status = (CASE WHEN STATUS=2 THEN 3 ELSE 2 END)", "id in (#{join(ids)})", updateMap);
		if (temp) {
			return success("操作成功!");
		} else {
			return error("操作失败!");
		}
	}
	
	@Json
	@RequestMapping("/restore")
	public AjaxResult restore() {
		String ids = getParameter("ids");
		CMap updateMap = CMap.init().set("status", 3).set("ids", Convert.toIntArray(ids));
		boolean temp = Blade.create(User.class).updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			return success("还原成功!");
		} else {
			return error("还原失败!");
		}
	}
	
	@Json
	@RequestMapping(KEY_REMOVE)
	public AjaxResult remove() {
		String ids = getParameter("ids");
		boolean temp = Blade.create(User.class).deleteByIds(ids) > 0;
		if (temp) {
			CacheKit.removeAll(SYS_CACHE);
			return success("删除成功!");
		} else {
			return error("删除失败!");
		}
	}
	
	@RequestMapping("/extrole/{id}/{roleName}")
	public String extrole(@PathVariable Integer id, @PathVariable String roleName, ModelMap mm) {
		User user = Blade.create(User.class).findById(id);
		String roleId = user.getRoleid();
		mm.put("userId", id);
		mm.put("roleId", roleId);
		mm.put("roleName", Func.decodeUrl(roleName));
		return BASE_PATH + "user_extrole.html";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Json
	@RequestMapping("/menuTreeIn")
	public AjaxResult menuTreeIn() {
		Integer userId = getParameterToInt("userId");
		Map<String, Object> roleIn = Db.selectOne("select ROLEIN from blade_role_ext where userId = #{userId}", CMap.init().set("userId",userId));
		List<Integer> ids = Db.queryListInt("select MENUID from blade_relation where ROLEID in (#{join(roles)})", CMap.init().set("roles", ShiroKit.getUser().getRoleList()));
		String inId = "0";
		if (!Func.isEmpty(roleIn)) {
			inId = Func.toStr(roleIn.get("ROLEIN"));
		}
		Object[] all = CollectionKit.addAll(ids.toArray(), Convert.toIntArray(inId));
		StringBuilder sb = Func.builder(
				"select m.id \"id\",(select id from blade_menu  where code=m.pCode) \"pId\",name \"name\",(case when m.levels=1 then 'true' else 'false' end) \"open\",(case when r.id is not null then 'true' else 'false' end) \"checked\"",
				" from blade_menu m",
				" left join (select id from blade_menu where id in (#{join(inId)})) r",
				" on m.id=r.id",
				" where m.status=1 and m.id in (#{join(all)}) order by m.levels,m.num asc"
				);
		
		List<Map> menu = Db.selectList(sb.toString(), CMap.init().set("inId", Convert.toIntArray(inId)).set("all", all));
		return json(menu);
	}
	
	@SuppressWarnings("rawtypes")
	@Json
	@RequestMapping("/menuTreeOut")
	public AjaxResult menuTreeOut() {
		Integer userId = getParameterToInt("userId");
		Map roleOut = Db.selectOne("select ROLEOUT from blade_role_ext where userId = #{userId}", CMap.init().set("userId",userId));
		List<Integer> ids = Db.queryListInt("select MENUID from blade_relation where ROLEID in (#{join(roles)})", CMap.init().set("roles", ShiroKit.getUser().getRoleList()));
		String outId = "0";
		if (!Func.isEmpty(roleOut)) {
			outId = Func.toStr(roleOut.get("ROLEOUT"));
		}
		Object[] all = CollectionKit.addAll(ids.toArray(), Convert.toIntArray(outId));
		StringBuilder sb = Func.builder(
				"select m.id \"id\",(select id from blade_menu  where code=m.pCode) \"pId\",name \"name\",(case when m.levels=1 then 'true' else 'false' end) \"open\",(case when r.id is not null then 'true' else 'false' end) \"checked\"",
				" from blade_menu m",
				" left join (select id from blade_menu where id in (#{join(outId)})) r",
				" on m.id=r.id",
				" where m.status=1 and m.id in (#{join(all)}) order by m.levels,m.num asc"
				);
		
		List<Map> menu = Db.selectList(sb.toString(), CMap.init().set("outId", Convert.toIntArray(outId)).set("all", all));
		return json(menu);
	}
	
	@Json
	@RequestMapping("/saveRoleExt")
	public AjaxResult saveRoleExt() {
		Blade blade = Blade.create(RoleExt.class);
		Integer userId = getParameterToInt("userId");
		String roleIn = getParameter("idsIn", "0");
		String roleOut = getParameter("idsOut", "0");
		RoleExt roleExt = blade.findFirstBy("userId = #{userId}", CMap.init().set("userId", userId));	
		boolean flag = false;
		if (Func.isEmpty(roleExt)) {
			roleExt = new RoleExt();
			flag = true;
		}
		roleExt.setUserid(userId);  
		roleExt.setRolein((StrKit.equals(roleIn, "")) ? "0" : roleIn); 
		roleExt.setRoleout((StrKit.equals(roleOut, "")) ? "0" : roleOut); 
		if (flag) {
			blade.save(roleExt);
		} else {
			blade.update(roleExt);
		}
		CacheKit.removeAll(SYS_CACHE);
		return success("配置成功!"); 
	}
	
	@RequestMapping("/roleAssign/{id}/{name}/{roleId}")
	public String roleAssign(@PathVariable String id, @PathVariable String name, @PathVariable String roleId, ModelMap mm) {
		mm.put("id", id);
		mm.put("roleId", roleId);
		mm.put("name", Func.decodeUrl(name));
		return BASE_PATH + "user_roleassign.html";
	}
	
	@Json
	@RequestMapping("/saveRole")
	public AjaxResult saveRole() {
		String id = getParameter("id");
		String roleIds = getParameter("roleIds");
		CMap cmap = CMap.init();
		cmap.set("roleIds", roleIds).set("id", Convert.toIntArray(id));
		boolean temp = Blade.create(User.class).updateBy("ROLEID = #{roleIds}", "id in (#{join(id)})", cmap);
		if (temp) {
			CacheKit.removeAll(SYS_CACHE);
			return success("配置成功!");
		} else {
			return error("配置失败!");
		}
	}
	
	@Json
	@RequestMapping("/userTreeList")
	public AjaxResult userTreeList() {
		List<Map<String, Object>> dept = CacheKit.get(SYS_CACHE, USER_TREE_ALL,
				new ILoader() {
					public Object load() {
						return Db.selectList("select id \"id\",pId \"pId\",simpleName as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  BLADE_DEPT order by pId,num asc", CMap.init(), new AopContext(), new IQuery() {
							
							@Override
							public void queryBefore(AopContext ac) {
								
							}
							
							@SuppressWarnings("unchecked")
							@Override
							public void queryAfter(AopContext ac) {
								List<Map<String, Object>> list = (List<Map<String, Object>>) ac.getObject();
								List<Map<String, Object>> _list = new ArrayList<>(); 
								for (Map<String, Object> map : list) {
									Integer[] deptIds = Convert.toIntArray(map.get("id").toString());
									List<User> users = Blade.create(User.class).findBy("DEPTID in (#{join(deptId)})", CMap.init().set("deptId", deptIds));
									for (User user : users) {
										for (Integer deptId : deptIds) {
											Map<String, Object> userMap = CMap.createHashMap();
											userMap.put("id", user.getId() + 9999);
											userMap.put("pId", deptId);
											userMap.put("name", user.getName());
											userMap.put("open", "false");
											userMap.put("iconSkin", "iconPerson");
											_list.add(userMap);
										}
									}
								}
								list.addAll(_list);
							}
						});
					}
				});

		return json(dept);
	}
	
	
}
