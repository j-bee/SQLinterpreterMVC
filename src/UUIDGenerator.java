import java.util.*;

public class UUIDGenerator {
	
	public static String generateUUID() {
		
		String result = UUID.randomUUID().toString();
		return result;
	}
}