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
package cn.com.xl.system.meta.factory;

import java.util.HashMap;
import java.util.Map;

import cn.com.xl.core.meta.MetaIntercept;
import cn.com.xl.core.meta.MetaManager;
import cn.com.xl.system.meta.intercept.AttachIntercept;
import cn.com.xl.system.model.Attach;

public class AttachFactory extends MetaManager {

	public Class<? extends MetaIntercept> intercept() {
		return AttachIntercept.class;
	}
	
	public String controllerKey() {
		return "attach";
	}

	public String paraPrefix() {
		return getTableName(Attach.class);
	}

	public Map<String, String> renderMap() {
		Map<String, String> renderMap = new HashMap<>();
		renderMap.put(KEY_INDEX, "/system/attach/attach.html");
		renderMap.put(KEY_ADD, "/system/attach/attach_add.html");
		renderMap.put(KEY_EDIT, "/system/attach/attach_edit.html");
		renderMap.put(KEY_VIEW, "/system/attach/attach_view.html");
		return renderMap;
	}

	public Map<String, String> sourceMap() {
		Map<String, String> sourceMap = new HashMap<>();
		sourceMap.put(KEY_INDEX, "attach.sourceList");
		return sourceMap;
	}

}
