package dev.crowdrec.eval.rival;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;
import net.recommenders.rival.core.DataModel;
import net.recommenders.rival.core.SimpleParser;
import net.recommenders.rival.evaluation.metric.ranking.NDCG;

import java.io.File;
import java.io.IOException;

public class UserBasedEval_batch {

    /**
     * The NDCG object used to evaluate the predictions.
     */
    public NDCG ndcg;
    /**
     * The data model based on the testset.
     */
    public DataModel test;
    /**
     * The data model based on the predictions.
     */
    public DataModel predictions;
    /**
     * The value at which to evaluate NDCG, i.e. ndcg@10
     */
    public static final int AT = 10;

	/**
	 *
	 * @param args
	 * $0: stage directory: directory where the algorithm can persist data (e.g., temp files, models,..)
	 * $1: communication directory: directory reserved to communication messages

	 */
	public static void main(String[] args) throws WrongNumberArgsException{

		if ( args.length < 2 ) {
			System.out.println("missing parameters");
            System.out.println("args[0] = testFile");
            System.out.println("args[1] = predictionsFile");
            throw new WrongNumberArgsException("Incorrect or no arguments given");
		}
        UserBasedEval_batch evaluator = null;
        try {
             evaluator = new UserBasedEval_batch(args[0], args[1]);
            int recUsers = evaluator.getNumPredictedUsers();
            int testUsers = evaluator.getNumTestUsers();
            if (recUsers < testUsers)
                System.out.println("There were no predictions for " + (testUsers - recUsers) + " users");
            System.out.println("----------------------\n");
            System.out.println("nDCG@10: " + evaluator.evaluate());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public UserBasedEval_batch(String testsetPath, String predictionsetPath) throws IOException{
        File testFile = new File(testsetPath);
        File predictionFile = new File(predictionsetPath);
        SimpleParser testParser = new SimpleParser();
        this.test = testParser.parseData(testFile, ",");
        this.predictions = testParser.parseData(predictionFile, ",");
        this.ndcg = new NDCG(predictions, test, new int[]{AT});
    }

    /**
     * Compute nDCG@10 for the files given in the constructor.
     * @return nDCG@10
     */
    public double evaluate(){
        ndcg.compute();
        return ndcg.getValueAt(AT);
    }
    /**
     * Get the number of users in the testset.
     * @return the number of users in the testset.
     */
    public int getNumTestUsers(){
        return test.getNumUsers();
    }
    /**
     * Get the number of users in the prediction set.
     * @return the number of users in the prediction set.
     */
    public int getNumPredictedUsers(){
        return predictions.getNumUsers();
    }

}

