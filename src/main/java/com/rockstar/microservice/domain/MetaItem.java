package com.rockstar.microservice.domain;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaItem {
	
	@Id
	@NotEmpty
	@JsonProperty("slug")
    private String id;
	
	@NotEmpty
	@Size(max=100)
	private String title;
	
	@Size(max=255)
	private String description;
	private String thumbnail;
	private Integer displayOrder;
	private Boolean enabled;
	private Collection<String> versions;
	
	public MetaItem() {
		this.enabled = Boolean.FALSE;
	}
	
	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Collection<String> getVersions() {
		return versions;
	}

	public void setVersions(Collection<String> versions) {
		this.versions = versions;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}
