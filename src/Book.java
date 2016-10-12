import java.io.*;

public class Book implements Serializable {

	private int id = -1;
	private String title = "title unknown";
	private String author = "author unknown";;
    private float price = -1;
	private int qty = -1;
	
	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public float getPrice() {
		return price;
	}

	public int getQty() {
		return qty;
	}

	public int getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
