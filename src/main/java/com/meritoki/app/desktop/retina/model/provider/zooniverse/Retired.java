package com.meritoki.app.desktop.retina.model.provider.zooniverse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Retired {
	
	@JsonProperty
	public String id;
	@JsonProperty("workflow_id")
	public String workflowID;
	@JsonProperty("classifications_count")
	public String classificationsCount;
	@JsonProperty("created_at")
	public String createdAt;
	@JsonProperty("updated_at")
	public String updateAt;
	@JsonProperty("retired_at")
	public String retiredAt;
	@JsonProperty("subject_id")
	public String subjectId;
	@JsonProperty("retirement_reason")
	public String taskLabel;
	
	

}
