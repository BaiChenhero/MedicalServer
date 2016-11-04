package com.example.mongomodel;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;  


public class Department implements Serializable {  
  
    private static final long serialVersionUID = 1L;  
    
    @Id  
    String id;
    String name;
    String parent;
    List<String> children;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public List<String> getChildren() {
		return children;
	}
	public void setChildren(List<String> children) {
		this.children = children;
	}

}
