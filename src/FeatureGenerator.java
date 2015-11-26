	import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
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
	}
	
	
