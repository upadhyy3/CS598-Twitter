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
        labelTweet.getTwitterDb().importTweetsFromFile("Tweets");
        unLabeledTweets = labelTweet.getUnlabeledTweet();
        unLabeledJson = labelTweet.getUnlabeledJson();
    }

    private void displayGUI()
    {
        frame = new JFrame("JFrame Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 1, 2, 2));
        textArea = new JTextArea();
        textArea.setText(unLabeledTweets.get(count).getMessage());
        textArea.setEditable(false);
        creditButton = new JButton("Credible");
        creditButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	labelTweet.labelTweet(unLabeledJson.get(count),"C");
                count++;
                if(count >= unLabeledTweets.size())System.exit(0);
                textArea.setText(unLabeledTweets.get(count).getMessage());
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
            	labelTweet.labelTweet(unLabeledJson.get(count),"NC");
                count++;
                if(count >= unLabeledTweets.size())System.exit(0);
                textArea.setText(unLabeledTweets.get(count).getMessage());
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