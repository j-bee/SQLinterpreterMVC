import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectionProvider {

private DataSource dataSource;
	
	public ConnectionProvider (DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	public Connection provideConnection() {
		
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
