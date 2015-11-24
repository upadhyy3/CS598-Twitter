import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.classifiers.Evaluation;
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
		for(int n=0;n<3;n++)
		{
			
		Instances train = data.trainCV(3,n);
		Instances test = data.testCV(3, n);
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
		 
	}

}
}
