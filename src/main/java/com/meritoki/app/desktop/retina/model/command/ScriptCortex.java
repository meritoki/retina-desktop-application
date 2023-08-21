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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.meritoki.app.desktop.retina.model.Model;
import com.meritoki.app.desktop.retina.model.document.Page;
import com.meritoki.app.desktop.retina.model.provider.meritoki.Meritoki;
import com.meritoki.library.cortex.model.Belief;
import com.meritoki.library.cortex.model.network.Cortex;
import com.meritoki.library.cortex.model.network.Level;
import com.meritoki.library.cortex.model.network.Network;
import com.meritoki.library.cortex.model.unit.Concept;

public class ScriptCortex extends Command {
	private Logger logger = LogManager.getLogger(ScriptPage.class.getName());

	public ScriptCortex(Model model) {
		super(model, "scriptConcept");
	}

	public void execute() throws Exception {
		logger.info("execute()");
		this.user = this.model.cache.user;
		this.operationList = (LinkedList<Operation>) this.getOperationList(this.model.cache.cortex,
				this.model.cache.conceptScript);
	}
	
	@Override
	public void undo() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redo() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * SWAP 1-2:3-4 INTERLACE 1-2:3-4 INSERT 1-2:3-4 SHEET even:odd | SHEET odd:even
	 * | SHEET 1:2 | SHEET 2:3
	 * 
	 * @param value
	 * @throws Exception
	 */
	public List<Operation> getOperationList(Cortex cortex, String value) throws Exception {
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
				List<Integer> set = this.getIndexList(instruction);
				System.out.println(set);
				operationList.addAll(forgetConcept(cortex, set));
			} else if (i.contains("REMEMBER")) {
				instruction = i.replaceFirst("REMEMBER", "");
				operationList.addAll(rememberConcept(cortex, instruction));
			} else if (i.contains("MAP")) {
				instruction = i.replaceFirst("MAP", "");
				parameters = instruction.split(":");
				a = parameters[0].trim();
				b = parameters[1].trim();
				List<Integer> set = this.getIndexList(a);
				operationList.addAll(mapConcept(cortex, set, b));
			} else if (i.contains("CLEAR")) {
				instruction = i.replaceFirst("CLEAR", "");
				a = instruction.trim();
				operationList.addAll(clearConcept(cortex.getBelief().conceptList));
			}
		}
		logger.info("getOperationList(...) operationList.size()=" + operationList.size());
		return operationList;
	}

	public List<Operation> forgetConcept(Cortex cortex, List<Integer> set) {
		Belief belief = cortex.getBelief();
		System.out.println("forgetConcept(" + belief + ", " + set + ")");
		List<Operation> operationList = new ArrayList<>();
		Operation operation = new Operation();
		List<Concept> cList = new ArrayList<>();
		for (Concept page : belief.conceptList) {
			cList.add(new Concept(page));
		}

		operation.object = cList;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		
		//Remove Cortex Map 
		
		// Logic
		if(set.size() == 1 && set.get(0) == -1) {
			set = new ArrayList<>();
			int index = 0;
			for(Concept c: belief.conceptList) {
				set.add(index);
				index++;
			}
			
		}
		
		List<Concept> conceptList = new ArrayList<>();
		for (int a : set) {
			System.out.println(a);
			Concept removeConcept = belief.conceptList.remove(a);
			conceptList.add(removeConcept);
		}
		
		if(cortex instanceof Network) {
			Network network = (Network)cortex;
			Level level = network.getRootLevel();
			level.removeCoincidenceConceptList(belief.coincidence, conceptList);
		}
		
		operation = new Operation();
		operation.object = belief.conceptList;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> rememberConcept(Cortex cortex, String value) {
		System.out.println("rememberConcept(" + cortex + ", " + value + ")");
		Belief belief = cortex.getBelief();
		List<Operation> operationList = new ArrayList<>();
		Operation operation = new Operation();
		List<Concept> cList = new ArrayList<>();
		for (Concept page : belief.conceptList) {
			cList.add(new Concept(page));
		}
		operation.object = cList;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		belief.conceptList.add(new Concept(value));
		
		if(cortex instanceof Network) {
			Network network = (Network)cortex;
			Level level = network.getRootLevel();
			level.addCoincidenceConceptList(belief.coincidence, belief.conceptList);
		}
		

		operation = new Operation();
		operation.object = cortex;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> mapConcept(Cortex cortex, List<Integer> set, String value) {
		System.out.println("mapConcept(" + cortex + ", " + set + ", " + value + ")");
		List<Operation> operationList = new ArrayList<>();
		Operation operation = new Operation();
		List<Concept> cList = new ArrayList<>();
		for (Concept page : cortex.getBelief().conceptList) {
			cList.add(new Concept(page));
		}
		operation.object = cList;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);

		// Logic
		if(set.size() == 1 && set.get(0) == -1) {
			set = new ArrayList<>();
			int index = 0;
			for(Concept c: cortex.getBelief().conceptList) {
				set.add(index);
				index++;
			}
			
		}
		for (int i : set) {
			String key = cortex.getBelief().conceptList.get(i).toString();
			if (!key.contains(value)) {
				System.out.println("key:" + key);
				System.out.println("value:" + value);
				try{
				    UUID uuid = UUID.fromString(key);
				    cortex.conceptMap.put(key, value);
				    //do something
				} catch (IllegalArgumentException exception){
				    //handle the case where string is not valid UUID 
				}
				
			}
		}
		
		for (Belief belief : cortex.beliefList) {
			belief.setConceptList(cortex.conceptMap, belief.conceptList);
		}
		
		operation = new Operation();
		operation.object = cortex.getBelief().conceptList;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Operation> clearConcept(List<Concept> conceptList) {
		List<Operation> operationList = new ArrayList<>();
		Operation operation = new Operation();
		List<Concept> cList = new ArrayList<>();
		for (Concept page : conceptList) {
			cList.add(new Concept(page));
		}
		operation.object = cList;
		operation.sign = 1;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		// Logic
		conceptList.removeAll(conceptList);
		operation = new Operation();
		operation.object = conceptList;
		operation.sign = 0;
		operation.id = UUID.randomUUID().toString();
		operationList.add(operation);
		return operationList;
	}

	public List<Integer> getIndexList(String value) throws Exception {
		System.out.println("getIndexList(" + value + ")");
		List<Integer> indexList = new ArrayList<>();
		value = value.toLowerCase();
		value = value.trim();
		if (value.contains("all")) {
			if (value.equals("all")) {
				indexList.add(-1);
			} else {
				throw new Exception("Invalid index");
			}
		} else {
			String[] commaArray = value.split(",");
			if (commaArray.length > 1) {
				for (String c : commaArray) {
					c.trim();
					if (c.contains("-")) {
						String[] dashArray = c.split("-");
						try {
							int a = Integer.parseInt(dashArray[0].trim());
							int b = Integer.parseInt(dashArray[1].trim());
							if (a < b) {
								for (int i = a; i <= b; i++) {
									indexList.add(i);
								}
							}
						} catch (Exception e) {
							throw new Exception("A Not integer(s)");
						}
					} else {
						try {
							int a = Integer.parseInt(c);
							indexList.add(a);
						} catch (Exception e) {
							throw new Exception("B Not integer(s)");
						}
					}
				}
			} else {
				try {
					int a = Integer.parseInt(value);
					indexList.add(a);
				} catch (Exception e) {
					throw new Exception("C Not integer(s)");
				}
			}
		}
		logger.info("getIndexList(" + value + ") indexList=" + indexList);
		return indexList;
	}
}
