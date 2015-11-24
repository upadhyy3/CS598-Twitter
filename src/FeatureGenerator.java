import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;


public class FeatureGenerator {

	static String MSGfeatures[];
	static String userFeatures[];
	private static FastVector labelOfTweet;
	private static FastVector zeroOne;

	
	static{
	MSGfeatures = new String [] {"LengthOfMessage","LengthOfWords","CountOfUpperCaseCharacters","ContainsHashtag","NumberOfUniqueCharacters"
	,"Retweet_Count","tweetIsaReply","numberOf@", "hasURL","NumberOfURL","UseOfURLShotner"};
//	userFeatures = new String [] {"NumberOfFollowers","NumberOfFollowees","Verified"};
	labelOfTweet = new FastVector(2);
	labelOfTweet.addElement("\"C\"");
	labelOfTweet.addElement("\"NC\"");
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
	  //  System.out.println(label);
	    Instance instance = makeInstance(instances, line,label,retweet_count,is_reply);

	    instances.add(instance);
	}
	
	ArffSaver saver = new ArffSaver();
	saver.setInstances(instances);
	//saver.setFile(new File(args[1]));
	saver.setFile(new File("TwitterFeatures"));
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
    
    private static int CountOfURL(String s)
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
        if(arrUrl.size()>0){
        	System.out.println(arrUrl.size());
        	return arrUrl.size();}
        else
        	return 0;
    	
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
    
    private static Instance makeInstance(Instances instances, String inputLine,String label,int retweet_count,String is_reply) {
    	inputLine = inputLine.trim();
    	Instance instance = new Instance(MSGfeatures.length+ 1);
    	instance.setDataset(instances);
    //	int lengthOfMessage = inputLine.length();
    	double lengthOfMessage = inputLine.length();
    	instance.setValue(instances.attribute(0),lengthOfMessage);
//    	if(lengthOfMessage > 30)
//    	{
//    		instance.setValue(instances.attribute(0),"1");
//    	}
//    	else{
//    		instance.setValue(instances.attribute(0),"0");
//    	}
    	
    	int noOfWords = countWords(inputLine);
    	instance.setValue(instances.attribute(1),(double)noOfWords);
 //   	if (noOfWords > 6)
//    	{
//    		instance.setValue(instances.attribute(1),"1");
//        }
//        else{
//        	instance.setValue(instances.attribute(1),"0");
//        	}
    	
    	int CountOfUpperCaseCharacters = countUpperLowerCases(inputLine, "u");
   // 	int CountOfLowerCaseCharacters =countUpperLowerCases(inputLine, "l");
    	instance.setValue(instances.attribute(2),(double)CountOfUpperCaseCharacters);
    	
//    	if (CountOfUpperCaseCharacters > 5)
//    	{
//    		instance.setValue(instances.attribute(2),"0");
//        }
//        else{
//        	instance.setValue(instances.attribute(2),"1");
//        	}
    	
    	int CountOfHashTag = CountOfHashTag(inputLine);
    	instance.setValue(instances.attribute(3),(double)CountOfHashTag);
//    	if (CountOfHashTag > 1)
//    	{
//    		instance.setValue(instances.attribute(3),"1");
//        }
//        else{
//        	instance.setValue(instances.attribute(3),"0");
//        	}
    	
    	//char temp[] = firstName.toCharArray();
    	//firstName.substring(0, 4)
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
    	
    	// Number Of URL
    	
    	
    	
    	int numberOfURL = CountOfURL(inputLine);
    	if(numberOfURL>0)
    	{
    		instance.setValue(instances.attribute(8),(double)1 );
    	}
    	else{
    		instance.setValue(instances.attribute(8),(double)0 );
    	}
    	
    	instance.setValue(instances.attribute(8),(double)numberOfURL);
    	
    	//Page Rank
        String domain = "http://www.gmail.com";
        System.out.println("Checking " + domain);
        int pageRank = PageRank.get(domain);
        System.out.println(pageRank);
    	
//    	NLP.init();
//    	System.out.println(NLP.findSentiment(inputLine));
    	
   // 	Instance instance = new Instance(MSGfeatures.length +userFeatures.length+ 1);

    	
//    	instance.setValue(instances.attribute(0),lengthOfMessage);
//    	instance.setValue(instances.attribute(1),noOfWords);
//    	instance.setValue(instances.attribute(2),CountOfUpperCaseCharacters);
//    	instance.setValue(instances.attribute(3),CountOfHashTag);
    //	System.out.println("The Final label before if statement is");
//    	System.out.println(label);
    //	instance.setClassValue(label);
//    	System.out.println("\"C\"");
    	if(label.equals("\"C\""))
    	{
    	instance.setClassValue(0);
    	//System.out.println("C is set");
    	}
    	else if(label.equals("\"N\"")||label.equals("\"NC\"") )
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
//	for (String featureName : userFeatures) {
//	  //  attributes.add(new Attribute(featureName));
//	    attributes.addElement(new Attribute(featureName));
//	}
	Attribute classLabel = new Attribute("Class",labelOfTweet);
	attributes.addElement(classLabel);
//	attributes.add(classLabel);
	//instances = new Instances
	instances = new Instances(nameOfDataset, attributes, 0);

	instances.setClass(classLabel);

	return instances;

    }
}


