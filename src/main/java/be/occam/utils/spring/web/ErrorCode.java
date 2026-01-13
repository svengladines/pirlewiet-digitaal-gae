package be.occam.utils.spring.web;

public class ErrorCode {
	
	protected String code;
	
	public ErrorCode(String code ) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equal
			= obj instanceof ErrorCode;
		
		if ( equal ) {
			equal = this.code.equals( ((ErrorCode) obj).getCode() );
		}
		return equal;
		
	}


}
