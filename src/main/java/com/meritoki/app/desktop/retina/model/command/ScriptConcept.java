package com.meritoki.app.desktop.retina.model.command;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.library.cortex.model.Concept;

public class ScriptConcept extends Command {
	private Logger logger = LogManager.getLogger(ScriptPage.class.getName());

	public ScriptConcept(Model document) {
		super(document, "executeScript");
	}

	public void execute() throws Exception {
		logger.info("execute()");
		this.user = this.model.cache.user;
		Meritoki meritoki = (Meritoki)this.model.system.providerMap.get("meritoki");
		this.operationList = (LinkedList<Operation>) this.getOperationList(this.model.cache.pageList,
				this.model.cache.script);
	}
	
	/**
	 * SWAP 1-2:3-4 INTERLACE 1-2:3-4 INSERT 1-2:3-4 SHEET even:odd | SHEET odd:even
	 * | SHEET 1:2 | SHEET 2:3
	 * 
	 * @param value
	 * @throws Exception
	 */
	public List<Operation> getOperationList(List<Concept> conceptList, String value) throws Exception {
		LinkedList<Operation> operationList = new LinkedList<>();
		value = value.replace("\n", "").replace("\r", "");
		String[] instructions = value.split(";");
		String instruction;
		String[] parameters;
		String a;
		String b;
		for (String i : instructions) {
			if (i.contains("FORGET")) {
				instruction = i.replaceFirst("FORGET", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(forgetConcept(conceptList, a, b));
			} else if (i.contains("MAP")) {
				instruction = i.replaceFirst("MAP", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(mapConcept(conceptList, a, b));
			} else if (i.contains("JOIN")) {
				instruction = i.replaceFirst("JOIN", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				operationList.addAll(joinConcept(conceptList, a, b));
			} else if (i.contains("CLEAR")) {
				instruction = i.replaceFirst("CLEAR", "");
				a = instruction.trim();
				operationList.addAll(clearConcept(conceptList, a));
			}
		}
		logger.info("getOperationList(...) operationList.size()=" + operationList.size());
		return operationList;
	}

}
