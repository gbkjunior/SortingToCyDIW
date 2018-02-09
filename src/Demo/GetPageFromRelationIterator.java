package Demo;

import java.io.File;
import java.nio.ByteBuffer;

import PTCFramework.ProducerIterator;
import StorageManager.Storage;

import storagemanager.StorageConfig;
import storagemanager.StorageUtils;
import cysystem.diwGUI.gui.*;

import cycsx.csxpagination.util.Constant;
import storagemanager.StorageConfig;
import storagemanager.FileStorageManager;
import storagemanager.StorageManagerClient;
import storagemanager.StorageDirectoryDocument;
import storagemanager.StorageManager;

public class GetPageFromRelationIterator implements ProducerIterator<byte []>{
	String filename;
	private StorageManagerClient xmlClient;
	int currentpage;
	int nextpage;
	Storage s;
	private int pagesize;
	StorageConfig conf;
	StorageUtils su;
	StorageManager xmlSto;
	
	public GetPageFromRelationIterator(String filename, int currentpage){
		this.filename = filename;
		this.nextpage = currentpage;
	}

	@Override
	public boolean hasNext() {
		if(nextpage!=-1){
			currentpage = nextpage;
			return true;
		}
		return false;
	}

	@Override
	public byte[] next() {
		byte[] buffer = new byte[pagesize];
		try {
			//s.ReadPage(currentpage, buffer);
			buffer = su.getXmlClient().readPagewithoutPin(currentpage);
			byte[] temp = new byte[4];
			for(int i=0; i<4; i++){
				temp[i] = buffer[i+4];
			}
			nextpage = ByteBuffer.wrap(temp).getInt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}

	@Override
	public void open() throws Exception {
		//s = new Storage();
		
		
		//s.LoadStorage(filename);
		StorageUtils su = new StorageUtils();
		String fileName ="StorageConfig.xml" ;
	    File fStorageConfig = new File(fileName);
	     
	     if (!fStorageConfig.exists()) {
	    	 System.out.println("Please make sure storage config exists");
	     }
	  // Please remember to set storageConfig first before createStorage and useStorage   
	     StorageConfig temp = new StorageConfig(fStorageConfig);
	     su.loadStorage(temp);
		this.pagesize = temp.getPageSize();
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}