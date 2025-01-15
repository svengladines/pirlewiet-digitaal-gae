package be.pirlewiet.digitaal.domain.people;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeMan {
	
	protected final String[] letters =
		{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	
	protected final String[] numbers
		= {"0","1","2","3","4","5","6","7","8","9"};
	
	public String generateCode() {
		
		StringBuilder b
			= new StringBuilder();
		
		b.append( letters[ new Random().nextInt( letters.length )] );
		b.append( letters[ new Random().nextInt( letters.length )] );
		b.append( letters[ new Random().nextInt( letters.length )] );
		
		b.append( numbers[ new Random().nextInt( numbers.length )] );
		b.append( numbers[ new Random().nextInt( numbers.length )] );
		b.append( numbers[ new Random().nextInt( numbers.length )] );
		
		return b.toString();
		
	}

}
