package com.meritoki.retina.application.desktop.model.command;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.retina.application.desktop.model.Document;
import com.meritoki.retina.application.desktop.model.Model;
import com.meritoki.retina.application.desktop.model.document.Page;
import com.meritoki.retina.application.desktop.model.document.Project;

public class AddPage extends Command {
	private static Logger logger = LogManager.getLogger(AddPage.class.getName());
	public AddPage(Model project) {
		this.model = project;
		this.name = "addPage";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
		Document document = (model != null) ? model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
    	Page page = null;
    	for(File file: model.variable.files) {
    		page = new Page();
			page.fileList.add(new com.meritoki.retina.application.desktop.model.document.File(file.getParent(),file.getName()));
    		if(project != null) {
    			project.addPage(page);
    		}
    	}
    }
}
