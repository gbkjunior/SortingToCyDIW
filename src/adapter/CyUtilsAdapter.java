
package adapter;
//import StorageManager.Storage;
import cysystem.clientsmanager.ClientsFactory;
import cysystem.clientsmanager.CyGUI;
import cysystem.diwGUI.gui.DBGui;
//import cyutils.btree.BTree;
import StorageManager.Storage;
import storagemanager.StorageUtils;


public class CyUtilsAdapter extends ClientsFactory {
	//private StorageUtils stoUtils;
	private Storage simpleStorage;
	
	public void initialize(CyGUI gui, int clientID) {
		//this.stoUtils = ((DBGui) dbgui).getStorageUtils();
		this.dbgui = gui;
		this.simpleStorage = new Storage((DBGui) dbgui);
	
	}
	
	public void execute(int clientID, String text) {
		if (this.dbgui == null) {
			System.out.println("Error! The client parser is not initialized properly."
					+ " The handle to CyDIW GUI is not initialized.");
			return;
		}
		
		text = text.trim();
		String[] commands = text.split(" ");
		System.out.println(commands[0] + " " + commands[1]);
		// List Commands
		
			// List Commands for sorter 
		
		 if(commands[0].equalsIgnoreCase("list") && commands[1].equalsIgnoreCase("commands") && commands[2].equalsIgnoreCase("simple") && commands[2].equalsIgnoreCase("storage")) {
					
					dbgui.addOutputPlainText("$Cyutils Commands List Simple Storage:");
					dbgui.addOutputPlainText("$Cyutils:> CreateSimpleStorage filename pagesize fileszize;");
					dbgui.addOutputPlainText("$Cyutils:> OpenSimpleStorage filename");
					dbgui.addOutputPlainText("$Cyutils:> PrintStats");
					dbgui.addOutputPlainText("$Cyutils:> CloseSimpleStorage");
					
				}
				else if (commands[0].equalsIgnoreCase("CreateSimpleStorage")) {
					
					dbgui.addOutputPlainText("$CyUtils:> CreateSimpleStorage <SimpleStorageName> <SimpleStoragePageSize> <SimpleStorageFileSize>;");
					
					if (commands.length != 4) {
						dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
					} else {
						
						String simpleStorageFileName = dbgui.getVariableValue(commands[1].trim().substring(2));
						String simpleStoragePageSize = dbgui.getVariableValue(commands[2].trim().substring(2));;
						String simpleStorageFileSize = dbgui.getVariableValue(commands[3].trim().substring(2));;

						int storagePageSize = Integer.parseInt(simpleStoragePageSize);
						int storageFileSize = Integer.parseInt(simpleStorageFileSize);
						//Storage s1 = new Storage();
						try {
							simpleStorage.CreateStorage(simpleStorageFileName, storagePageSize, storageFileSize);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


						dbgui.addOutputPlainText("Successfully created a simple storage. Page size is: " 
						+ String.valueOf(storagePageSize) + ", Storage size is: " 
								+ String.valueOf(storageFileSize));
						

					}
				}
					else if (commands[0].equalsIgnoreCase("OpenSimpleStorage")) {
						
						dbgui.addOutputPlainText("$CyUtils:> OpenSimpleStorage <SimpleStorageName> ;");
						
						if (commands.length != 2) {
							dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
						} else {
							
							String simpleStorageFileName = dbgui.getVariableValue(commands[1].trim().substring(2));
							
							//Storage s1 = new Storage();
							try {
								simpleStorage.LoadStorage(simpleStorageFileName);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


							dbgui.addOutputPlainText("Successfully opened" + simpleStorageFileName +  "simple storage.");
							

						}		
		
	//	else {
	//		dbgui.addConsoleMessage("Wrong use of commands, type 'list commands' for reference");
		}
			
	}   // end for execute method


}
