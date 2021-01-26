package org.luvx.pattern.structural.composite;

abstract class FileType {
	public void add(FileType c) {
	};

	public void remove(FileType c) {
	};

	public FileType getChild(int i) {
		return null;
	};

	public abstract void operation();
}
