package lab11;

public class Entry {
	private String name, addr, phone;

	public Entry() { }

	public Entry(String name, String addr, String phone) {
		load(name, addr, phone);
	}

	public void load(String name, String addr, String phone) {
		this.name = name;
		this.addr = addr;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public String getAddr() {
		return addr;
	}

	public String getPhone() {
		return phone;
	}
}