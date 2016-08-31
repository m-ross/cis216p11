package lab11;

import lab04.*;
import stuff.*;
import java.util.*;
import java.io.*;

public class MailingList {
	private ArrayList<Entry> entries;

	public MailingList() {
		entries = new ArrayList<Entry>();
	}

	public MailingList(String fileN) throws IOException {
		entries = new ArrayList<Entry>();
		String name, addr, phone;
		BufferedReader inFile = new BufferedReader(new FileReader(fileN));
		while(inFile.ready()) {
			name = inFile.readLine();
			addr = inFile.readLine();
			phone = inFile.readLine();
			entries.add(new Entry(name, addr, phone));
		}
		inFile.close();
	}

	public void addPerson(String name, String addr, String phone) {
		entries.add(new Entry(name, addr, phone));
	}

	public void remPerson(String name) throws SearchFailException {
		for(int i = 0; i < entries.size(); i++)
			if(entries.get(i).getName() != null && entries.get(i).getName().equalsIgnoreCase(name)) {
				entries.remove(i);
				return;
			}
		throw new SearchFailException();
	}

	public Entry findPerson(String search, char type) throws SearchFailException {
		switch(type) {
		case 'n':
			for(Entry entryCur:entries) {
				if(entryCur.getName() != null && entryCur.getName().equalsIgnoreCase(search))
					return entryCur;
			}
			throw new SearchFailException();
		case 'a':
			for(Entry entryCur:entries) {
				if(entryCur.getAddr() != null && entryCur.getAddr().equalsIgnoreCase(search))
					return entryCur;
			}
			throw new SearchFailException();
		case 'p':
			for(Entry entryCur:entries) {
				if(entryCur.getPhone() != null && entryCur.getPhone().equalsIgnoreCase(search))
					return entryCur;
			}
			throw new SearchFailException();
		}
		return new Entry();
	}

	public ArrayList<Entry> getList() { //Display header to user		
		return entries;
	}
}