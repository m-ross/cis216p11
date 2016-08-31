/*	Program Name:   Lab 11 Mailing List Revised
	Programmer:     Marcus Ross
	Date Due:       02 Dec 2013
	Description:	Revised to use objects. No wonder new students aren't taught anything about GUI. Using a GUI is not just a different way of presenting a program, but a task in itself! As usual, I would appreciate any criticism on my design—especially since I (had to) learned so much stuff on my own, so I have no idea what's good and bad.
*/

package lab11;

import lab04.*;
import stuff.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Main {
	public static void main (String args[]) {
		new Menu();
	}
}

class Menu extends Frame implements WindowListener {
	private MailingList mailList;
	private Panel panel; //object attribute because all methods use it

	public Menu() {
		setResizable(false);
		addWindowListener(this);
		// setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mail.png")));
		setIconImage(new ImageIcon("mail.png").getImage()); // when running without jar
		load();
		GetFileN(); // called to load initial mailing list file
	}

	private void load() { // this method sets the frame to the main menu
		panel = new Panel(new GridLayout(5, 1));

		setSize(250, 200);
		setTitle("Mailing List Utility");

		Button btnAdd = new Button("Add a person");
		Button btnRem = new Button("Remove a person");
		Button btnFind = new Button("Search the list");
		Button btnList = new Button("Display the list");
		Button btnLoad = new Button("Load another list from file");

		btnAdd.addActionListener(new AddPerson());
		btnRem.addActionListener(new RemPerson());
		btnFind.addActionListener(new SearchList());
		btnList.addActionListener(new ShowList());
		btnLoad.addActionListener(new GetFileN());

		panel.add(btnAdd);
		panel.add(btnRem);
		panel.add(btnFind);
		panel.add(btnList);
		panel.add(btnLoad);

		removeAll();
		add(panel);
		setLocationRelativeTo(null);
		validate();
		setVisible(true);
	}

	private void initList(String fileN) throws IOException { // called each time a file is loaded
		mailList = new MailingList(fileN);
	}

	private void GetFileN() { // prompt for file name
		setVisible(false);
		setSize(270, 140);
		setTitle("Choose file to load list from");

		SpringLayout layout = new SpringLayout();
		panel = new Panel(layout);

		Label msg = new Label();
		msg.setForeground(Color.RED);
		Label label = new Label("File name");
		TextField fileF = new TextField();

		Button btnOK = new Button("OK");

		btnOK.addActionListener(new LoadList(fileF, msg));

		panel.add(msg);
		panel.add(label);
		panel.add(fileF);
		panel.add(btnOK);

		layout.putConstraint(SpringLayout.WEST, msg, -50, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, msg, 50, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.SOUTH, msg, -10, SpringLayout.NORTH, fileF);
		layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, fileF, 0, SpringLayout.NORTH, label);
		layout.putConstraint(SpringLayout.WEST, fileF, 10, SpringLayout.EAST, label);
		layout.putConstraint(SpringLayout.EAST, fileF, -10, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, btnOK, -16, SpringLayout.HORIZONTAL_CENTER, panel);

		removeAll();
		add(panel);
		setLocationRelativeTo(null);
		validate();
		setVisible(true);
	}

	private void saveList() { // called when closing program, to write mailing list to a file
		setVisible(false);
		setSize(270, 140);
		setTitle("Save list to file");

		SpringLayout layout = new SpringLayout();
		panel = new Panel(layout);

		Label msg = new Label();
		msg.setForeground(Color.RED);
		Label label = new Label("File name");
		TextField fileF = new TextField();

		Button btnOK = new Button("Save & Close");
		Button btnExit = new Button("Resume");

		btnOK.addActionListener(new SaveList(fileF, msg));
		btnExit.addActionListener(new Exit());
		panel.add(msg);
		panel.add(label);
		panel.add(fileF);
		panel.add(btnOK);
		panel.add(btnExit);

		layout.putConstraint(SpringLayout.WEST, msg, -50, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, msg, 50, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.SOUTH, msg, -10, SpringLayout.NORTH, fileF);
		layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.NORTH, fileF, 0, SpringLayout.NORTH, label);
		layout.putConstraint(SpringLayout.WEST, fileF, 10, SpringLayout.EAST, label);
		layout.putConstraint(SpringLayout.EAST, fileF, -10, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.WEST, btnOK, -80, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.EAST, btnOK, -10, SpringLayout.WEST, btnExit);
		layout.putConstraint(SpringLayout.EAST, btnExit, 80, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, btnExit, 0, SpringLayout.NORTH, btnOK);

		removeAll();
		add(panel);
		setLocationRelativeTo(null);
		validate();
		setVisible(true);
	}

	private class SaveList implements ActionListener { // called when "OK" button is pressed on save prompt
		private TextField fileF;
		private Label msg;
		public SaveList(TextField fileF, Label msg) {
			this.fileF = fileF;
			this.msg = msg;
		}
		public void actionPerformed(ActionEvent e) {
			try {
				ArrayList<Entry> entries = mailList.getList();
				PrintWriter outFile = new PrintWriter(fileF.getText());
				for(Entry entCur:entries)
					outFile.printf("%s\n%s\n%s\n", entCur.getName(), entCur.getAddr(), entCur.getPhone());
				outFile.close();
				setVisible(false);
				dispose();
				System.exit(0);
			} catch (IOException g) {
				msg.setText("I/O error");
			}
		}
	}

	private class AddPerson implements ActionListener { // called when "Add a person" button is pressed from main menu
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			setSize(300, 170);
			setTitle(e.getActionCommand());

			Label[] label = {new Label("Name"), new Label("Address"), new Label("Phone")};
			SpringLayout layout = new SpringLayout();
			panel = new Panel(layout);
			TextField nameF = new TextField();
			TextField addrF = new TextField();
			TextField phoneF = new TextField();

			Button btnOK = new Button("OK");
			Button btnExit = new Button("Exit");

			btnOK.addActionListener(new AddOK(nameF, addrF, phoneF));
			btnExit.addActionListener(new Exit());

			panel.add(label[0]);
			panel.add(nameF);
			panel.add(label[1]);
			panel.add(addrF);
			panel.add(label[2]);
			panel.add(phoneF);
			panel.add(btnOK);
			panel.add(btnExit);

			// component positions
			layout.putConstraint(SpringLayout.WEST, label[0], 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[0], 10, SpringLayout.NORTH, panel);
			layout.putConstraint(SpringLayout.WEST, nameF, 0, SpringLayout.WEST, phoneF);
			layout.putConstraint(SpringLayout.NORTH, nameF, 0, SpringLayout.NORTH, label[0]);
			layout.putConstraint(SpringLayout.EAST, nameF, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.WEST, label[1], 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[1], 10, SpringLayout.SOUTH, label[0]);
			layout.putConstraint(SpringLayout.WEST, addrF, 0, SpringLayout.WEST, phoneF);
			layout.putConstraint(SpringLayout.NORTH, addrF, 0, SpringLayout.NORTH, label[1]);
			layout.putConstraint(SpringLayout.EAST, addrF, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.WEST, label[2], 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[2], 10, SpringLayout.SOUTH, label[1]);
			layout.putConstraint(SpringLayout.WEST, phoneF, 10, SpringLayout.EAST, label[1]);
			layout.putConstraint(SpringLayout.NORTH, phoneF, 0, SpringLayout.NORTH, label[2]);
			layout.putConstraint(SpringLayout.EAST, phoneF, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.WEST, btnOK, -50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.SOUTH, label[2]);
			layout.putConstraint(SpringLayout.WEST, btnExit, 10, SpringLayout.EAST, btnOK);
			layout.putConstraint(SpringLayout.EAST, btnExit, 50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, btnExit, 0, SpringLayout.NORTH, btnOK);

			removeAll();
			add(panel);
			setLocationRelativeTo(null);
			validate();
			setVisible(true);
		}
	}

	private class AddOK implements ActionListener { // called when "OK" button is pressed from "Add a person"
		private TextField nameF, addrF, phoneF;
		public AddOK(TextField nameF, TextField addrF, TextField phoneF) {
			this.nameF = nameF;
			this.addrF = addrF;
			this.phoneF = phoneF;
		}
		public void actionPerformed(ActionEvent e) {
			String name = nameF.getText();
			String addr = addrF.getText();
			String phone = phoneF.getText();
			mailList.addPerson(name, addr, phone);
			load();
		}
	}

	private class RemPerson implements ActionListener { // called when "Remove a person" button is pressed from main menu
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			setSize(270, 140);
			setTitle(e.getActionCommand());

			Label msg = new Label();
			msg.setForeground(Color.RED);
			Label label = new Label("Name");
			SpringLayout layout = new SpringLayout();
			panel = new Panel(layout);
			TextField nameF = new TextField();

			Button btnOK = new Button("OK");
			Button btnExit = new Button("Exit");

			btnOK.addActionListener(new RemOK(nameF, msg));
			btnExit.addActionListener(new Exit());

			panel.add(msg);
			panel.add(label);
			panel.add(nameF);
			panel.add(btnOK);
			panel.add(btnExit);

			layout.putConstraint(SpringLayout.WEST, msg, -50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.EAST, msg, 50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, msg, 10, SpringLayout.NORTH, panel);
			layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, nameF);
			layout.putConstraint(SpringLayout.NORTH, nameF, 10, SpringLayout.SOUTH, msg);
			layout.putConstraint(SpringLayout.WEST, nameF, 10, SpringLayout.EAST, label);
			layout.putConstraint(SpringLayout.EAST, nameF, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.WEST, btnOK, -50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.SOUTH, nameF);
			layout.putConstraint(SpringLayout.WEST, btnExit, 10, SpringLayout.EAST, btnOK);
			layout.putConstraint(SpringLayout.EAST, btnExit, 50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, btnExit, 0, SpringLayout.NORTH, btnOK);

			removeAll();
			add(panel);
			setLocationRelativeTo(null);
			validate();
			setVisible(true);
		}
	}

	private class RemOK implements ActionListener { // called when "OK" button is pressed from "Remove a person"
		private TextField nameF;
		private Label msg;
		public RemOK(TextField nameF, Label msg) {
			this.nameF = nameF;
			this.msg = msg;
		}
		public void actionPerformed(ActionEvent e) {
			String search = nameF.getText();
			try {
				mailList.remPerson(search);
				load();
			} catch(SearchFailException f) {
				msg.setText("Person not found");
			}
		}
	}

	private class SearchList implements ActionListener { // called when "Search the list" button is pressed from main menu
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			setSize(270, 300);
			setTitle(e.getActionCommand());

			Label[] label = {new Label("Value to search"), new Label("Field to search"), new Label(""), new Label("Name"), new Label("Address"), new Label("Phone"), new Label(), new Label(), new Label()};
			SpringLayout layout = new SpringLayout();
			panel = new Panel(layout);
			TextField searchF = new TextField();

			Button btnName = new Button("Name");
			Button btnAddr = new Button("Address");
			Button btnPhone = new Button("Phone");
			Button btnExit = new Button("Exit");

			btnName.addActionListener(new Find(searchF, label[6], label[7], label[8], label[2]));
			btnAddr.addActionListener(new Find(searchF, label[6], label[7], label[8], label[2]));
			btnPhone.addActionListener(new Find(searchF, label[6], label[7], label[8], label[2]));
			btnExit.addActionListener(new Exit());

			panel.add(label[0]);
			panel.add(searchF);
			panel.add(label[1]);
			panel.add(btnName);
			panel.add(btnAddr);
			panel.add(btnPhone);
			panel.add(btnExit);
			panel.add(label[2]);
			panel.add(label[3]);
			panel.add(label[4]);
			panel.add(label[5]);
			panel.add(label[6]);
			panel.add(label[7]);
			panel.add(label[8]);

			layout.putConstraint(SpringLayout.WEST, label[0], -45, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, label[0], 10, SpringLayout.NORTH, panel);
			layout.putConstraint(SpringLayout.NORTH, searchF, 10, SpringLayout.SOUTH, label[0]);
			layout.putConstraint(SpringLayout.WEST, searchF, 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.EAST, searchF, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[1], 10, SpringLayout.SOUTH, searchF);
			layout.putConstraint(SpringLayout.WEST, label[1], -42, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, btnName, 10, SpringLayout.SOUTH, label[1]);
			layout.putConstraint(SpringLayout.WEST, btnName, 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, btnAddr, 0, SpringLayout.NORTH, btnName);
			layout.putConstraint(SpringLayout.WEST, btnAddr, 10, SpringLayout.EAST, btnName);
			layout.putConstraint(SpringLayout.NORTH, btnPhone, 0, SpringLayout.NORTH, btnName);
			layout.putConstraint(SpringLayout.EAST, btnPhone, -10, SpringLayout.WEST, btnExit);
			layout.putConstraint(SpringLayout.NORTH, btnExit, 0, SpringLayout.NORTH, btnName);
			layout.putConstraint(SpringLayout.EAST, btnExit, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[2], 10, SpringLayout.SOUTH, btnName);
			layout.putConstraint(SpringLayout.WEST, label[2], -48, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.EAST, label[2], 48, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, label[3], 10, SpringLayout.SOUTH, label[2]);
			layout.putConstraint(SpringLayout.WEST, label[3], 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[6], 0, SpringLayout.NORTH, label[3]);
			layout.putConstraint(SpringLayout.WEST, label[6], 10, SpringLayout.EAST, label[4]);
			layout.putConstraint(SpringLayout.EAST, label[6], 10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[4], 10, SpringLayout.SOUTH, label[3]);
			layout.putConstraint(SpringLayout.WEST, label[4], 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[7], 0, SpringLayout.NORTH, label[4]);
			layout.putConstraint(SpringLayout.WEST, label[7], 10, SpringLayout.EAST, label[4]);
			layout.putConstraint(SpringLayout.EAST, label[7], 10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[5], 10, SpringLayout.SOUTH, label[4]);
			layout.putConstraint(SpringLayout.WEST, label[5], 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, label[8], 0, SpringLayout.NORTH, label[5]);
			layout.putConstraint(SpringLayout.WEST, label[8], 10, SpringLayout.EAST, label[4]);
			layout.putConstraint(SpringLayout.EAST, label[8], 10, SpringLayout.EAST, panel);

			removeAll();
			add(panel);
			setLocationRelativeTo(null);
			validate();
			setVisible(true);
		}
	}

	private class Find implements ActionListener { // called when "OK" button is pressed from "Search the file"
		private TextField searchF;
		private Label nameL, addrL, phoneL, label;
		public Find(TextField searchF, Label nameL, Label addrL, Label phoneL, Label label) {
			this.searchF = searchF;
			this.nameL = nameL;
			this.addrL = addrL;
			this.phoneL = phoneL;
			this.label = label;
		}
		public void actionPerformed(ActionEvent e) {
			String search = searchF.getText();
			char type = e.getActionCommand().toLowerCase().charAt(0);
			try {
				Entry searchResult = mailList.findPerson(search, type);
				label.setForeground(Color.BLACK);
				label.setText("Search result");
				nameL.setText(searchResult.getName());
				addrL.setText(searchResult.getAddr());
				phoneL.setText(searchResult.getPhone());
			} catch(SearchFailException f) {
				label.setForeground(Color.RED);
				label.setText("Person not found");
				nameL.setText("");
				addrL.setText("");
				phoneL.setText("");
			}
		}
	}

	private class ShowList implements ActionListener { // called when "Display the list" button is pressed from main menu
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			setSize(465, 250);
			setTitle(e.getActionCommand());

			Font font = new Font("Lucida Console", Font.PLAIN, 12);
			SpringLayout layout = new SpringLayout();
			ArrayList<Entry> entries = mailList.getList();
			panel = new Panel(layout);

			JTextField header = new JTextField(String.format("%-25s%-20s%15s","Name","Address","Phone"));
			header.setColumns(60);
			header.setEditable(false);
			header.setFont(font);

			String text = "";
			for(Entry entCur:entries) {
				text += String.format("%-25.25s%-20.20s%15.15s\n",entCur.getName(), entCur.getAddr(), entCur.getPhone());
			}
			JTextArea entryText = new JTextArea(text); // vanilla TextArea refused to let me change font... bizarre
			entryText.setEditable(false);
			entryText.setColumns(60);
			entryText.setFont(font);

			JScrollPane scrollPane = new JScrollPane(entryText);
			scrollPane.setColumnHeaderView(header);
			scrollPane.setFont(font);
			panel.add(scrollPane);

			Button btnExit = new Button("OK");
			btnExit.addActionListener(new Exit());
			panel.add(btnExit);

			layout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel);
			layout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.NORTH, btnExit);
			layout.putConstraint(SpringLayout.WEST, btnExit, -16, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.SOUTH, btnExit, -10, SpringLayout.SOUTH, panel);

			removeAll();
			add(panel);
			setLocationRelativeTo(null);
			validate();
			setVisible(true);
		}
	}

	private class GetFileN implements ActionListener { // called when "Load another list from file" button is pressed from main menu
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			setSize(270, 140);
			setTitle(e.getActionCommand());

			SpringLayout layout = new SpringLayout();
			panel = new Panel(layout);

			Label msg = new Label();
			msg.setForeground(Color.RED);
			Label label = new Label("File name");
			TextField fileF = new TextField();

			Button btnOK = new Button("OK");
			Button btnExit = new Button("Exit");

			btnOK.addActionListener(new LoadList(fileF, msg));
			btnExit.addActionListener(new Exit());
			panel.add(msg);
			panel.add(label);
			panel.add(fileF);
			panel.add(btnOK);
			panel.add(btnExit);

			layout.putConstraint(SpringLayout.WEST, msg, -50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.EAST, msg, 50, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.SOUTH, msg, -10, SpringLayout.NORTH, fileF);
			layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.VERTICAL_CENTER, panel);
			layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, panel);
			layout.putConstraint(SpringLayout.NORTH, fileF, 0, SpringLayout.NORTH, label);
			layout.putConstraint(SpringLayout.WEST, fileF, 10, SpringLayout.EAST, label);
			layout.putConstraint(SpringLayout.EAST, fileF, -10, SpringLayout.EAST, panel);
			layout.putConstraint(SpringLayout.NORTH, btnOK, 10, SpringLayout.VERTICAL_CENTER, panel);
			layout.putConstraint(SpringLayout.WEST, btnOK, -35, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.EAST, btnOK, -10, SpringLayout.WEST, btnExit);
			layout.putConstraint(SpringLayout.EAST, btnExit, 35, SpringLayout.HORIZONTAL_CENTER, panel);
			layout.putConstraint(SpringLayout.NORTH, btnExit, 0, SpringLayout.NORTH, btnOK);

			removeAll();
			add(panel);
			setLocationRelativeTo(null);
			validate();
			setVisible(true);
		}
	}

	private class LoadList implements ActionListener { // called when "OK" button is pressed from "Load another list from file" and also from initial file name prompt
		private TextField fileF;
		private Label msg;
		public LoadList(TextField fileF, Label msg) {
			this.fileF = fileF;
			this.msg = msg;
		}
		public void actionPerformed(ActionEvent e) {
			try {
				initList(fileF.getText());
				load();
			} catch (FileNotFoundException f) {
				msg.setText("File not found");
			} catch (IOException g) {
				msg.setText("I/O error");
			}
		}
	}

	private class Exit implements ActionListener { // called to return to main menu any time an "Exit" button is pressed, or "Resume" from SaveList
		public void actionPerformed(ActionEvent e) {
			load();
		}
	}

	public void windowClosing(WindowEvent e) { // called when closing the window--only actually closes before a mailing list has been loaded from a file or when already at a "Save list to file" prompt--otherwise, it goes to the "Save list to file" prompt
		if(mailList == null || getTitle().equals("Save list to file")) {
			setVisible(false);
			dispose();
			System.exit(0);
		} else
			saveList();
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}