package cn.com.xl.system.controller;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.com.xl.core.annotation.Json;
import cn.com.xl.core.base.controller.BladeController;
import cn.com.xl.core.constant.Cst;
import cn.com.xl.core.plugins.dao.Db;
import cn.com.xl.core.toolbox.CMap;
import cn.com.xl.core.toolbox.file.BladeFile;
import cn.com.xl.core.toolbox.file.BladeFileKit;
import cn.com.xl.core.toolbox.kit.PathKit;

@Controller
@RequestMapping("/uploadify")
public class UploadifyController extends BladeController {
	
	@Json
	@RequestMapping("/upload")
	public CMap upload(@RequestParam("Filedata") MultipartFile file) {
		CMap cmap = CMap.init();
		if (null == file) {
			cmap.set("error", 1);
			cmap.set("message", "请选择要上传的图片");
			return cmap;
		}
		String originalFileName = file.getOriginalFilename();
		String dir = getParameter("dir", "image");
		// 测试后缀
		boolean ok = BladeFileKit.testExt(dir, originalFileName);
		if (!ok) {
			cmap.set("error", 1);
			cmap.set("message", "上传文件的类型不允许");
			return cmap;
		}
		BladeFile bf = getFile(file);
		bf.transfer();
		Object fileId = bf.getFileId();	
		String url = "/uploadify/renderFile/" + fileId;
		cmap.set("error", 0);
		cmap.set("fileId", fileId);
		cmap.set("url", Cst.me().getContextPath() + url);
		cmap.set("fileName", originalFileName);
		return cmap;	
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/renderFile/{id}")
	public void renderFile(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
		Map<String, Object> file = Db.findById("BLADE_ATTACH", id);
		String url = file.get("URL").toString();
		File f = new File((Cst.me().isRemoteMode() ? "" : PathKit.getWebRootPath()) + url);
		makeFile(response, f);
	}
	
}
