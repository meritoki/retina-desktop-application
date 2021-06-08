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
		System.out.println(this+".execute() fileName="+fileName);
		File file = new File(fileName);
		if(!fileName.contains(".ret")) {
			throw new Exception("Not a Retina Project File (.ret)");
		}
		ZipFile zipFile = new ZipFile(fileName);
	    Enumeration<? extends ZipEntry> entries = zipFile.entries();
	    
	    InputStream is;
	    String documentPath = this.model.system.defaultFileName;
	    System.out.println(this+".execute() documentJSON="+documentPath);
	    ZipEntry documentEntry = null;
	    while(entries.hasMoreElements()){
	        ZipEntry entry = entries.nextElement();
	        String entryName = entry.getName();
	        is = zipFile.getInputStream(entry);
	        if(entryName.contains(".json")) {
	        	documentEntry = entry;
	        	documentPath = file.getParent()+File.separatorChar+entryName;
	        	System.out.println(this+".execute() documentPath="+documentPath);
	        	File documentFile = new File(documentPath);
	        	if(!documentFile.exists()) {
	        		Files.copy(zipFile.getInputStream(documentEntry), Paths.get(documentPath));
	        	}
	        	this.model.openDocument(documentFile);
	        	
	        } 
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
	        		File imageFile = new File(imagePath);
	        		if(!imageFile.exists()) {
	        			Files.copy(is, Paths.get(imagePath));
	        		}
	        	}
	        }
	    }
	    zipFile.close();
	}
}
