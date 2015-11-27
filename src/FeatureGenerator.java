	import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
	



	import javax.swing.plaf.synth.SynthOptionPaneUI;

import com.google.common.net.InternetDomainName;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;
	
	
	public class FeatureGenerator {
	
		static String MSGfeatures[];
		static String userFeatures[];
		private static FastVector labelOfTweet;
		private static FastVector zeroOne;
	
		
		static{
		MSGfeatures = new String [] {"LengthOfMessage","LengthOfWords","CountOfUpperCaseCharacters","ContainsHashtag","NumberOfUniqueCharacters"
		,"Retweet_Count","tweetIsaReply","numberOf@", "hasURL","NumberOfURL","UseOfURLShotner"};
		userFeatures = new String [] {"NumberOfStatuses","NumberOfFollowers","NumberOfFollowees","Verified","LengthOfDescription","LengthOfScreenName", "HasURL", 
		"RatioOfFollowersToFollowees"};
		labelOfTweet = new FastVector(2);
		labelOfTweet.addElement("C");
		labelOfTweet.addElement("NC");
		zeroOne = new FastVector(2);
		zeroOne.addElement("0");
		zeroOne.addElement("1");
		}
	
		
	    public static void generateFeature() throws Exception{
	
		Instances instances = initializeAttributes();
		LabelTweets labelTweets = new LabelTweets();
		ArrayList<Tweet> labeledTweets = labelTweets.getLabeledTweet();
	
		for(Tweet tweet:labeledTweets)
		{
		    String line = tweet.getMessage();
		    String label = tweet.getCredibility();
		    int retweet_count = tweet.getRetweet_count();
		    String is_reply = tweet.getIn_reply_to_screen_name();
		    ArrayList<String> fullURL = tweet.getExpanded_url();
		    int number_of_statuses = tweet.getNumber_of_statuses();
		    int number_of_followers = tweet.getNumber_of_followers();
		    int number_of_followees = tweet.getNumber_of_followees();
		    boolean is_verified = tweet.isIs_verified();
		    String description = tweet.getDescription();
		    String screen_name = tweet.getScreen_name();
		    String user_url = tweet.getUser_url();
		  //  System.out.println(label);
		    Instance instance = makeInstance(instances, line,label,retweet_count,is_reply,fullURL, number_of_statuses, number_of_followers, number_of_followees, 
					is_verified, description, screen_name, user_url);
	
		    instances.add(instance);
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(instances);
		//saver.setFile(new File(args[1]));
		saver.setFile(new File("TwitterFeatures.arff"));
		saver.writeBatch();
	
		CSVSaver csvSaver = new CSVSaver();
		saver.setInstances(instances);
		saver.setFile(new File("FeaturesinCSV.csv"));
		saver.writeBatch();
		
	    }
	    
	    public static int countWords(String s){
	
	        int wordCount = 0;
	
	        boolean word = false;
	        int endOfLine = s.length() - 1;
	
	        for (int i = 0; i < s.length(); i++) {
	            // if the char is a letter, word = true.
	            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
	                word = true;
	                // if char isn't a letter and there have been letters before,
	                // counter goes up.
	            } else if (!Character.isLetter(s.charAt(i)) && word) {
	                wordCount++;
	                word = false;
	                // last word of String; if it doesn't end with a non letter, it
	                // wouldn't count without this.
	            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
	                wordCount++;
	            }
	        }
	        return wordCount;
	    }
	    
	    private static ArrayList<URL> CountOfURL(String s)
	    {
			String text = "Shivam is readign http://stackoverflow.com/questions/285619/how-to-detect-the-presence-of-url-in-a-string";
			ArrayList<URL> arrUrl = new ArrayList<URL>();
			// separete input by spaces
			String [] parts = s.split(" "); 
			 
	        // Attempt to convert each item into an URL.    
	        for( String item : parts )
	        {
	        try { 
	            URL url = new URL(item); 
	            arrUrl.add(url);
	            // If possible then replace with anchor... 
	           // System.out.print(url+"");     
	        } catch (MalformedURLException e) { 
	            // If there was an URL that was not it!... 
	          //  System.out.print( item + " " ); 
	        } 
	        }
	      
	
	        	return arrUrl;
	    }
	
	    	
	    public static String expandUrl(String shortenedUrl) throws IOException {
	        URL url = new URL(shortenedUrl);    
	        System.out.println("Exception occured and this is not printed");
	        // open connection
	        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); 
	        
	        // stop following browser redirect
	        httpURLConnection.setInstanceFollowRedirects(false);
	         
	        // extract location header containing the actual destination URL
	        String expandedURL = httpURLConnection.getHeaderField("Location");
	        httpURLConnection.disconnect();
	     //   System.out.println(expandedURL);
	        if(expandedURL != null)
	        {
	        return expandedURL;
	        }
	        else
	        {
	        	return shortenedUrl;
	        }
	    }
	    
	    
	    private static int countUpperLowerCases(String s, String Ul)
	    {
	    	int charCount = 0;
	    	
	    	if (Ul.equalsIgnoreCase("u"))
	    	{
	    		for(int i=0; i<s.length();i++) {
	    			if(Character.isUpperCase(s.charAt(i))){
	            		charCount++;
	    			}
	    	}
	    	}
	    	else {
	    		for(int i=0; i<s.length();i++) {
	    			if(Character.isLowerCase(s.charAt(i))){
	            		charCount++;
	    			}
	    	}
	    	}  
	    	return charCount;
	    }
	    
	    private static int CountOfMentions(String s)
	    {
	    	int Count=0;
	    	Pattern MY_PATTERN =  Pattern.compile("@(\\w+)");
	    	Matcher mat = MY_PATTERN.matcher(s);
	    	List<String> str=new ArrayList<String>();
	    	while (mat.find()) {
	    	  //System.out.println(mat.group(1));
	    	//  str.add(mat.group(1));
	    		Count++;
	    	}
	    return Count;
	    }
	    private static int CountOfHashTag(String s)
	    {
	    	int HashtagCount=0;
	    	Pattern MY_PATTERN =  Pattern.compile("#(\\w+)");
	    	Matcher mat = MY_PATTERN.matcher(s);
	    	List<String> str=new ArrayList<String>();
	    	while (mat.find()) {
	    	  //System.out.println(mat.group(1));
	    	//  str.add(mat.group(1));
	    		HashtagCount++;
	    	}
	    return HashtagCount;
	    }
	    
	    private static int CountOfUniqueCharacters(String s)
	    {
	        boolean[] isItThere = new boolean[Character.MAX_VALUE];
	        for (int i = 0; i < s.length(); i++) {
	            isItThere[s.charAt(i)] = true;
	        }
	
	        int count = 0;
	        for (int i = 0; i < isItThere.length; i++) {
	            if (isItThere[i] == true){
	                count++;
	            }
	        }
	
	        return count;
	    }
	    
	    private static String getDomainName(String url) throws URISyntaxException {
	        URI uri = new URI(url);
	        String domain = uri.getHost();
	        System.out.println(domain);
//	        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//	        System.out.println(InternetDomainName.from(domain).topPrivateDomain().toString());
//	        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	        return InternetDomainName.from(domain).topPrivateDomain().toString();
	        //  return domain.startsWith("www.") ? domain.substring(4) : domain;
	    }
	    
	    private static Instance makeInstance(Instances instances, String inputLine,String label,int retweet_count,String is_reply,ArrayList<String> fullURL,
	    		int number_of_statuses, int number_of_followers, int number_of_followees, boolean is_verified, String description, String screen_name, 
				String user_url) {
	    	inputLine = inputLine.trim();
	    	Instance instance = new Instance(MSGfeatures.length+ userFeatures.length + 1);
	    	instance.setDataset(instances);
	    //	int lengthOfMessage = inputLine.length();
	    	double lengthOfMessage = inputLine.length();
	    	instance.setValue(instances.attribute(0),lengthOfMessage);

	    	
	    	int noOfWords = countWords(inputLine);
	    	instance.setValue(instances.attribute(1),(double)noOfWords);

	    	
	    	int CountOfUpperCaseCharacters = countUpperLowerCases(inputLine, "u");
	   // 	int CountOfLowerCaseCharacters =countUpperLowerCases(inputLine, "l");
	    	instance.setValue(instances.attribute(2),(double)CountOfUpperCaseCharacters);
	    	

	    	
	    	int CountOfHashTag = CountOfHashTag(inputLine);
	    	instance.setValue(instances.attribute(3),(double)CountOfHashTag);

	    	//Number of unique characters
	    	int NumberOfUniqueCharacters = CountOfUniqueCharacters(inputLine);
	    	instance.setValue(instances.attribute(4),(double)NumberOfUniqueCharacters);
	    	
	    	//Retweet count 
	    	instance.setValue(instances.attribute(5),(double)retweet_count);
	    	
	    	//Tweet is a reply
	    	if(is_reply.isEmpty())
	    	{
	    	instance.setValue(instances.attribute(6),(double)0);
	    	}
	    	else
	    	{
	    		instance.setValue(instances.attribute(6),(double)1);
	    	}
	    	
	    	// Number of @
	    	int numberOfMentions = CountOfMentions(inputLine);
	    	instance.setValue(instances.attribute(7),(double)numberOfMentions);
	    	
	    	// Number Of URL and has a url     	
	    	ArrayList<URL> listOfURL = CountOfURL(inputLine);
	    	if(listOfURL.size()>0)
	    	{
	    		instance.setValue(instances.attribute(8),(double)1 );
	    	}
	    	else{
	    		instance.setValue(instances.attribute(8),(double)0 );
	    	}
	    	
	    	instance.setValue(instances.attribute(9),(double)listOfURL.size());
	    	
	    	//Page Rank
	    	int average = 0;
	    	if(fullURL != null && fullURL.size()>0)
	    	{	
		    	for(String url:fullURL)
		    	{
			        try {
//			        	String temp = "https://instagram.com/p/9SIiwevKUg/";
//			        	System.out.println("Before expand URL call");
			        	System.out.println(url);
			        	String expandedURL = expandUrl(url);
			        	
//			        	System.out.println(expandedURL);
//			        	System.out.println("executing get domain");
//			        	String dom =getDomainName(temp);
			        	String dom =getDomainName(expandedURL);
					//	String dom =getDomainName(url.toString());
//				        System.out.println("Checking " + dom);
				        int pageRank = PageRank.get(dom);
				        average += pageRank;
				    //    int pageRank = PageRank.get(url.toString());
				        System.out.println(pageRank);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						System.out.println("Its coming here");
						e.printStackTrace();
					} 
			        catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    	instance.setValue(instances.attribute(10),(double)(average/fullURL.size()));
	    	}
	    	
	    	//==================================== userFeatures start here ====================================
	    	
	    	//Number of statuses
	    	instance.setValue(instances.attribute(11),(double)number_of_statuses);
	    	
	    	//Number of followers
	    	instance.setValue(instances.attribute(12),(double)number_of_followers);
	    	
	    	//Number of followees
	    	instance.setValue(instances.attribute(13),(double)number_of_followees);
	    	
	    	//Is verified
			if(is_verified){
				instance.setValue(instances.attribute(14),(double)1);
			}else{
				instance.setValue(instances.attribute(14),(double)0);
			}
			
			//Length of description
			instance.setValue(instances.attribute(15),(double)description.length());
			
			//Length of screen name
			instance.setValue(instances.attribute(16),(double)screen_name.length());
			
			//Has URL
			if(!"null".equals(user_url)){
				instance.setValue(instances.attribute(17),(double)1);
			}else{
				instance.setValue(instances.attribute(17),(double)0);
			}
			
			//Ratio number of followers to followees
			instance.setValue(instances.attribute(18),((double)number_of_followers)/((double)number_of_followees));
	    	
			//==================================== End of userFeatures =======================================


	    	
//	    	NLP.init();
//	    	System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//	    	System.out.println(NLP.findSentiment(inputLine));
//	    	System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

	    	if(label.equals("C"))
	    	{
	    	instance.setClassValue(0);
	    	//System.out.println("C is set");
	    	}
	    	else if(label.equals("\"N\"")||label.equals("NC") )
	    	{
	    		instance.setClassValue(1);
	    		//System.out.println("N is set");
	    	}
	    	else
	    	{
	    		instance.setClassMissing();
	    	}
	   // 	instance.setClassValue("C");
	    	return instance;
	        }
		
	    private static Instances initializeAttributes() {
	
		String nameOfDataset = "Tweets";
	
		Instances instances;
	
		FastVector attributes = new FastVector();
	//	List<Attribute> attributes = new ArrayList<Attribute>();
		for (String featureName : MSGfeatures) {
	//	    attributes.add(new Attribute(featureName));
		    attributes.addElement(new Attribute(featureName));
		}
		for (String featureName : userFeatures) {
		  //  attributes.add(new Attribute(featureName));
		    attributes.addElement(new Attribute(featureName));
		}
		Attribute classLabel = new Attribute("Class",labelOfTweet);
		attributes.addElement(classLabel);
	//	attributes.add(classLabel);
		//instances = new Instances
		instances = new Instances(nameOfDataset, attributes, 0);
	
		instances.setClass(classLabel);
	
		return instances;
	
	    }
	    
	//==================================== Methods for Aggregate Features ====================================
	    
	    private int countOccurrences(String s, char c){
	    	int counter = 0;
	    	for(int i=0; i<s.length(); i++){
	    	    if(s.charAt(i) == c){
	    	        counter++;
	    	    }
	    	}
	    	return counter;
	    }
	    
	    public int countTweets(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	return tweets.size();
	    }
	    
	    public double averageLength(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int sum = 0;
	    	for(Tweet tw : tweets){
	    		sum = sum + tw.getMessage().length();
	    	}
	    	return (double)sum/(double)n;
	    }
	    
	    public double fractionTweetsQuestionMark(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfQuestionMark = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.getMessage().contains("?"))numberOfQuestionMark++;
	    	}
	    	return (double)numberOfQuestionMark/(double)n;
	    }
	    
	    public double fractionTweetsExclamationMark(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfExclamationMark = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.getMessage().contains("!"))numberOfExclamationMark++;
	    	}
	    	return (double)numberOfExclamationMark/(double)n;
	    }
	    
	    public double fractionTweetsMultiQuestOrExclMark(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfMultiQuestOrExclMark = 0;
	    	for(Tweet tw : tweets){
	    		int questMark = countOccurrences(tw.getMessage(), '?'); 
	    		int exclMark = countOccurrences(tw.getMessage(), '!');
	    		if((questMark > 1) || (exclMark > 1))numberOfMultiQuestOrExclMark++;
	    	}
	    	return (double)numberOfMultiQuestOrExclMark/(double)n;
	    }
	    
	    public double fractionTweets30PctUppercase(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfTweetsUpper = 0;
	    	for(Tweet tw : tweets){
	    		int countUpper = 0;
	    		for(int i=0;i<tw.getMessage().length();i++){
	    			if(Character.isUpperCase(tw.getMessage().charAt(i)))countUpper++;
	    		}
	    		double percentUpper = (double)countUpper/(double)tw.getMessage().length();
	    		if(percentUpper > 0.3)numberOfTweetsUpper++;
	    	}
	    	return (double)numberOfTweetsUpper/(double)n;
	    }
	    
	    public double fractionTweetsURL(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfTweetsURL = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.getExpanded_url().size() > 0)numberOfTweetsURL++;
	    	}
	    	return (double)numberOfTweetsURL/(double)n;
	    }
	    
	    public double fractionTweetsUserMention(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfTweetsUserMention = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.getUser_mention().size() > 0)numberOfTweetsUserMention++;
	    	}
	    	return (double)numberOfTweetsUserMention/(double)n;
	    }
	    
	    public double fractionTweetsHashtag(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfTweetsHashtag = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.getHashtag().size() > 0)numberOfTweetsHashtag++;
	    	}
	    	return (double)numberOfTweetsHashtag/(double)n;
	    }
	    
	    public double fractionRetweets(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int n = tweets.size();
	    	int numberOfRetweet = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.getRetweet_count() > 0)numberOfRetweet++;
	    	}
	    	return (double)numberOfRetweet/(double)n;
	    }
	    
	    public int countDistinctExpandedUrl(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> s = new TreeSet<String>();
	    	for(Tweet tw : tweets){
	    		ArrayList<String> expandedUrl = tw.getExpanded_url();
	    		for(String url : expandedUrl){
	    			s.add(url);
	    		}
	    	}
	    	return s.size();
	    }
	    
	    public double shareMostFreqExpandedUrl(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	HashMap<String, Integer> map = new HashMap<String, Integer>();
	    	int n = 0;
	    	for(Tweet tw : tweets){
	    		ArrayList<String> expandedUrl = tw.getExpanded_url();
	    		for(String url : expandedUrl){
	    			n++;
	    			if(map.containsKey(url)){
	    				map.put(url, map.get(url) + 1);
	    			}else{
	    				map.put(url,1);
	    			}
	    		}
	    	}
	    	Entry<String, Integer> maxEntry = null;
    		for(Entry<String, Integer> entry : map.entrySet()){
    		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
    		        maxEntry = entry;
    		    }
    		}
	    	return (double)maxEntry.getValue()/(double)n;
	    }
	    
	    public int countDistinctHashtag(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> s = new TreeSet<String>();
	    	for(Tweet tw : tweets){
	    		ArrayList<String> hashtag = tw.getHashtag();
	    		for(String text : hashtag){
	    			s.add(text);
	    		}
	    	}
	    	return s.size();
	    }
	    
	    public double shareMostFreqHashtag(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	HashMap<String, Integer> map = new HashMap<String, Integer>();
	    	int n = 0;
	    	for(Tweet tw : tweets){
	    		ArrayList<String> hashtag = tw.getHashtag();
	    		for(String text : hashtag){
	    			n++;
	    			if(map.containsKey(text)){
	    				map.put(text, map.get(text) + 1);
	    			}else{
	    				map.put(text,1);
	    			}
	    		}
	    	}
	    	Entry<String, Integer> maxEntry = null;
    		for(Entry<String, Integer> entry : map.entrySet()){
    		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
    		        maxEntry = entry;
    		    }
    		}
	    	return (double)maxEntry.getValue()/(double)n;
	    }
	    
	    public int countDistinctUserMention(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> s = new TreeSet<String>();
	    	for(Tweet tw : tweets){
	    		ArrayList<String> userMention = tw.getUser_mention();
	    		for(String strId : userMention){
	    			s.add(strId);
	    		}
	    	}
	    	return s.size();
	    }
	    
	    public double shareMostFreqUserMention(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	HashMap<String, Integer> map = new HashMap<String, Integer>();
	    	int n = 0;
	    	for(Tweet tw : tweets){
	    		ArrayList<String> userMention = tw.getUser_mention();
	    		for(String strId : userMention){
	    			n++;
	    			if(map.containsKey(strId)){
	    				map.put(strId, map.get(strId) + 1);
	    			}else{
	    				map.put(strId,1);
	    			}
	    		}
	    	}
	    	Entry<String, Integer> maxEntry = null;
    		for(Entry<String, Integer> entry : map.entrySet()){
    		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
    		        maxEntry = entry;
    		    }
    		}
	    	return (double)maxEntry.getValue()/(double)n;
	    }
	    
	    public int countDistinctAuthor(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> s = new TreeSet<String>();
	    	for(Tweet tw : tweets){
	    		s.add(tw.getScreen_name());
	    	}
	    	return s.size();
	    }
	    
	    public double shareMostFreqAuthor(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	HashMap<String, Integer> map = new HashMap<String, Integer>();
	    	int n = 0;
	    	for(Tweet tw : tweets){
	    		String screenName = tw.getScreen_name();
    			n++;
    			if(map.containsKey(screenName)){
    				map.put(screenName, map.get(screenName) + 1);
    			}else{
    				map.put(screenName,1);
    			}
	    	}
	    	Entry<String, Integer> maxEntry = null;
    		for(Entry<String, Integer> entry : map.entrySet()){
    		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
    		        maxEntry = entry;
    		    }
    		}
	    	return (double)maxEntry.getValue()/(double)n;
	    }
	    
	    public double authorAverageStatusesCount(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> author = new TreeSet<String>();
	    	int sum = 0;
	    	for(Tweet tw : tweets){
	    		if(!author.contains(tw.getScreen_name())){
	    			sum = sum + tw.getNumber_of_statuses();
	    			author.add(tw.getScreen_name());
	    		}
	    	}
	    	return (double)sum/(double)author.size();
	    }
	    
	    public double authorAverageCountFollowees(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> author = new TreeSet<String>();
	    	int sum = 0;
	    	for(Tweet tw : tweets){
	    		if(!author.contains(tw.getScreen_name())){
	    			sum = sum + tw.getNumber_of_followers();
	    			author.add(tw.getScreen_name());
	    		}
	    	}
	    	return (double)sum/(double)author.size();
	    }
	    
	    public double authorAverageCountFriends(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	Set<String> author = new TreeSet<String>();
	    	int sum = 0;
	    	for(Tweet tw : tweets){
	    		if(!author.contains(tw.getScreen_name())){
	    			sum = sum + tw.getNumber_of_followees();
	    			author.add(tw.getScreen_name());
	    		}
	    	}
	    	return (double)sum/(double)author.size();
	    }
	    
	    public double authorFractionIsVerified(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int numTweetVerified = 0;
	    	for(Tweet tw : tweets){
	    		if(tw.isIs_verified())numTweetVerified++;
	    	}
	    	return (double)numTweetVerified/(double)tweets.size();
	    }
	    
	    public double authorFractionHasDescription(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int numTweetHasDesc = 0;
	    	for(Tweet tw : tweets){
	    		if(!"null".equals(tw.getDescription()))numTweetHasDesc++;
	    	}
	    	return (double)numTweetHasDesc/(double)tweets.size();
	    }
	    
	    public double authorFractionHasUrl(String restaurant_name){
	    	LabelTweets labelTweets = new LabelTweets();
	    	ArrayList<Tweet> tweets = labelTweets.getLabeledTweetByRestaurantName(restaurant_name);
	    	int numTweetHasUrl = 0;
	    	for(Tweet tw : tweets){
	    		if(!"null".equals(tw.getUser_url()))numTweetHasUrl++;
	    	}
	    	return (double)numTweetHasUrl/(double)tweets.size();
	    }
	    
	  //==================================== End of Methods for Aggregate Features ====================================
	   
	}
	
	
