/*
 * Copyright 2021 Joaquin Osvaldo Rodriguez
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
package com.meritoki.app.desktop.retina.model.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.controller.node.NodeController;
import com.meritoki.app.desktop.retina.model.Model;

public class OpenProject extends Command {

	private static Logger logger = LogManager.getLogger(OpenProject.class.getName());

	public OpenProject(Model document) {
		super(document, "openProject");
	}

	public void execute() throws Exception {
		String fileName = this.model.cache.fileArray[0];
		logger.info("execute() fileName=" + fileName);
		File file = new File(fileName);
		if (!fileName.contains(".ret")) {
			throw new Exception("Not a Retina Project File (.ret)");
		}
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(fileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			InputStream is;
			String documentPath = this.model.system.defaultFileName;
			logger.info("execute() documentJSON=" + documentPath);
			ZipEntry documentEntry = null;
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String entryName = entry.getName();
				is = zipFile.getInputStream(entry);
				if (entryName.contains(".json")) {
					documentEntry = entry;
					documentPath = file.getParent() + File.separatorChar + entryName;
//					System.out.println(this + ".execute() documentPath=" + documentPath);
					File documentFile = new File(documentPath);
					if (!documentFile.exists()) {
						Files.copy(zipFile.getInputStream(documentEntry), Paths.get(documentPath));
					}
					this.model.openDocument(documentFile);
				}
			}
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String entryName = entry.getName();
				is = zipFile.getInputStream(entry);
				if (!entryName.contains(".json")) {
					if (this.model.document != null) {
						String imagePath = NodeController.getDocumentCache(this.model.document.uuid)
								+ File.separatorChar + entryName;
//						System.out.println(this + ".execute() imagePath=" + imagePath);
						File imageFile = new File(imagePath);
						if (!imageFile.exists()) {
							Files.copy(is, Paths.get(imagePath));
						}
					}
				}
			}
			zipFile.close();
		} catch (Exception e) {
			throw new Exception("Retina Project File (.ret) Corrupt");
		} finally {
			if(zipFile != null) {
				zipFile.close();
			}
		}

	}
}
