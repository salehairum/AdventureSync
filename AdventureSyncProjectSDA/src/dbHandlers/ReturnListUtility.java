package dbHandlers;

import java.util.HashMap;

public class ReturnListUtility<T> {
	HashMap<Integer, T> list;
	String message;
	boolean success;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public HashMap<Integer, T> getList() {
		return list;
	}
	public void setList(HashMap<Integer, T> list) {
		this.list = list;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
