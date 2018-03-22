package com.xh.activiti.commons.result;

import java.util.List;

/**
 * @description：TreeVO
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class Tree {

	private Long id;
	private String name;
	// open,closed
	private String state = "open";
	private boolean checked = false;
	private Object attributes;
	private String iconCls;
	private Long pId;
	// ajax,iframe
	private String openMode;
	private List<Tree> children; // null不输出

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setState(Integer opened) {
		this.state = (null != opened && opened == 1) ? "open" : "closed";
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Object getAttributes() {
		return attributes;
	}

	public void setAttributes(Object attributes) {
		this.attributes = attributes;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getOpenMode() {
		return openMode;
	}

	public void setOpenMode(String openMode) {
		this.openMode = openMode;
	}

}
