package com.meritoki.app.desktop.retina.model.command;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.ModelPrototype;
import com.meritoki.app.desktop.retina.model.document.Document;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.document.Project;

public class AddPage extends Command {
	private static Logger logger = LogManager.getLogger(AddPage.class.getName());
	public AddPage(ModelPrototype project) {
		this.model = project;
		this.name = "addPage";
	}
	
    @Override // Command
    public void execute() {
    	logger.info("execute()");
    	this.user = this.model.user;
		Document document = (this.model != null) ? this.model.getDocument() : null;
        Project project = (document != null) ? document.getProject() : null;
    	Page page = null;
    	for(File file: model.variable.files) {
    		page = new Page();
			page.fileList.add(new com.meritoki.app.desktop.retina.model.document.File(file.getParent(),file.getName()));
    		if(project != null) {
    			project.addPage(page);
    		}
    	}
    }
}
