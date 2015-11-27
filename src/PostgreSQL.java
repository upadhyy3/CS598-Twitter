import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;

public class PostgreSQL {
	
	private final String url = "jdbc:postgresql://127.0.0.1:5432/testdb";
	private final String user = "chanon";
	private final String password = "";
	
	private Connection connection;

	public PostgreSQL (){
		//System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
			return;
		}

		//System.out.println("PostgreSQL JDBC Driver Registered!");

		this.connection = null;

		try {
			this.connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (this.connection != null) {
			//System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	private String removeEmojiAndSymbolFromString(String content){
		String resultStr = "";
	    String utf8tweet = "";
	    
	    try {
	        byte[] utf8Bytes = content.getBytes("UTF-8");
	        utf8tweet = new String(utf8Bytes, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    
	    Pattern unicodeOutliers =
	        Pattern.compile(
	            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
	            Pattern.UNICODE_CASE |
	            Pattern.CANON_EQ |
	            Pattern.CASE_INSENSITIVE
	        );
	    Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8tweet);
	    utf8tweet = unicodeOutlierMatcher.replaceAll(" ");
	    
	    resultStr = utf8tweet.replaceAll("'", "''");
	    
	    return resultStr;
	}
	
	public void importTweetsFromFile (String targetDir){
		Statement st;
		File dir = new File(targetDir);
		File[] files = dir.listFiles();
		System.out.println(files.length+"");
		for (File f : files) {
			if(f.isFile() && !".DS_Store".equals(f.getName())){
				try (BufferedReader br = new BufferedReader(new FileReader(f))) {
					String line;
				    while ((line = br.readLine()) != null) {
				    	line = removeEmojiAndSymbolFromString(line);
				    	String tweet = line.substring(line.indexOf(",")+1);
				    	String restaurant_name = line.substring(0, line.indexOf(","));
				    	st = connection.createStatement();
				    	String updateSQLStr = "insert into tweets (tweet,restaurant_name) values('" + tweet + "','"
				    							+ restaurant_name + "');";
				    	System.out.println(updateSQLStr);
						st.executeUpdate(updateSQLStr);
				   }
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public ResultSet selectUnlabeledTweet(){
		Statement st;
		ResultSet rs = null;
		String sqlStr = "select * from tweets;";
		
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sqlStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
	public void insertLabeledTweet(JsonObject json, String restaurant_name){
		Statement st;
		String sqlStr = "insert into tweets_labeled (tweet,restaurant_name) values('" + removeEmojiAndSymbolFromString(json.toString()) + "','"
							+ restaurant_name + "');";
		try {
			st = connection.createStatement();
			st.executeUpdate(sqlStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet selectLabeledTweet(){
		Statement st;
		ResultSet rs = null;
		String sqlStr = "select * from tweets_labeled;";
		
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sqlStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
	
}
