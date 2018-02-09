package Demo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import PTCFramework.ConsumerIterator;
import PTCFramework.PTCFramework;
import PTCFramework.ProducerIterator;
import StorageManager.Storage;
import Tuple.Tuple;
import storagemanager.StorageUtils;
import cysystem.diwGUI.gui.*;

import cycsx.csxpagination.util.Constant;
import storagemanager.StorageConfig;
import storagemanager.FileStorageManager;
import storagemanager.StorageManagerClient;
import storagemanager.StorageDirectoryDocument;
import storagemanager.StorageManager;

public class TestPages{
	static StorageManagerClient xmlClient;
	public static void main(String[] args) throws Exception{		
		long startTime = System.nanoTime();
		
		//Storage s1 = new Storage();
		StorageUtils su = new StorageUtils();
		StorageManager xmlSto;
		String fileName ="StorageConfig.xml" ;
	    File fStorageConfig = new File(fileName);
	     
	     if (!fStorageConfig.exists()) {
	    	 System.out.println("Please make sure storage config exists");
	     }
	  // Please remember to set storageConfig first before createStorage and useStorage   
	     StorageConfig temp = new StorageConfig(fStorageConfig);
	     su.setStorageConfig(temp);  //xmlStorageConfig has been set, the following part can use xmlStorageConfig   
//	     boolean ret = utils.createStorage(temp, true);
	     boolean ret = su.createStorage(temp, true);
	    // StorageManagerClient xmlClient = new StorageManagerClient(xmlSto);

	     System.out.println(ret);
		 //s1.CreateStorage("myDisk35", 16*1024, 1024*2500);
		
		
		ProducerIterator<byte []> textFileProducerIterator= new TextFileScanIterator();
		ConsumerIterator<byte []> relationConsumerIterator = new PutTupleInRelationIterator(35,"myDisk35");

		PTCFramework<byte[],byte[]> fileToRelationFramework= new TextFileToRelationPTC(textFileProducerIterator, relationConsumerIterator);
		fileToRelationFramework.run();
	     int numPages = su.getXmlClient().getNumOfPages();
			
		System.out.println("Number of Pages from CyDIW Storage: " + numPages);
		Tuple t = new Tuple();

		/*int numPages = xmlClient.getNumOfPages();
		
		System.out.println("Number of Pages from CyDIW Storage: " + numPages);*/
		
		GetPageFromRelationIterator getpagefromrelationiter = new GetPageFromRelationIterator("myDisk35",0);
		getpagefromrelationiter.open();
		
		relationConsumerIterator = new PutTupleInRelationIterator(35,"myDisk35");
		relationConsumerIterator.open();
		
		while(getpagefromrelationiter.hasNext()){
			List<Bytenode> byteList = new ArrayList<Bytenode>();
			byte[] page = getpagefromrelationiter.next();
			byte[] getcount = new byte[4];
			for(int i=0; i<4; i++){
				getcount[i] = page[i];
			}
			int count = ByteBuffer.wrap(getcount).getInt();
			int bytesread = 8;
			
			for(int i=0 ; i<count; i++){
				byte[] val = new byte[35];
				
				for(int j=0; j<35; j++){
					val[j] = page[bytesread+j];
				}
				
				byte[] key = val;
				
				bytesread = bytesread + 35;
				Bytenode bytenode = new Bytenode(key,val);
				byteList.add(bytenode);
			}
			
			byteList.sort(new Comparator<Bytenode>(){

				@Override
				public int compare(Bytenode o1, Bytenode o2) {
					byte[] keyo1 = o1.key;
					byte[] keyo2 = o2.key;
					System.out.println("Keys 0 and 1:" + keyo1 + "" + keyo2);
					return t.compare(keyo1, keyo2);
					
				}
				
			});
			
			for(Bytenode e : byteList){
				byte[] fill = e.val;
				relationConsumerIterator.next(fill);
			} 
		} 
		
		CreateRuns proc = new CreateRuns(3,numPages,numPages);
		
		GetTupleFromRelationIterator iter = new GetTupleFromRelationIterator("myDisk35",35, proc.getLastSortPage(numPages));
		iter.open();
		PrintStream out = new PrintStream(new FileOutputStream("C:/Users/vijay/workspace/SortingToCyDiw/src/Demo/output.txt"));
		System.setOut(out);
		Stack a = new Stack<byte[]>();
		while(iter.hasNext()) {
			byte [] tuple = iter.next();
			//StackDemo.pushStackElements(a,tuple);
			//out.println(StackDemo.returnStack(a));
			//System.out.print(StackDemo.stackSize(a));
			out.println(new String(toInt(tuple, 0)+", "+new String(tuple).substring(4, 27)+", "+ new String(tuple).substring(27,31)+", "+ toInt(tuple, 31)));
		}  
		
		long endTime = System.nanoTime();
		System.out.println("Took "+(endTime - startTime) + " ns"); 
	} 
	
	private static int toInt(byte[] bytes, int offset) {
		  int ret = 0;
		  for (int i=0; i<4; i++) {
		    ret <<= 8;
		    ret |= (int)bytes[offset+i] & 0xFF;
		  }
		  return ret;
		}
}

class Bytenode{
	byte[] key;
	byte[] val;
	
	public Bytenode(byte[] key, byte[] val){
		this.key = key;
		this.val = val;
	}
}