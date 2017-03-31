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
package cn.com.xl.core.listener;

import cn.com.xl.core.config.BladeConfig;
import cn.com.xl.core.constant.Cst;
import cn.com.xl.core.plugins.IPluginHolder;
import cn.com.xl.core.plugins.PluginFactory;
import cn.com.xl.core.plugins.PluginManager;
import cn.com.xl.core.plugins.connection.LogoPlugin;
import cn.com.xl.core.plugins.connection.RedisPlugin;
import cn.com.xl.core.plugins.connection.SQLManagerPlugin;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 启动监听器
 */
@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			globalConstants(Cst.me());
			registerPlugins();
			globalSettings();
			afterBladeStart();
		}
	}
	
	/**   
	 * 全局配置
	*/
	private void globalConstants(Cst me){
		BladeConfig.getConf().globalConstants(me);
	}

	/**
	 * 插件的启用
	 */
	private void registerPlugins() {
		IPluginHolder plugins = PluginFactory.init();
		plugins.register(SQLManagerPlugin.init());
		plugins.register(RedisPlugin.init());
		plugins.register(new LogoPlugin());
		BladeConfig.getConf().registerPlugins(plugins);//自定义配置插件	
		PluginManager.init().start();
	}
	
	/**   
	 * 全局配置
	*/
	private void globalSettings(){
		BladeConfig.getConf().globalSettings();
	}
	
	/**   
	 * 系统启动后执行
	*/
	private void afterBladeStart(){
		BladeConfig.getConf().afterBladeStart();
	}
	
}