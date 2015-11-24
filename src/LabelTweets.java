import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.util.PGobject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class LabelTweets {
	
	private PostgreSQL twitterDb;

	public LabelTweets(){
		this.twitterDb = new PostgreSQL();
	}
	
	public PostgreSQL getTwitterDb() {
		return twitterDb;
	}

	public void setTwitterDb(PostgreSQL twitterDb) {
		this.twitterDb = twitterDb;
	}
	
	public ArrayList<Tweet> getUnlabeledTweet(){
		ArrayList<Tweet> unlabeledTweets = new ArrayList<Tweet>();
		ResultSet rs = twitterDb.selectUnlabeledTweet();
		
		try {
			while (rs.next()) {
			    JsonParser parser = new JsonParser();
			    JsonObject json = (JsonObject)parser.parse(((PGobject)rs.getObject(2)).toString());
			    Tweet tw = new Tweet();
			    tw.setMessage(json.get("text").toString());
			    unlabeledTweets.add(tw);
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unlabeledTweets;
	}
	
	public ArrayList<JsonObject> getUnlabeledJson(){
		ArrayList<JsonObject> unlabeledJson = new ArrayList<JsonObject>();
		ResultSet rs = twitterDb.selectUnlabeledTweet();
		
		try {
			while (rs.next()) {
			    JsonParser parser = new JsonParser();
			    JsonObject json = (JsonObject)parser.parse(((PGobject)rs.getObject(2)).toString());
			    unlabeledJson.add(json);
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unlabeledJson;
	}
	
	public ArrayList<Tweet> getLabeledTweet(){
		ArrayList<Tweet> labeledTweets = new ArrayList<Tweet>();
		ResultSet rs = twitterDb.selectLabeledTweet();
		
		try {
			while (rs.next()) {
			    JsonParser parser = new JsonParser();
			    JsonObject json = (JsonObject)parser.parse(((PGobject)rs.getObject(2)).toString());
			    Tweet tw = new Tweet();
			    tw.setMessage(json.get("text").toString());
			    tw.setCredibility(json.get("credibility").toString());
			    tw.setRetweet_count(json.get("retweet_count").getAsInt());
			    tw.setIn_reply_to_screen_name(json.get("in_reply_to_screen_name").toString());
			    // Retweet count is required
			    labeledTweets.add(tw);
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return labeledTweets;
	}
	
	public void labelTweet(JsonObject json, String credibility){
		json.addProperty("credibility", credibility);
		twitterDb.insertLabeledTweet(json);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LabelTweets core = new LabelTweets();
		//core.twitterDb.importTweetsFromFile("Tweets/tweet1");
//		ArrayList<JsonObject> jsonArray = core.getUnlabeledJson();
//		core.labelTweet(jsonArray.get(0),"C");
//		core.labelTweet(jsonArray.get(1),"NC");
//		core.labelTweet(jsonArray.get(2),"C");
		ArrayList<Tweet> labeledTweets = core.getLabeledTweet();
//		System.out.println(labeledTweets.get(0).getMessage());
//		System.out.println(labeledTweets.get(0).getCredibility());
//		System.out.println(labeledTweets.get(1).getMessage());
//		System.out.println(labeledTweets.get(1).getCredibility());
//		System.out.println(labeledTweets.get(2).getMessage());
//		System.out.println(labeledTweets.get(2).getCredibility());
		FeatureGenerator feature = new FeatureGenerator();
	
		try {
			feature.generateFeature();
			DecisionTreeClassifier.TreeClassifier();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
