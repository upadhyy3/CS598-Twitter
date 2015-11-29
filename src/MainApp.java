import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import com.google.gson.JsonObject;

public class MainApp
{   
    private JFrame frame;
    private JButton creditButton;
    private JButton notCreditButton;
    private JTextArea textArea;
    private int count;
    private LabelTweets labelTweet;
    private ArrayList<Tweet> unLabeledTweets;
    private ArrayList<JsonObject> unLabeledJson;

    public MainApp()
    {
        count = 0;
        labelTweet = new LabelTweets();
        labelTweet.getTwitterDb().importTweetsFromFile("TWEETS");
        unLabeledTweets = labelTweet.getUnlabeledTweet();
        unLabeledJson = labelTweet.getUnlabeledJson();
    }

    private void displayGUI()
    {
        frame = new JFrame("JFrame Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 1, 2, 2));
        textArea = new JTextArea();
        StringBuilder str = new StringBuilder();
        str.append("Message : " + unLabeledTweets.get(count).getMessage() + "\n");
        str.append("User Description : " + unLabeledTweets.get(count).getDescription() + "\n");
        str.append("Reply to Screen Name : " + unLabeledTweets.get(count).getIn_reply_to_screen_name() + "\n");
        str.append("Number of Followees : " + unLabeledTweets.get(count).getNumber_of_followees() + "\n");
        str.append("Number of Followers : " + unLabeledTweets.get(count).getNumber_of_followers() + "\n");
        str.append("Number of Posts : " + unLabeledTweets.get(count).getNumber_of_statuses() + "\n");
        str.append("Restaurant Name : " + unLabeledTweets.get(count).getRestaurant_name() + "\n");
        str.append("Number of Retweets : " + unLabeledTweets.get(count).getRetweet_count() + "\n");
        str.append("Screen Name : " + unLabeledTweets.get(count).getScreen_name() + "\n");
        str.append("User URL : " + unLabeledTweets.get(count).getUser_url() + "\n");
        str.append("Expanded URL :");
        ArrayList<String> expandedUrl = unLabeledTweets.get(count).getExpanded_url();
        for(String url : expandedUrl){
        	str.append(" " + url);
        }
        str.append("\n");
        str.append("Hashtag :");
        ArrayList<String> hashtag = unLabeledTweets.get(count).getHashtag();
        for(String text : hashtag){
        	str.append(" " + text);
        }
        str.append("\n");
        str.append("Promoted : " + unLabeledTweets.get(count).isPromoted() + "\n");
        textArea.setText(str.toString());
        textArea.setEditable(false);
        creditButton = new JButton("Credible");
        creditButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	labelTweet.labelTweet(unLabeledJson.get(count), "C", unLabeledTweets.get(count).getRestaurant_name());
                count++;
                if(count >= unLabeledTweets.size())System.exit(0);
                StringBuilder str = new StringBuilder();
                str.append("Message : " + unLabeledTweets.get(count).getMessage() + "\n");
                str.append("User Description : " + unLabeledTweets.get(count).getDescription() + "\n");
                str.append("Reply to Screen Name : " + unLabeledTweets.get(count).getIn_reply_to_screen_name() + "\n");
                str.append("Number of Followees : " + unLabeledTweets.get(count).getNumber_of_followees() + "\n");
                str.append("Number of Followers : " + unLabeledTweets.get(count).getNumber_of_followers() + "\n");
                str.append("Number of Posts : " + unLabeledTweets.get(count).getNumber_of_statuses() + "\n");
                str.append("Restaurant Name : " + unLabeledTweets.get(count).getRestaurant_name() + "\n");
                str.append("Number of Retweets : " + unLabeledTweets.get(count).getRetweet_count() + "\n");
                str.append("Screen Name : " + unLabeledTweets.get(count).getScreen_name() + "\n");
                str.append("User URL : " + unLabeledTweets.get(count).getUser_url() + "\n");
                str.append("Expanded URL :");
                ArrayList<String> expandedUrl = unLabeledTweets.get(count).getExpanded_url();
                for(String url : expandedUrl){
                	str.append(" " + url);
                }
                str.append("\n");
                str.append("Hashtag :");
                ArrayList<String> hashtag = unLabeledTweets.get(count).getHashtag();
                for(String text : hashtag){
                	str.append(" " + text);
                }
                str.append("\n");
                str.append("Promoted : " + unLabeledTweets.get(count).isPromoted() + "\n");
                textArea.setText(str.toString());
                frame.revalidate();
                frame.repaint();
            }
        });
        notCreditButton = new JButton("Not Credible");
        notCreditButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	labelTweet.labelTweet(unLabeledJson.get(count), "NC", unLabeledTweets.get(count).getRestaurant_name());
                count++;
                if(count >= unLabeledTweets.size())System.exit(0);
                StringBuilder str = new StringBuilder();
                str.append("Message : " + unLabeledTweets.get(count).getMessage() + "\n");
                str.append("User Description : " + unLabeledTweets.get(count).getDescription() + "\n");
                str.append("Reply to Screen Name : " + unLabeledTweets.get(count).getIn_reply_to_screen_name() + "\n");
                str.append("Number of Followees : " + unLabeledTweets.get(count).getNumber_of_followees() + "\n");
                str.append("Number of Followers : " + unLabeledTweets.get(count).getNumber_of_followers() + "\n");
                str.append("Number of Posts : " + unLabeledTweets.get(count).getNumber_of_statuses() + "\n");
                str.append("Restaurant Name : " + unLabeledTweets.get(count).getRestaurant_name() + "\n");
                str.append("Number of Retweets : " + unLabeledTweets.get(count).getRetweet_count() + "\n");
                str.append("Screen Name : " + unLabeledTweets.get(count).getScreen_name() + "\n");
                str.append("User URL : " + unLabeledTweets.get(count).getUser_url() + "\n");
                str.append("Expanded URL :");
                ArrayList<String> expandedUrl = unLabeledTweets.get(count).getExpanded_url();
                for(String url : expandedUrl){
                	str.append(" " + url);
                }
                str.append("\n");
                str.append("Hashtag :");
                ArrayList<String> hashtag = unLabeledTweets.get(count).getHashtag();
                for(String text : hashtag){
                	str.append(" " + text);
                }
                str.append("\n");
                str.append("Promoted : " + unLabeledTweets.get(count).isPromoted() + "\n");
                textArea.setText(str.toString());
                frame.revalidate();
                frame.repaint();
            }
        });
        frame.add(textArea);
        frame.add(creditButton);
        frame.add(notCreditButton);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String... args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new MainApp().displayGUI();
            }
        });
    }
}
