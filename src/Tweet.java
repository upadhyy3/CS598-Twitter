import java.util.ArrayList;

public class Tweet { 

	private String message;
	private String credibility;
	private int retweet_count;
	private String in_reply_to_screen_name;
	private ArrayList<String> expanded_url;
	
	public void setRetweet_count(int retweet_count) {
		this.retweet_count = retweet_count;
	}

	public Tweet (){
		this.message = "";
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


}
