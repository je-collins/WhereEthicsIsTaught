package com.jordan.ethics;

import java.util.ArrayList;

public enum Flag {
	Graduate("Graduate", 1);
	
	private String name;
	private int flag;
	private Flag(String name, int flag) {
		this.name = name;
		this.flag = flag;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public int getFlag() {
		return flag;
	}
	
	public static Flag[] getFlags(int flags) {
		ArrayList<Flag> list = new ArrayList<Flag>();
		for (Flag f : values()) {
			if ((flags & f.getFlag()) > 0) {
				list.add(f);
			}
		}
		return list.toArray(new Flag[0]);
	}
}
