package database;

public class LogModel {

	private long id;
	private String date;
	private String comment;
	
	public LogModel(){}
	
	public LogModel(long id, String date, String comment){
		this.id = id;
		this.date = date;
		this.comment = comment;		
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getComment(){
		return comment;
	}
	
	@Override
	public String toString(){
		return id+". "+date+": "+comment;
	}
	
}
