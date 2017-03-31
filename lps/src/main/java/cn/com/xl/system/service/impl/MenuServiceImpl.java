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
package cn.com.xl.system.service.impl;

import org.springframework.stereotype.Service;

import cn.com.xl.core.base.service.BaseService;
import cn.com.xl.core.plugins.dao.Blade;
import cn.com.xl.core.toolbox.CMap;
import cn.com.xl.core.toolbox.support.Convert;
import cn.com.xl.system.model.Menu;
import cn.com.xl.system.service.MenuService;

@Service
public class MenuServiceImpl extends BaseService<Menu> implements MenuService {

	@Override
	public int findLastNum(String code) {
		try{
			Blade blade = Blade.create(Menu.class);
			Menu menu = blade.findFirstBy("pCode = #{pCode} order by num desc", CMap.init().set("pCode", code));
			return menu.getNum() + 1;
		}
		catch(Exception ex){
			return 1;
		}
	}

	@Override
	public boolean isExistCode(String code) {
		Blade blade = Blade.create(Menu.class);
		String sql = "select * from blade_menu where code = #{code}";
		boolean temp = blade.isExist(sql, CMap.init().set("code", code));
		return temp;
	}

	@Override
	public boolean updateStatus(String ids, Integer status) {
		CMap paras = CMap.init().set("status", status).set("ids", Convert.toIntArray(ids));
		Blade blade = Blade.create(Menu.class);
		boolean temp = blade.updateBy("status=#{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

}
