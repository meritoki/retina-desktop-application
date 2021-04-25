package com.meritoki.app.desktop.retina.model.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Image;

public class CreateProject extends Command {

	public CreateProject(Model document) {
		super(document, "createProject");
	}
	
	public void execute() throws Exception {
		String file = this.model.cache.fileArray[0];
		List<File> fileList = new ArrayList<>();
		if(this.model.system.file != null) {
			fileList.add(this.model.system.file);
			for(Image image: this.model.document.getImageList()) {
				fileList.add(new File(NodeController.getDocumentCache(model.document.uuid)+File.separatorChar+image.fileCache));
			}
			NodeController.zip(fileList, file);
		} else {
			throw new Exception("Please Save Document First");
		}
	}
}
