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
package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author osvaldo.rodriguez
 */
public class Workflow {

    public String title;
    public String uuid;
    public String id;
    public List<SubjectSet> subjectSetList;

    public Workflow() {
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }

    public Workflow(String id, String title) {
        this.id = id;
        this.title = title;
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }

    public Workflow(String title) {
        this.title = title;
        UUID uuid = UUID.randomUUID();
        this.uuid = uuid.toString();
    }
    
    public boolean equals(Object object) {
    	Workflow workflow = (object != null)?(Workflow)object:null;
    	return (workflow != null)?this.id.equals(workflow.id):false;
    }
    
    public String getId() {
        return this.id;
    }
}
