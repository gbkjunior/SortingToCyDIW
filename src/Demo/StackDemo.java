package Demo;
import java.util.*;
public class StackDemo {
//public static Stack a = new Stack<byte[]>();
	public static void pushStackElements(Stack a, byte[] b)
	{
		
		a.push(new byte[35]);
	}
	public static Stack returnStack(Stack a)
	{
		return a;
	}
	public static int stackSize(Stack a)
	{
		return a.size();
	}
}
