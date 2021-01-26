package org.luvx.pattern.structural.composite;

class Text extends FileType {
	private String name;

	public Text(String name) {
		this.name = name;
	}

	public void operation() {
		System.out.println("    对文件'" + name + "'进行读取");
	}
}