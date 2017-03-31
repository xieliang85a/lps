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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.xl.common.base.BaseController;
import cn.com.xl.core.annotation.Before;
import cn.com.xl.core.annotation.Json;
import cn.com.xl.core.constant.Const;
import cn.com.xl.core.plugins.dao.Blade;
import cn.com.xl.core.shiro.ShiroKit;
import cn.com.xl.core.toolbox.Func;
import cn.com.xl.core.toolbox.ajax.AjaxResult;
import cn.com.xl.core.toolbox.kit.LogKit;
import cn.com.xl.core.toolbox.log.BladeLogManager;
import cn.com.xl.system.meta.intercept.LoginValidator;
import cn.com.xl.system.model.LoginLog;

@Controller
public class LoginController extends BaseController implements Const{

	@RequestMapping("/")
	public String index() {
		return INDEX_REALPATH;
	}
	
	/**
	 * 登陆地址
	 */
	@GetMapping("/login")
	public String login() {
		if (ShiroKit.isAuthenticated()) {
			return redirect("/");
		}
		return LOGIN_REALPATH;
	}

	/**
	 * 登陆请求
	 */
	@Json
	@Before(LoginValidator.class)
	@PostMapping("/login")
	public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
		String account = getParameter("account");
		String password = getParameter("password");
		String imgCode = getParameter("imgCode");
		if (!validateCaptcha(response, imgCode)) {
			return error("验证码错误");
		}
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password.toCharArray());
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			Session session = ShiroKit.getSession();
			LogKit.println("\nsessionID	: {} ", session.getId());
			LogKit.println("sessionHost	: {}", session.getHost());
			LogKit.println("sessionTimeOut	: {}", session.getTimeout());
		} catch (UnknownAccountException e) {
			LOGGER.error("账号不存在!", e);
			return error("账号不存在");
		} catch (DisabledAccountException e) {
			LOGGER.error("账号未启用!", e);
			return error("账号未启用");
		} catch (IncorrectCredentialsException e) {
			LOGGER.error("密码错误!", e);
			return error("密码错误");
		} catch (RuntimeException e) {
			LOGGER.error("未知错误,请联系管理员!", e);
			return error("未知错误,请联系管理员");
		}
		doLog(ShiroKit.getSession(), "登录");
		return success("登录成功");
	}

	@RequestMapping("/logout")
	public String logout() {
		doLog(ShiroKit.getSession(), "登出");
		Subject currentUser = ShiroKit.getSubject();
		currentUser.logout();
		return redirect("/login");
	}

	@RequestMapping("/unauth")
	public String unauth() {
		if (ShiroKit.notAuthenticated()) {
			return redirect("/login");
		}
		return NOPERMISSION_PATH;
	}

	@RequestMapping("/captcha")
	public void captcha(HttpServletResponse response) {
		makeCaptcha(response);
	}

	public void doLog(Session session, String type){
		if(!BladeLogManager.isDoLog()){
			return;
		}
		try{
			LoginLog log = new LoginLog();
			String msg = Func.format("[sessionID]: {} [sessionHost]: {} [sessionHost]: {}", session.getId(), session.getHost(), session.getTimeout());
			log.setLogname(type);
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed("1");
			log.setUserid(Func.toStr(ShiroKit.getUser().getId()));
			Blade.create(LoginLog.class).save(log);
		}catch(Exception ex){
			LogKit.logNothing(ex);
		}
	}
	
}
