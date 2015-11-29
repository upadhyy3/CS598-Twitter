import java.util.ArrayList;

public class Tweet { 

	private String restaurant_name;
	private String message;
	private String credibility;
	private int retweet_count;
	private String in_reply_to_screen_name;
	private ArrayList<String> expanded_url;
	private int number_of_statuses;
	private int number_of_followers;
	private int number_of_followees;
	private boolean is_verified;
	private String description;
	private String screen_name;
	private String user_url;
	private ArrayList<String> user_mention;
	private ArrayList<String> hashtag;
	private boolean promoted;
	
	public void setRetweet_count(int retweet_count) {
		this.retweet_count = retweet_count;
	}

	public Tweet (){
		this.message = "";
	}
	
	public String getRestaurant_name() {
		return restaurant_name;
	}

	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCredibility() {
		return credibility;
	}

	public void setCredibility(String credibility) {
		this.credibility = credibility;
	}

	public int getRetweet_count()
	{
		return retweet_count;
	}

	public String getIn_reply_to_screen_name() {
		return in_reply_to_screen_name;
	}

	public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
		this.in_reply_to_screen_name = in_reply_to_screen_name;
	}

	public ArrayList<String> getExpanded_url() {
		return expanded_url;
	}

	public void setExpanded_url(ArrayList<String> expanded_url) {
		this.expanded_url = expanded_url;
	}
	
	public int getNumber_of_statuses() {
		return number_of_statuses;
	}

	public void setNumber_of_statuses(int number_of_statuses) {
		this.number_of_statuses = number_of_statuses;
	}

	public int getNumber_of_followers() {
		return number_of_followers;
	}

	public void setNumber_of_followers(int number_of_followers) {
		this.number_of_followers = number_of_followers;
	}

	public int getNumber_of_followees() {
		return number_of_followees;
	}

	public void setNumber_of_followees(int number_of_followees) {
		this.number_of_followees = number_of_followees;
	}

	public boolean isIs_verified() {
		return is_verified;
	}

	public void setIs_verified(boolean is_verified) {
		this.is_verified = is_verified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getUser_url() {
		return user_url;
	}

	public void setUser_url(String user_url) {
		this.user_url = user_url;
	}

	public ArrayList<String> getUser_mention() {
		return user_mention;
	}

	public void setUser_mention(ArrayList<String> user_mention) {
		this.user_mention = user_mention;
	}

	public ArrayList<String> getHashtag() {
		return hashtag;
	}

	public void setHashtag(ArrayList<String> hashtag) {
		this.hashtag = hashtag;
	}

	public boolean isPromoted() {
		return promoted;
	}

	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}

}
