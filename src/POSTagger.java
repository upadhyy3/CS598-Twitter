
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
	
public class POSTagger {

 private POSTagger() {}

	  public static List<Integer> POStagger(String tweetText) throws Exception {
	    MaxentTagger tagger = new MaxentTagger("english-bidirectional-distsim.tagger");
	    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(tweetText));
	  //  List<TaggedWord> tSentence = new ArrayList<TaggedWord>();
	    List<Integer>CountOfNounAndPronoun = new ArrayList<Integer>();
	    int nounCount=0;
	    int pronounCount=0;
	    for (List<HasWord> sentence : sentences) {
	      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
	      System.out.println(Sentence.listToString(tSentence, false));
	      System.out.println("$$$$$$$$$$$$$$$$");
	      	for(TaggedWord tw: tSentence){
	      		if(tw.toString().split("/")[1].startsWith("N"))
	      		{
	      			nounCount++;
	      		}
	      		if(tw.toString().split("/")[1].startsWith("W"))
	      		{
	      			pronounCount++;
	      		}
	      	}
	    }
	    System.out.println(nounCount);
	    System.out.println(pronounCount);
	    CountOfNounAndPronoun.add(0, nounCount);
	    CountOfNounAndPronoun.add(1, pronounCount);
	   return CountOfNounAndPronoun;
	  }

	
public static void main(String[] args)
{
	try {
		POStagger("President Vladimir Putin said Russian planes were easily identifiable and the jet's flight co-ordinates had been passed on to Turkey's ally, the US.");
		POStagger("When we will complete our project?");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


}


