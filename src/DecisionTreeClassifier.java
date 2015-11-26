import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instances;


public class DecisionTreeClassifier {
	
	public static void TreeClassifier() throws Exception
	{
		String filepath = "C:\\Users\\admin\\CS446\\workspace\\CS598Project\\TwitterFeatures";
		Instances data = new Instances(new FileReader(new File(filepath)));
		data.setClassIndex(data.numAttributes() - 1);
		System.out.println(data.classAttribute());
		
	//	Instances train = data.trainCV(5,0);
	//	Instances test = data.testCV(5, 0);
		for(int n=0;n<2;n++)
		{
			
		Instances train = data.trainCV(2,n);
		Instances test = data.testCV(2, n);
//		Id3 classifier = new Id3();
//		classifier.setMaxDepth(-1);
//		classifier.buildClassifier(train);
		
		 J48 j48 = new J48();
		 j48.setUnpruned(true);
		 j48.buildClassifier(train);
		Evaluation evaluation = new Evaluation(test);
		evaluation.evaluateModel(j48, test);
//		evaluation.evaluateModel(classifier, test);
	//	System.out.println(classifier);
		System.out.println(j48);
		System.out.println(evaluation.toSummaryString());
		System.out.println(evaluation.toMatrixString());
		
//		AdaBoostM1 classifier = new AdaBoostM1();
//		String optionString = " -P 100 -S 1 -I 10 -W weka.classifiers.trees.DecisionStump";
//		classifier.setOptions(weka.core.Utils.splitOptions(optionString));
//		classifier.buildClassifier(train);
//		evaluation.evaluateModel(classifier, test);
//		System.out.println(evaluation.toSummaryString());
//		System.out.println(evaluation.toMatrixString());
//		
//		SMO smo = new SMO();
//		smo.buildClassifier(train);
//		evaluation.evaluateModel(smo, test);
//		System.out.println(evaluation.toSummaryString());
//		System.out.println(evaluation.toMatrixString());
		
		LinearRegression lr = new LinearRegression();
		lr.buildClassifier(train);
		evaluation.evaluateModel(lr, test);
		System.out.println(evaluation.toSummaryString());
		System.out.println(evaluation.toMatrixString());
		
		
	}

}
}
