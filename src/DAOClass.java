import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.sql.*;

public class DAOClass {

	private ConnectionProvider cprov;
	private Connection connection;
	private ResultSet rset;
	private Statement stmt;
	private PreparedStatement insertUserStmt;
	private PreparedStatement verifyUserStmt;
	private PreparedStatement confirmUserUUIDStmt;
	private PreparedStatement activateUserStmt;
	private PreparedStatement isAlreadyConfirmedStmt;
	private PreparedStatement deleteUserStmt;
	private ScheduledExecutorService actLinkDestroyer;
	private ScheduledFuture nextTask;
	private String errorMsg;
	
	public DAOClass (ConnectionProvider cprov) {
		this.cprov = cprov;
		connection = cprov.provideConnection();
		
		try {
		insertUserStmt = connection.prepareStatement
		("INSERT INTO users (username, email, password, mailConfUUID, activated) values (?, ?, ?, ?, ?)");
		verifyUserStmt = connection.prepareStatement
		("SELECT password, activated FROM users WHERE username = ?");

		confirmUserUUIDStmt = connection.prepareStatement
		("SELECT mailConfUUID FROM users WHERE username = ?");
		
		isAlreadyConfirmedStmt = connection.prepareStatement
		("SELECT activated FROM users WHERE username = ?");
		
		activateUserStmt = connection.prepareStatement
		("UPDATE users SET activated=1 WHERE username = ?");
		
		deleteUserStmt = connection.prepareStatement
		("DELETE FROM users WHERE username = ?"); 
		
		} catch (SQLException e) {
			errorMsg = e.getMessage();
		}
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String[] getQueryParams(String sqlquery) throws SQLException {
		
		sqlquery = sqlquery.toLowerCase();

		int beginIndex = sqlquery.lastIndexOf("select")+6;
		int endIndex = sqlquery.lastIndexOf("from");
		if(beginIndex == -1 || endIndex == -1) throw new SQLException();
		
		for (int i = beginIndex; i<endIndex;i++) {
			if(sqlquery.charAt(i)=='*') {int astIndex = i;
				if(sqlquery.charAt(astIndex-1)!='(' && sqlquery.charAt(astIndex+1)!=')') {
				sqlquery = sqlquery.substring(0, astIndex)+"id, title, author, price, qty" + sqlquery.substring(astIndex+1);
				endIndex = sqlquery.lastIndexOf("from");
				}
			}
		}
		
		String[] columns = sqlquery.substring(beginIndex, endIndex).split(",");
		String[] result = new String[columns.length];
				
		for (int i = 0; i<columns.length;i++) {
			String s = columns[i];
			s = s.replaceAll("\\s+", "");
			result[i] = s;
		}
		
		return result;
	}
	
	public Boolean isSelect(String sqlquery) {
		
			Boolean isSelectQuery = false; 
		try {
			stmt = connection.createStatement();
			isSelectQuery =  stmt.execute(sqlquery);
			if(isSelectQuery) rset = stmt.getResultSet();
				
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			return null;
		}
		
		return isSelectQuery; 
	}
	
	public int getAffectedRowsCount() {
		
		int result;
		try {
		result = stmt.getUpdateCount();	
		} catch (SQLException e) {
			return -1;
		}
		
		return result;
	}
	
	
	public List<Book> getBookList (String[] columns, String sqlquery) {
		
		 List<Book> books = new ArrayList<Book>();
		
			try {
			while(rset.next()) {
				Book book = new Book();
				
				for(int i = 0; i<columns.length; i++) {
				if(columns[i].equals("id")) book.setId(rset.getInt("id"));
				if(columns[i].equals("title")) book.setTitle(rset.getString("title"));
				if(columns[i].equals("author")) book.setAuthor(rset.getString("author"));
				if(columns[i].equals("price")) book.setPrice(rset.getFloat("price"));
				if(columns[i].equals("qty")) book.setQty(rset.getInt("qty"));
				}
				books.add(book);
			}
		} catch (SQLException e) {
			return null;
		}
			
		return books;
	}
	
	public String confirmUser(String username, String uuid) {
		
		ResultSet rset = null;
		String result = null;
		boolean isActive;
		
		//jeśli kliknie link aktywacyjny - zatrzymaj linkDestroyer
		
		try {
			
			isAlreadyConfirmedStmt.setString(1, username);
			rset = isAlreadyConfirmedStmt.executeQuery();
			
			if(rset.next()) {
			isActive = rset.getBoolean("activated");
			if(isActive) throw new SQLException("User has already confirmed his registration.");
			}
			
			confirmUserUUIDStmt.setString(1, username);
			rset = confirmUserUUIDStmt.executeQuery();
			
			if(rset.next()) {
			result = rset.getString("mailConfUUID");
			
			if(!uuid.equals(result))
			throw new SQLException("Error: conflicting IDs. Make sure you opened the correct activation email.");
		
			else {
				activateUserStmt.setString(1, username);
				activateUserStmt.executeUpdate();
			  }
			}//end first if
			
			else throw new SQLException("Error: no such user exists in the database. Perhaps your activation link has expired. Please register again.");
		
		} catch (SQLException e) {
			return e.getMessage();
		}
	

		
		return "OK";
	}
	
	
	public String insertNewUser(String username, String email, String password, String uuid) {

		boolean usernameOK = validateUsername(username);
		boolean emailOK = validateEmail(email);
		boolean passwordOK = validatePassword(password);
		
		if(!usernameOK) return "Invalid username: " + username;
		if(!emailOK) return "Invalid email." + email;
		if(!passwordOK) return "Invalid password.";
		
		try {
			insertUserStmt.setString(1, username);
			insertUserStmt.setString(2, email);
			insertUserStmt.setString(3, password);
			insertUserStmt.setString(4, uuid);
			insertUserStmt.setBoolean(5, false);
			insertUserStmt.executeUpdate();
				
		} catch (SQLException e) {
			errorMsg = e.getMessage();
			return errorMsg;
		}	
		return "OK"; 
		
	}
	
	public static boolean validatePassword(String password) {
		
		if(password.length()<6) return false;

		Pattern smallLet = Pattern.compile("[a-z]");
		Pattern capitalLet = Pattern.compile("[A-Z]");
		Pattern num = Pattern.compile("[0-9]");
		Pattern specialChars = Pattern.compile("[&-._!\"'#$*+,/:;<=>?@\\[\\]\\\\`^{}~()]");
		
		Matcher smallLetMatcher = smallLet.matcher(password);
		Matcher capitalLetMatcher =  capitalLet.matcher(password);
		Matcher numMatcher = num.matcher(password);
		Matcher specialCharsMatcher = specialChars.matcher(password);
		
		return (smallLetMatcher.find() && capitalLetMatcher.find() && numMatcher.find()
		&& specialCharsMatcher.find());
	}

	public static boolean validateEmail(String email) {

		int atIndex = email.indexOf("@");
		int dotIndex = email.lastIndexOf(".");
		if(atIndex < 1 || dotIndex < atIndex + 2 || atIndex < 0 || dotIndex < 0) {
			return false;}

		return true;
	}

	public static boolean validateUsername(String username) {

		return username.length() >= 1;
	}
	
	public String loginUser(String username, String password) {
		
		ResultSet passwordRes = null;
		String result = null;
		boolean isActive;
		
		try {
			verifyUserStmt.setString(1, username);
			passwordRes = verifyUserStmt.executeQuery();
			
			if(passwordRes.next()) {
			result = passwordRes.getString("password");
			
			if(!password.equals(result))
			throw new SQLException("Username and password don't match.");
		
			isActive = passwordRes.getBoolean("activated");
			if(!isActive) throw new SQLException("You must activate your account first.");
			}
			
			else throw new SQLException("No such user is registered.");
		
		} catch (SQLException e) {
			return e.getMessage();
		}	
		return "OK"; 
		
	}//end loginUser()
	
	
	//deaktywacja linku	
	public ScheduledExecutorService initAndRunLinkDestroyer(String username) {
	
	Runnable destroyActivationLink = new Runnable() {
	
		public void run() {
			try {
				
			deleteUserStmt.setString(1, username);
			deleteUserStmt.executeUpdate();	
	
		} catch (SQLException e) {}	
		
	  }
	};

	actLinkDestroyer = Executors.newSingleThreadScheduledExecutor();
	nextTask = actLinkDestroyer.schedule(
	destroyActivationLink, 25, TimeUnit.SECONDS);
	
	return actLinkDestroyer;
	} 


	
}
	

