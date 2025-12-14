package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;
  String currentUser = null;


	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
  JMenuItem mnuItemUpdatePrio;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
  

	public Tickets(Boolean isAdmin, String username) {

		chkIfAdmin = isAdmin;
    currentUser = username;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

    // initialize third sub menu items for Admin main menu
    mnuItemUpdatePrio = new JMenuItem("Update Ticket Priority");
    // add to Admin main menu item
    mnuAdmin.add(mnuItemUpdatePrio);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);


		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
    mnuItemUpdatePrio.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
    

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above
		 */

 
	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
    } 

    else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
      String ticketerGender = JOptionPane.showInputDialog(null, "Enter your gender");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
      String[] priorityOptions = {"Low", "Medium", "High"};
      String ticketPriority = JOptionPane.showInputDialog(null, "Enter percieved ticket priority (Low, Medium, Or High)", priorityOptions[0]);

			// insert ticket information to database

			int id = dao.insertRecords(ticketName, ticketerGender, ticketDesc, ticketPriority);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
		}

		else if (e.getSource() == mnuItemViewTicket) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, currentUser)));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

    else if (e.getSource() == mnuItemUpdate) {
      String ticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to update:");
      String newDesc = JOptionPane.showInputDialog(null, "Enter new Ticket Description:");
      
      if (ticketID != null && newDesc != null) {

        if (chkIfAdmin) {
          try {
            dao.updateRecords(Integer.parseInt(ticketID), newDesc, chkIfAdmin);
            JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketID + " updated successfully.");
          }
          
          catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Invalid Ticket ID.");
          }
        }

        else {
          JOptionPane.showMessageDialog(null, "You dont have permission to update tickets.");
        }
      }
    }

    else if (e.getSource() == mnuItemUpdatePrio) {
      String ticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to update:");
      String newPrio = JOptionPane.showInputDialog(null, "Enter Ticket Priority:");
      
      if (ticketID != null && newPrio != null) {

        if (chkIfAdmin) {
          try {
            dao.updatePriority(Integer.parseInt(ticketID), newPrio, chkIfAdmin);
            JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketID + " updated successfully.");
          }
          
          catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Invalid Ticket ID.");
          }
        }

        else {
          JOptionPane.showMessageDialog(null, "You dont have permission to update ticket Priority.");
        }
      }
    }

    
      


		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */

	}

}
