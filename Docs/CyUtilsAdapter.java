package cyutils.parser;

import cysystem.clientsmanager.ClientsFactory;
import cysystem.clientsmanager.CyGUI;
import cysystem.diwGUI.gui.DBGui;
import cyutils.btree.BTree;
import cyutils.btree.BTreeTupleGenerator;
import cyutils.btree.PrepareSortedData;
import cyutils.btree.ReWriUtils;
import cyutils.btree.Tuple;
import cyutils.btree.TupleAttribute;
import storagemanager.StorageUtils;


public class CyUtilsAdapter extends ClientsFactory {
	private StorageUtils stoUtils;
	
	public void initialize(CyGUI gui, int clientID) {
		this.dbgui = gui;
		this.stoUtils = ((DBGui) dbgui).getStorageUtils();
	}
	
	public void execute(int clientID, String text) {
		if (this.dbgui == null) {
			System.out.println("Error! The client parser is not initialized properly."
					+ " The handle to CyDIW GUI is not initialized.");
			return;
		}
		
		text = text.trim();
		String[] commands = text.split(" ");
		
		// List Commands
		if (commands[0].equalsIgnoreCase("list") && commands[1].equalsIgnoreCase("commands")) {
			
			dbgui.addOutputPlainText("$Cyutils Commands List:");
			dbgui.addOutputPlainText("$Cyutils:> list commands;");
			dbgui.addOutputPlainText("$Cyutils:> BTreePersistent;");
			dbgui.addOutputPlainText("$Cyutils:> BTreeBuildNew 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
			dbgui.addOutputPlainText("$Cyutils:> BTreeShowRootSequencePageId 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
			dbgui.addOutputPlainText("$Cyutils:> BTreePrepareSortedData <TupleConfigXmlFile> <TupleTxtFile>;");
			dbgui.addOutputPlainText("$Cyutils:> BTreeBulkLoad 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile> <TupleTxtFile>;");
			dbgui.addOutputPlainText("$Cyutils:> BTreeInsert 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile> $OneTuple;");
			dbgui.addOutputPlainText("$Cyutils:> BTreeSequenceScan 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
			
		} else if (commands[0].equalsIgnoreCase("BTreeBuildNew")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreeBuildNew 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
			
			if (commands.length != 4) {
				dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
			} else {
				
				String btreeName = commands[1];
				String btreeConfigFile = commands[2];
				String tupleConfigFile = commands[3];
				
				Tuple tupleDefinition = new Tuple(tupleConfigFile);
				BTree btree = new BTree(this.stoUtils, btreeName, tupleDefinition);
				btree.initializeBTreeFromEmpty(btreeConfigFile);

				dbgui.addOutputPlainText("Successfully build a new btree, root page id is: " 
				+ String.valueOf(btree.getRootPageId()) + ", sequence page id is: " 
						+ String.valueOf(btree.getSequencePageId()));
				
				btree.restoreBTreeToXml(btreeConfigFile);
			}
			
		} else if (commands[0].equalsIgnoreCase("BTreePersistent")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreePersistent;");
			
			if (commands.length != 1) {
				dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
			} else {
				this.stoUtils.setStoragePersistent(true);
			}
			
		} else if (commands[0].equalsIgnoreCase("BTreeShowRootSequencePageId")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreeShowRootSequencePageId 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
			
			if (commands.length != 4) {
				dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
			} else {
				
				String btreeName = commands[1];
				String btreeConfigFile = commands[2];
				String tupleConfigFile = commands[3];
				
				Tuple tupleDefinition = new Tuple(tupleConfigFile);
				BTree btree = new BTree(this.stoUtils, btreeName, tupleDefinition);
				btree.initializeBTreeFromXml(btreeConfigFile);

				dbgui.addOutputPlainText("BTree root page id is: " + String.valueOf(btree.getRootPageId()));
				dbgui.addOutputPlainText("BTree sequence page id is: " + String.valueOf(btree.getSequencePageId()));
				
				btree.restoreBTreeToXml(btreeConfigFile);
			}
			
		} else if (commands[0].equalsIgnoreCase("BTreePrepareSortedData")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreePrepareSortedData <TupleConfigXmlFile> <TupleTxtFile>;");
			
			if (commands.length != 3) {
				dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
			} else {
				
				String tupleConfigFile = commands[1];
				String tupleDataFile = commands[2];
				
				try {
					PrepareSortedData.prepare(tupleConfigFile, tupleDataFile);
				} catch (Exception e) {
					dbgui.addOutputPlainText("IO Exception during preparing sorted data for btree");
				}
			}
			
		} else if (commands[0].equalsIgnoreCase("BTreeBulkLoad")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreeBulkLoad 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile> <TupleTxtFile>;");
			
			if (commands.length != 5) {
				dbgui.addOutputPlainText("Lack of command parameters, type 'list commands' for reference");
			} else {
				
				String btreeName = commands[1];
				String btreeConfigFile = commands[2];
				String tupleConfigFile = commands[3];
				String tupleDataFile = commands[4];
				
				Tuple tupleDefinition = new Tuple(tupleConfigFile);
				BTree btree = new BTree(this.stoUtils, btreeName, tupleDefinition);
				btree.initializeBTreeFromXml(btreeConfigFile);
				
				BTreeTupleGenerator tupleGenerator = new BTreeTupleGenerator(tupleDataFile, tupleDefinition);
				btree.BTreeBulkLoading(tupleGenerator);
				
				dbgui.addOutputPlainText("Successfully bulkloaded sorted data into storage!");
				
				btree.restoreBTreeToXml(btreeConfigFile);
			}
			
		} else if (commands[0].equalsIgnoreCase("BTreeInsert")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreeInsert 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile> $OneTuple;");
			
			if (commands.length != 5) {
				dbgui.addOutputPlainText("Wrong command parameters, type 'list commands' for reference");
			} else {
				
				String btreeName = commands[1];
				String btreeConfigFile = commands[2];
				String tupleConfigFile = commands[3];
				String tupleData = commands[4];
				
				Tuple tupleDefinition = new Tuple(tupleConfigFile);
				BTree btree = new BTree(this.stoUtils, btreeName, tupleDefinition);
				btree.initializeBTreeFromXml(btreeConfigFile);
				String currentContent = tupleData.substring(1, tupleData.length()-1);
				String[] contentArray = currentContent.split(",");
				
				if(tupleDefinition.getAttributeNum() != contentArray.length){
					dbgui.addOutputPlainText("Tuple format error!");
					
				} else {
					// currentLine -> buffer
					byte[] tuple = new byte[tupleDefinition.getLength()];
					int offset = 0;
					int count = 0;
					for(TupleAttribute tupleAttribute : tupleDefinition.getTupleAttributes()){
						String attrType = tupleAttribute.getType();
						int attrLen = tupleAttribute.getLength();
						if(attrType.equals("Integer") && attrLen==4){
							// if it is an integer
							ReWriUtils.intToByteArray(Integer.parseInt(contentArray[count].trim()), tuple, offset);
						}else if(attrType.equals("String")){
							// if it is a string
							ReWriUtils.stringToByteArray(contentArray[count], tuple, offset, offset+attrLen);
						}
						count++;
						offset += attrLen;
					}
					btree.BTreeInsert(btree.getRootPageId(), tuple);
					dbgui.addOutputPlainText("Successfully insert this tuple!");
				}
				
				btree.restoreBTreeToXml(btreeConfigFile);
			}
			
		} else if (commands[0].equalsIgnoreCase("BTreeSequenceScan")) {
			
			dbgui.addOutputPlainText("$Cyutils:> BTreeSequenceScan 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
			
			if (commands.length != 4) {
				dbgui.addOutputPlainText("Lack of command parameters, type 'list commands' for reference");
			} else {

				String btreeName = commands[1];
				String btreeConfigFile = commands[2];
				String tupleConfigFile = commands[3];
				
				Tuple tupleDefinition = new Tuple(tupleConfigFile);
				BTree btree = new BTree(this.stoUtils, btreeName, tupleDefinition);
				btree.initializeBTreeFromXml(btreeConfigFile);
				
				int leafpageId = btree.getSequencePageId();
				while(leafpageId != -1){
					dbgui.addOutputPlainText("Print Leaf PageId " + String.valueOf(leafpageId) + " in console!");
					leafpageId = btree.printAllTuples(leafpageId);
				}
			}
			
			
		} 
			// List Commands for sorter 
		
			else if(commands[0].equalsIgnoreCase("list") && commands[1].equalsIgnoreCase("commands") && commands[2].equalsIgnoreCase("sorter")) {
					
					dbgui.addOutputPlainText("$Cyutils Commands List Sorter:");
					dbgui.addOutputPlainText("$Cyutils:> list commands sorter;");
					dbgui.addOutputPlainText("$Cyutils:> GenerateTextData cat.xml;");
					dbgui.addOutputPlainText("$Cyutils:> LoadStorageFile abc");
					dbgui.addOutputPlainText("$Cyutils:> SortData abc cde");
					//dbgui.addOutputPlainText("$Cyutils:> BTreePrepareSortedData <TupleConfigXmlFile> <TupleTxtFile>;");
					//dbgui.addOutputPlainText("$Cyutils:> BTreeBulkLoad 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile> <TupleTxtFile>;");
					//dbgui.addOutputPlainText("$Cyutils:> BTreeInsert 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile> $OneTuple;");
					//dbgui.addOutputPlainText("$Cyutils:> BTreeSequenceScan 'BTreeName' <BTreeConfigXmlFile> <TupleConfigXmlFile>;");
					
				}
				
		
		else {
			dbgui.addConsoleMessage("Wrong use of commands, type 'list commands' for reference");
		}
			
	} // end for execute method
	
	public static void main(String [] args)
	{
		CyUtilsAdapter cya = new CyUtilsAdapter();
		
	}
}
