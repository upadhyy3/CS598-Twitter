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
			    String restaurant_name = rs.getObject(3).toString();
			    Tweet tw = new Tweet();
			    tw.setRestaurant_name(restaurant_name);
			    tw.setMessage(json.get("text").toString().replace("\"",""));
			    tw.setRetweet_count(json.get("retweet_count").getAsInt());
			    tw.setIn_reply_to_screen_name(json.get("in_reply_to_screen_name").toString().replace("\"",""));
			 
			    int numberOfURL = json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().size();
			    
			    ArrayList<String> expandedURL = new ArrayList<String>();
			    if(numberOfURL>0)
			    {  
			    	for(int i = 0; i <numberOfURL;i++)
			    	{
			    	expandedURL.add(json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().get(i).getAsJsonObject().get("expanded_url").toString().replace("\"","").replace("\"", ""));		    
			    	}			   
			    }
			    tw.setExpanded_url(expandedURL);

			    tw.setNumber_of_statuses(json.get("user").getAsJsonObject().get("statuses_count").getAsInt());
			    tw.setNumber_of_followers(json.get("user").getAsJsonObject().get("followers_count").getAsInt());
			    tw.setNumber_of_followees(json.get("user").getAsJsonObject().get("friends_count").getAsInt());
			    tw.setIs_verified(json.get("user").getAsJsonObject().get("verified").getAsBoolean());
			    tw.setDescription(json.get("user").getAsJsonObject().get("description").toString().replace("\"",""));
			    tw.setScreen_name(json.get("user").getAsJsonObject().get("screen_name").toString().replace("\"",""));
			    tw.setUser_url(json.get("user").getAsJsonObject().get("url").toString().replace("\"",""));
			    
			    int numberUserMention = json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().size();
			    ArrayList<String> userMention = new ArrayList<String>();
			    if(numberUserMention>0)
			    {
			    	for(int i = 0; i <numberUserMention;i++)
			    	{
			    		userMention.add(json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().get(i).getAsJsonObject().get("id_str").toString().replace("\"","").replace("\"", ""));		    
			    	}			   
			    }
			    tw.setUser_mention(userMention);
			    
			    int numberHashtag = json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().size();
			    ArrayList<String> hashtag = new ArrayList<String>();
			    if(numberHashtag>0)
			    {
			    	for(int i = 0; i <numberHashtag;i++)
			    	{
			    		hashtag.add(json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().get(i).getAsJsonObject().get("text").toString().replace("\"","").replace("\"", ""));		    
			    	}			   
			    }
			    tw.setHashtag(hashtag);
			    
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
			    String restaurant_name = rs.getObject(3).toString();
			    Tweet tw = new Tweet();
			    tw.setRestaurant_name(restaurant_name);
			    tw.setMessage(json.get("text").toString().replace("\"",""));
			    tw.setCredibility(json.get("credibility").toString().replace("\"",""));
			    tw.setRetweet_count(json.get("retweet_count").getAsInt());
			    tw.setIn_reply_to_screen_name(json.get("in_reply_to_screen_name").toString().replace("\"",""));
			 
		//	    tw.setExpanded_url(json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().g.getAsJsonObject().get("expanded_url").toString());
		//	    System.out.println(json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().size());
			    int numberOfURL = json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().size();
			    ArrayList<String> expandedURL = new ArrayList<String>();
			    if(numberOfURL>0)
			    {  
			    	for(int i = 0; i <numberOfURL;i++)
			    	{
			    	expandedURL.add(json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().get(i).getAsJsonObject().get("expanded_url").toString().replace("\"","").replace("\"", ""));		    
			    	}			   
			    }
			    tw.setExpanded_url(expandedURL);
			   // System.out.println(tw.getExpanded_url());
			    // Retweet count is required
			    tw.setNumber_of_statuses(json.get("user").getAsJsonObject().get("statuses_count").getAsInt());
			    tw.setNumber_of_followers(json.get("user").getAsJsonObject().get("followers_count").getAsInt());
			    tw.setNumber_of_followees(json.get("user").getAsJsonObject().get("friends_count").getAsInt());
			    tw.setIs_verified(json.get("user").getAsJsonObject().get("verified").getAsBoolean());
			    tw.setDescription(json.get("user").getAsJsonObject().get("description").toString().replace("\"",""));
			    tw.setScreen_name(json.get("user").getAsJsonObject().get("screen_name").toString().replace("\"",""));
			    tw.setUser_url(json.get("user").getAsJsonObject().get("url").toString().replace("\"",""));
			    
			    int numberUserMention = json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().size();
			    ArrayList<String> userMention = new ArrayList<String>();
			    if(numberUserMention>0)
			    {
			    	for(int i = 0; i <numberUserMention;i++)
			    	{
			    		userMention.add(json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().get(i).getAsJsonObject().get("id_str").toString().replace("\"","").replace("\"", ""));		    
			    	}			   
			    }
			    tw.setUser_mention(userMention);
			    
			    int numberHashtag = json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().size();
			    ArrayList<String> hashtag = new ArrayList<String>();
			    if(numberHashtag>0)
			    {
			    	for(int i = 0; i <numberHashtag;i++)
			    	{
			    		hashtag.add(json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().get(i).getAsJsonObject().get("text").toString().replace("\"","").replace("\"", ""));		    
			    	}			   
			    }
			    tw.setHashtag(hashtag);
			    
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
	
	public void labelTweet(JsonObject json, String credibility, String retaurant_name){
		json.addProperty("credibility", credibility);
		twitterDb.insertLabeledTweet(json, retaurant_name);
	}
	
	public ArrayList<Tweet> getUnlabeledTweetByRestaurantName(String restaurant_name){
		ArrayList<Tweet> unlabeledTweets = new ArrayList<Tweet>();
		ResultSet rs = twitterDb.selectUnlabeledTweet();
		
		try {
			while (rs.next()) {
			    JsonParser parser = new JsonParser();
			    JsonObject json = (JsonObject)parser.parse(((PGobject)rs.getObject(2)).toString());
			    if(rs.getObject(3).toString().equals(restaurant_name)){
				    Tweet tw = new Tweet();
				    tw.setRestaurant_name(restaurant_name);
				    tw.setMessage(json.get("text").toString().replace("\"",""));
				    tw.setRetweet_count(json.get("retweet_count").getAsInt());
				    tw.setIn_reply_to_screen_name(json.get("in_reply_to_screen_name").toString().replace("\"",""));
				    
				    int numberOfURL = json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().size();
				    ArrayList<String> expandedURL = new ArrayList<String>();
				    if(numberOfURL>0)
				    {  
				    	for(int i = 0; i <numberOfURL;i++)
				    	{
				    	expandedURL.add(json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().get(i).getAsJsonObject().get("expanded_url").toString().replace("\"","").replace("\"", ""));		    
				    	}			   
				    }
				    tw.setExpanded_url(expandedURL);
				    
				    tw.setNumber_of_statuses(json.get("user").getAsJsonObject().get("statuses_count").getAsInt());
				    tw.setNumber_of_followers(json.get("user").getAsJsonObject().get("followers_count").getAsInt());
				    tw.setNumber_of_followees(json.get("user").getAsJsonObject().get("friends_count").getAsInt());
				    tw.setIs_verified(json.get("user").getAsJsonObject().get("verified").getAsBoolean());
				    tw.setDescription(json.get("user").getAsJsonObject().get("description").toString().replace("\"",""));
				    tw.setScreen_name(json.get("user").getAsJsonObject().get("screen_name").toString().replace("\"",""));
				    tw.setUser_url(json.get("user").getAsJsonObject().get("url").toString().replace("\"",""));
				    
				    int numberUserMention = json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().size();
				    ArrayList<String> userMention = new ArrayList<String>();
				    if(numberUserMention>0)
				    {
				    	for(int i = 0; i <numberUserMention;i++)
				    	{
				    		userMention.add(json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().get(i).getAsJsonObject().get("id_str").toString().replace("\"","").replace("\"", ""));		    
				    	}			   
				    }
				    tw.setUser_mention(userMention);
				    
				    int numberHashtag = json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().size();
				    ArrayList<String> hashtag = new ArrayList<String>();
				    if(numberHashtag>0)
				    {
				    	for(int i = 0; i <numberHashtag;i++)
				    	{
				    		hashtag.add(json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().get(i).getAsJsonObject().get("text").toString().replace("\"","").replace("\"", ""));		    
				    	}			   
				    }
				    tw.setHashtag(hashtag);
				    
				    // Retweet count is required
				    unlabeledTweets.add(tw);
			    }
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
	
	public ArrayList<Tweet> getLabeledTweetByRestaurantName(String restaurant_name){
		ArrayList<Tweet> labeledTweets = new ArrayList<Tweet>();
		ResultSet rs = twitterDb.selectLabeledTweet();
		
		try {
			while (rs.next()) {
			    JsonParser parser = new JsonParser();
			    JsonObject json = (JsonObject)parser.parse(((PGobject)rs.getObject(2)).toString());
			    if(rs.getObject(3).toString().equals(restaurant_name)){
				    Tweet tw = new Tweet();
				    tw.setRestaurant_name(restaurant_name);
				    tw.setMessage(json.get("text").toString().replace("\"",""));
				    tw.setCredibility(json.get("credibility").toString().replace("\"",""));
				    tw.setRetweet_count(json.get("retweet_count").getAsInt());
				    tw.setIn_reply_to_screen_name(json.get("in_reply_to_screen_name").toString().replace("\"",""));
				    
				    int numberOfURL = json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().size();
				    ArrayList<String> expandedURL = new ArrayList<String>();
				    if(numberOfURL>0)
				    {  
				    	for(int i = 0; i <numberOfURL;i++)
				    	{
				    	expandedURL.add(json.get("entities").getAsJsonObject().get("urls").getAsJsonArray().get(i).getAsJsonObject().get("expanded_url").toString().replace("\"","").replace("\"", ""));		    
				    	}			   
				    }
				    tw.setExpanded_url(expandedURL);
				    
				    tw.setNumber_of_statuses(json.get("user").getAsJsonObject().get("statuses_count").getAsInt());
				    tw.setNumber_of_followers(json.get("user").getAsJsonObject().get("followers_count").getAsInt());
				    tw.setNumber_of_followees(json.get("user").getAsJsonObject().get("friends_count").getAsInt());
				    tw.setIs_verified(json.get("user").getAsJsonObject().get("verified").getAsBoolean());
				    tw.setDescription(json.get("user").getAsJsonObject().get("description").toString().replace("\"",""));
				    tw.setScreen_name(json.get("user").getAsJsonObject().get("screen_name").toString().replace("\"",""));
				    tw.setUser_url(json.get("user").getAsJsonObject().get("url").toString().replace("\"",""));
				    
				    int numberUserMention = json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().size();
				    ArrayList<String> userMention = new ArrayList<String>();
				    if(numberUserMention>0)
				    {
				    	for(int i = 0; i <numberUserMention;i++)
				    	{
				    		userMention.add(json.get("entities").getAsJsonObject().get("user_mentions").getAsJsonArray().get(i).getAsJsonObject().get("id_str").toString().replace("\"","").replace("\"", ""));		    
				    	}			   
				    }
				    tw.setUser_mention(userMention);
				    
				    int numberHashtag = json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().size();
				    ArrayList<String> hashtag = new ArrayList<String>();
				    if(numberHashtag>0)
				    {
				    	for(int i = 0; i <numberHashtag;i++)
				    	{
				    		hashtag.add(json.get("entities").getAsJsonObject().get("hashtags").getAsJsonArray().get(i).getAsJsonObject().get("text").toString().replace("\"","").replace("\"", ""));		    
				    	}			   
				    }
				    tw.setHashtag(hashtag);
				    
				    // Retweet count is required
				    labeledTweets.add(tw);
			    }
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LabelTweets core = new LabelTweets();
		//core.twitterDb.importTweetsFromFile("Tweets/tweet1");
//		ArrayList<JsonObject> jsonArray = core.getUnlabeledJson();
//		core.labelTweet(jsonArray.get(0),"C");
//		core.labelTweet(jsonArray.get(1),"NC");
//		core.labelTweet(jsonArray.get(2),"C");
//		ArrayList<Tweet> labeledTweets = core.getLabeledTweetByRestaurantName("HowellsandHood");
//		for(Tweet tw : labeledTweets){
//			System.out.println(tw.getRestaurant_name());
//			System.out.println(tw.getMessage());
//			System.out.println(tw.getCredibility());
//			System.out.println(tw.getNumber_of_followees());
//			System.out.println(tw.getNumber_of_followers());
//			System.out.println(tw.getNumber_of_statuses());
//			System.out.println(tw.getDescription());
//			System.out.println(tw.getScreen_name());
//			System.out.println(tw.isIs_verified());
//			System.out.println(tw.getUser_url());
//		}
		FeatureGenerator feature = new FeatureGenerator();
	
//		try {
//			feature.generateFeature();
//			DecisionTreeClassifier.TreeClassifier();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println(feature.authorAverageCountFollowees("HowellsandHood"));
		System.out.println(feature.authorAverageCountFriends("HowellsandHood"));
		System.out.println(feature.authorAverageStatusesCount("HowellsandHood"));
		System.out.println(feature.authorFractionHasDescription("HowellsandHood"));
		System.out.println(feature.authorFractionHasUrl("HowellsandHood"));
		System.out.println(feature.authorFractionIsVerified("HowellsandHood"));
		System.out.println(feature.averageLength("HowellsandHood"));
		System.out.println(feature.countDistinctAuthor("HowellsandHood"));
		System.out.println(feature.countDistinctExpandedUrl("HowellsandHood"));
		System.out.println(feature.countDistinctHashtag("HowellsandHood"));
		System.out.println(feature.countDistinctUserMention("HowellsandHood"));
		System.out.println(feature.countTweets("HowellsandHood"));
		System.out.println(feature.fractionRetweets("HowellsandHood"));
		System.out.println(feature.fractionTweets30PctUppercase("HowellsandHood"));
		System.out.println(feature.fractionTweetsExclamationMark("HowellsandHood"));
		System.out.println(feature.fractionTweetsQuestionMark("HowellsandHood"));
		System.out.println(feature.fractionTweetsURL("HowellsandHood"));
		System.out.println(feature.fractionTweetsUserMention("HowellsandHood"));
		System.out.println(feature.fractionTweetsMultiQuestOrExclMark("HowellsandHood"));
		System.out.println(feature.fractionTweetsHashtag("HowellsandHood"));
		System.out.println(feature.shareMostFreqAuthor("HowellsandHood"));
		System.out.println(feature.shareMostFreqExpandedUrl("HowellsandHood"));
		System.out.println(feature.shareMostFreqHashtag("HowellsandHood"));
		System.out.println(feature.shareMostFreqUserMention("HowellsandHood"));
	}
}
