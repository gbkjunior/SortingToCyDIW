package Demo;

import PTCFramework.ConsumerIterator;
import PTCFramework.PTCFramework;
import PTCFramework.ProducerIterator;

public class ProcessRelation1{
	public static void main(String[] args) throws Exception{
		
		ProducerIterator<byte []> relationProducerIterator= new GetTupleFromRelationIterator("myDisk35", 35, 0);
		ConsumerIterator<byte []> consumerIterator= new PutTupleInRelationIterator(31,"myDisk35");
		PTCFramework<byte[], byte[]>relationToRelationFramework= new RelationToRelationPTC(relationProducerIterator, consumerIterator);
		
		relationToRelationFramework.run();
		
	}
	
}