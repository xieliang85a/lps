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
package cn.com.xl.core.constant;

import java.math.BigDecimal;

import cn.com.xl.core.intercept.CURDInterceptor;
import cn.com.xl.core.intercept.QueryInterceptor;
import cn.com.xl.core.intercept.SelectInterceptor;
import cn.com.xl.core.meta.ICURD;
import cn.com.xl.core.meta.IQuery;
import cn.com.xl.core.meta.ISelect;
import cn.com.xl.core.shiro.DefaultShiroFactory;
import cn.com.xl.core.shiro.IShiro;
import cn.com.xl.core.toolbox.cache.EhcacheFactory;
import cn.com.xl.core.toolbox.cache.ICache;
import cn.com.xl.core.toolbox.check.ICheck;
import cn.com.xl.core.toolbox.check.PermissionCheckFactory;
import cn.com.xl.core.toolbox.file.DefaultFileProxyFactory;
import cn.com.xl.core.toolbox.file.IFileProxy;
import cn.com.xl.core.toolbox.grid.IGrid;
import cn.com.xl.core.toolbox.grid.JqGridFactory;
import cn.com.xl.core.toolbox.log.BladeLogFactory;
import cn.com.xl.core.toolbox.log.ILog;

/**
 * Blade系统配置类
 */
public class Cst {

	/**
	 * 开发模式
	 */
	private boolean devMode = false;

	/**
	 * 远程上传模式
	 */
	private boolean remoteMode = false;

	/**
	 * 全文索引开启
	 */
	private boolean luceneIndex = false;

	/**
	 * 上传下载路径(物理路径)
	 */
	private String remotePath = "D://blade";

	/**
	 * 上传路径(相对路径)
	 */
	private String uploadPath = "/upload";

	/**
	 * 下载路径
	 */
	private String downloadPath = "/download";
	
	/**
	 * 图片压缩
	 */
	private boolean compress = false;
	
	/**
	 * 图片压缩比例
	 */
	private BigDecimal compressScale = new BigDecimal(2);
	
	/**
	 * 图片缩放选择:true放大;false缩小
	 */
	private boolean compressFlag = false;

	/**
	 * 项目物理路径
	 */
	private String realPath = ConstConfig.REAL_PATH;

	/**
	 * 项目相对路径
	 */
	private String contextPath = ConstConfig.CONTEXT_PATH;

	/**
	 * 密码允许错误次数
	 */
	private int passErrorCount = 6;

	/**
	 * 密码锁定小时数
	 */
	private int passErrorHour = 6;
	
	/**
	 * 是否启用乐观锁
	 */
	private boolean optimisticLock = true;

	/**
	 * 默认grid分页工厂类
	 */
	private IGrid defaultGridFactory = new JqGridFactory();

	/**
	 * 默认日志处理工厂类
	 */
	private ILog defaultLogFactory = new BladeLogFactory();

	/**
	 * 默认自定义权限检查工厂类
	 */
	private ICheck defaultCheckFactory = new PermissionCheckFactory();

	/**
	 * 默认shirorealm工厂类
	 */
	private IShiro defaultShiroFactory = new DefaultShiroFactory();

	/**
	 * 默认缓存工厂类
	 */
	private ICache defaultCacheFactory = new EhcacheFactory();
	
	/**
	 * 默认文件上传转换工厂类
	 */
	private IFileProxy defaultFileProxyFactory = new DefaultFileProxyFactory();
	
	/**
	 * 默认CURD工厂类
	 */
	private ICURD defaultCURDFactory = new CURDInterceptor();
	
	/**
	 * 默认查询工厂类
	 */
	private IQuery defaultQueryFactory = new QueryInterceptor();
	
	/**
	 * 默认select查询工厂类
	 */
	private ISelect defaultSelectFactory = new SelectInterceptor();

	private static final Cst me = new Cst();

	private Cst() {

	}

	public static Cst me() {
		return me;
	}

	public boolean isDevMode() {
		return devMode;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}

	public boolean isRemoteMode() {
		return remoteMode;
	}

	public void setRemoteMode(boolean remoteMode) {
		this.remoteMode = remoteMode;
	}

	public boolean isLuceneIndex() {
		return luceneIndex;
	}

	public void setLuceneIndex(boolean luceneIndex) {
		this.luceneIndex = luceneIndex;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public BigDecimal getCompressScale() {
		return compressScale;
	}

	public void setCompressScale(BigDecimal compressScale) {
		this.compressScale = compressScale;
	}

	public boolean isCompressFlag() {
		return compressFlag;
	}

	public void setCompressFlag(boolean compressFlag) {
		this.compressFlag = compressFlag;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public int getPassErrorCount() {
		return passErrorCount;
	}

	public void setPassErrorCount(int passErrorCount) {
		this.passErrorCount = passErrorCount;
	}

	public int getPassErrorHour() {
		return passErrorHour;
	}

	public void setPassErrorHour(int passErrorHour) {
		this.passErrorHour = passErrorHour;
	}

	public String getUploadRealPath() {
		return (remoteMode ? remotePath : realPath) + uploadPath;
	}

	public String getUploadCtxPath() {
		return contextPath + uploadPath;
	}

	public String getRealPath() {
		return realPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public boolean isOptimisticLock() {
		return optimisticLock;
	}

	public void setOptimisticLock(boolean optimisticLock) {
		this.optimisticLock = optimisticLock;
	}

	public IGrid getDefaultGridFactory() {
		return defaultGridFactory;
	}

	public void setDefaultGridFactory(IGrid defaultGridFactory) {
		this.defaultGridFactory = defaultGridFactory;
	}
	
	public ILog getDefaultLogFactory() {
		return defaultLogFactory;
	}

	public void setDefaultLogFactory(ILog defaultLogFactory) {
		this.defaultLogFactory = defaultLogFactory;
	}

	public ICheck getDefaultCheckFactory() {
		return defaultCheckFactory;
	}

	public void setDefaultCheckFactory(ICheck defaultCheckFactory) {
		this.defaultCheckFactory = defaultCheckFactory;
	}

	public IShiro getDefaultShiroFactory() {
		return defaultShiroFactory;
	}

	public void setDefaultShiroFactory(IShiro defaultShiroFactory) {
		this.defaultShiroFactory = defaultShiroFactory;
	}

	public ICache getDefaultCacheFactory() {
		return defaultCacheFactory;
	}

	public void setDefaultCacheFactory(ICache defaultCacheFactory) {
		this.defaultCacheFactory = defaultCacheFactory;
	}

	public IFileProxy getDefaultFileProxyFactory() {
		return defaultFileProxyFactory;
	}

	public void setDefaultFileProxyFactory(IFileProxy defaultFileProxyFactory) {
		this.defaultFileProxyFactory = defaultFileProxyFactory;
	}

	public ICURD getDefaultCURDFactory() {
		return defaultCURDFactory;
	}

	public void setDefaultCURDFactory(ICURD defaultCURDFactory) {
		this.defaultCURDFactory = defaultCURDFactory;
	}

	public IQuery getDefaultQueryFactory() {
		return defaultQueryFactory;
	}

	public void setDefaultQueryFactory(IQuery defaultQueryFactory) {
		this.defaultQueryFactory = defaultQueryFactory;
	}

	public ISelect getDefaultSelectFactory() {
		return defaultSelectFactory;
	}

	public void setDefaultSelectFactory(ISelect defaultSelectFactory) {
		this.defaultSelectFactory = defaultSelectFactory;
	}
	
	
}
