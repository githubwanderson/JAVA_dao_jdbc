package db;

public class dBIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public dBIntegrityException(String msg) {
		super(msg);
	}	

}
