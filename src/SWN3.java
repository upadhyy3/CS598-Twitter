import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


public class SWN3 {
    private String pathToSWN = "SentiWordNet_3.0.0_20130122.txt";
    private HashMap<String, Double> _dict;
    private int CountofPositiveWords;
    private int CountofNegativeWords;

    public int getCountofPositiveWords() {
		return CountofPositiveWords;
	}

	public void setCountofPositiveWords(int countofPositiveWords) {
		CountofPositiveWords = countofPositiveWords;
	}

	public int getCountofNegativeWords() {
		return CountofNegativeWords;
	}

	public void setCountofNegativeWords(int countofNegativeWords) {
		CountofNegativeWords = countofNegativeWords;
	}

	public SWN3(){

        _dict = new HashMap<String, Double>();
        HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
        try{
            BufferedReader csv =  new BufferedReader(new FileReader(pathToSWN));
            String line = "";   
            int lineNumber=0;
            while((line = csv.readLine()) != null)
            {
            	lineNumber++;

            	// If it's a comment, skip this line.
            	if (!line.trim().startsWith("#")) {
            	  // We use tab separation
            	  String[] data = line.split("\t");
            	  String wordTypeMarker = data[0];

            	  // Example line:
            	  // POS ID PosS NegS SynsetTerm#sensenumber Desc
            	  // a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
            	  // ascetic#2 practicing great self-denial;...etc

            	  // Is it a valid line? Otherwise, through exception.
            	  if (data.length != 6) {
            	    throw new IllegalArgumentException(
            					       "Incorrect tabulation format in file, line: "
            					       + lineNumber);
            	  }
           //     String[] data = line.split("\t");
                Double score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
                String[] words = data[4].split(" ");
                for(String w:words)
                {
                    String[] w_n = w.split("#");
                    w_n[0] += "#"+data[0];
                    int index = Integer.parseInt(w_n[1])-1;
                    if(_temp.containsKey(w_n[0]))
                    {
                        Vector<Double> v = _temp.get(w_n[0]);
                        if(index>v.size())
                            for(int i = v.size();i<index; i++)
                                v.add(0.0);
                        v.add(index, score);
                        _temp.put(w_n[0], v);
                    }
                    else
                    {
                        Vector<Double> v = new Vector<Double>();
                        for(int i = 0;i<index; i++)
                            v.add(0.0);
                        v.add(index, score);
                        _temp.put(w_n[0], v);
                    }
                }
            }
            }
            System.out.println("Came out of loop");
            Set<String> temp = _temp.keySet();
            for (Iterator<String> iterator = temp.iterator(); iterator.hasNext();) {
                String word = (String) iterator.next();
                Vector<Double> v = _temp.get(word);
                double score = 0.0;
                double sum = 0.0;
                for(int i = 0; i < v.size(); i++)
                    score += ((double)1/(double)(i+1))*v.get(i);
                for(int i = 1; i<=v.size(); i++)
                    sum += (double)1/(double)i;
                score /= sum;
                String sent = "";      
               // System.out.println(score);
                if(score>=0.75)
                    sent = "strong_positive";
                else
                if(score > 0.25 && score<=0.5)
                    sent = "positive";
                else
                if(score > 0 && score>=0.25)
                    sent = "weak_positive";
                else
                if(score < 0 && score>=-0.25)
                    sent = "weak_negative";
                else
                if(score < -0.25 && score>=-0.5)
                    sent = "negative";
                else
                if(score<=-0.75)
                    sent = "strong_negative";
                _dict.put(word, score);
            }
            csv.close();
        
           
        }
        catch(Exception e){e.printStackTrace();}        
    }

    public Double extract(String word)
    {
        Double total = new Double(0);
        if(_dict.get(word+"#n") != null)
             total =_dict.get(word+"#n") + total;
        if(_dict.get(word+"#a") != null)
            total = _dict.get(word+"#a") + total;
        if(_dict.get(word+"#r") != null)
            total = _dict.get(word+"#r") + total;
        if(_dict.get(word+"#v") != null)
            total = _dict.get(word+"#v") + total;
        return total;
    }

    public double classifytweet(String text){
    	
    	//String sample = "To date, no ISIS member or Syrian refugee has ever bombed or planned to bomb a black church or home. Nor have they walked into a Bible Study in the basement of a black church and unleashed a hail of bullets. Neither are they at fault for the continuing epidemic of unarmed black men, women, boys, and girls murdered by police officers in the streets of America.";
        String[] words = text.split("\\s+"); 
        double totalScore = 0, averageScore;
        for(String word : words) {
            word = word.replaceAll("([^a-zA-Z\\s])", "");
            double score =extract(word);
            if(score > 0.25)
            {
            	setCountofPositiveWords(getCountofPositiveWords() + 1);
            }
            else if(score <= -0.25)
            {
            	setCountofNegativeWords(getCountofNegativeWords() + 1);
            }
            
            if (extract(word) == null)
                continue;
            
            totalScore += extract(word);
        }
        averageScore = totalScore;

//        if(averageScore>=0.75)
//            return "very positive";
//        else if(averageScore > 0.25 && averageScore<0.5)
//            return  "positive";
//        else if(averageScore>=0.5)
//            return  "positive";
//        else if(averageScore < 0 && averageScore>=-0.25)
//            return "negative";
//        else if(averageScore < -0.25 && averageScore>=-0.5)
//            return "negative";
//        else if(averageScore<=-0.75)
//            return "very negative";
//        return "neutral";
        
        return averageScore;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	//SWN3 swn = new SWN3();
    	//System.out.println(swn.classifytweet());
    	//System.out.println(CountofPositiveWords);
    	//System.out.println(CountofNegativeWords);
    }    
}