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
package cn.com.xl.core.meta;

import org.beetl.sql.core.annotatoin.Table;

import cn.com.xl.core.base.model.BaseModel;

public class MetaTool {
	
	/**
	 * 获取javabean对应的表名
	 * @param clazz  javabean.class
	 * @return String
	 */
	public String getTableName(Class<? extends BaseModel> clazz) {
		return clazz.getAnnotation(Table.class).name();
	}
	
}
