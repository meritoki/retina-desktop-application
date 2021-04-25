package com.meritoki.app.desktop.retina.model.command;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;

public class OpenProject extends Command {

	public OpenProject(Model document) {
		super(document, "openProject");
	}
	
	public void execute() throws Exception {
		String fileName = this.model.cache.fileArray[0];
		File file = new File(fileName);
		System.out.println(file);
		ZipFile zipFile = new ZipFile(fileName);
	    Enumeration<? extends ZipEntry> entries = zipFile.entries();
	    InputStream is;
	    String documentJSON = this.model.system.defaultFileName;
	    ZipEntry documentEntry = null;
	    while(entries.hasMoreElements()){
	        ZipEntry entry = entries.nextElement();
	        String entryName = entry.getName();
	        is = zipFile.getInputStream(entry);
	        if(entryName.contains(".json")) {
	        	documentEntry = entry;
	        	documentJSON = file.getParent()+File.separatorChar+entryName;
	        	Files.copy(zipFile.getInputStream(documentEntry), Paths.get(documentJSON));
		    	this.model.openDocument(new File(documentJSON));
	        } 
//	        else {
//	        	if(documentUUID != null) {
//	        		Files.copy(is, Paths.get(NodeController.getDocumentCache(documentUUID)+File.separatorChar+entryName));
//	        	}
//	        }
	    }
	    entries = zipFile.entries();
	    while(entries.hasMoreElements()){
	        ZipEntry entry = entries.nextElement();
	        String entryName = entry.getName();
	        is = zipFile.getInputStream(entry);
	        if(!entryName.contains(".json")) {
	        	if(this.model.document != null) {
	        		String imagePath = NodeController.getDocumentCache(this.model.document.uuid)+File.separatorChar+entryName;
	        		System.out.println(this+".execute() imagePath="+imagePath);
	        		Files.copy(is, Paths.get(imagePath));
	        	}
	        }
	    }
//	    if(documentEntry != null) {
//		    System.out.println(documentJSON);
//	    	Files.copy(zipFile.getInputStream(documentEntry), Paths.get(documentJSON));
//	    	this.model.openDocument(new File(documentJSON));
//	    }
	    zipFile.close();
	}
}
