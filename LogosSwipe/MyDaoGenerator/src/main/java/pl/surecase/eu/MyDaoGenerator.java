package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");
        //Creating basic tables
        Entity solution = schema.addEntity("Solution");
        solution.addIdProperty();
        solution.addStringProperty("name");
        solution.addStringProperty("description");

        Entity problem = schema.addEntity("Problem");
        problem.addIdProperty();
        problem.addStringProperty("name");
        problem.addStringProperty("description");

        Entity versus = schema.addEntity("Versus");
        versus.addIdProperty();

        Entity solutionScore = schema.addEntity("SolutionScore");
        solutionScore.addIdProperty();
        solutionScore.addDoubleProperty("score");

        Entity versusResponse = schema.addEntity("VersusResponse");
        versusResponse.addIdProperty();
        versusResponse.addDoubleProperty("response");

        Entity comment = schema.addEntity("Comment");
        comment.addIdProperty();
        comment.addStringProperty("name");
        comment.addDateProperty("datetime");
        comment.addStringProperty("content");

        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("username");

        Entity valueSolutionScore = schema.addEntity("ValueSolutionScore");
        valueSolutionScore.addIdProperty();
        valueSolutionScore.addDoubleProperty("score");

        Entity value = schema.addEntity("Value");
        value.addIdProperty();
        value.addStringProperty("name");
        value.addStringProperty("description");

        Entity valueScore = schema.addEntity("ValueScore");
        valueScore.addIdProperty();
        valueScore.addDoubleProperty("score");
        // Creating ToMany relationships
        // ** Problem **//
        /*Property problemId = solution.addLongProperty("problemId").notNull().getProperty();
        ToMany problemToSolution = problem.addToMany(solution, problemId);
        problemToSolution.setName("solutions");

        Property problemId2 = versus.addLongProperty("problemId").notNull().getProperty();
        ToMany problemToVersus = problem.addToMany(versus, problemId2);
        problemToVersus.setName("versus");

        Property problemId3 = value.addLongProperty("problemId").notNull().getProperty();
        ToMany problemToValue = problem.addToMany(value, problemId3);
        problemToValue.setName("values");*/

        // Creating ToOne relationships
        // ** Comment **//

        Property commentId = comment.addLongProperty("commentId").getProperty();
        comment.addToOne(comment, commentId,"comment");

        Property commentId2 = comment.addLongProperty("versusId").getProperty();
        comment.addToOne(versus, commentId2,"versus");

        Property commentId3 = comment.addLongProperty("userId").getProperty();
        comment.addToOne(user, commentId3,"user");

        // ** Solution **/

        Property problemId4 = solution.addLongProperty("problemId").getProperty();
        solution.addToOne(problem, problemId4,"problem");

        // ** SolutionScore *//

        Property solutionScoreId = solutionScore.addLongProperty("solutionId").getProperty();
        solutionScore.addToOne(solution, solutionScoreId,"solution");

        Property solutionScoreId2 = solutionScore.addLongProperty("userId").getProperty();
        solutionScore.addToOne(user, solutionScoreId2,"user");

        // ** Value **//

        Property valueId = value.addLongProperty("problemId").getProperty();
        value.addToOne(problem, valueId,"problem");

        // ** ValueScore **//

        Property valueScoreId = valueScore.addLongProperty("valueId").getProperty();
        valueScore.addToOne(value, valueScoreId,"value");

        Property valueScoreId2 = valueScore.addLongProperty("userId").getProperty();
        valueScore.addToOne(user, valueScoreId2,"user");

        // ** ValueSolutionScore **//

        Property valueSolutionScoreId = valueSolutionScore.addLongProperty("valueId").getProperty();
        valueSolutionScore.addToOne(value, valueSolutionScoreId,"value");

        Property valueSolutionScoreId2 = valueSolutionScore.addLongProperty("userId").getProperty();
        valueSolutionScore.addToOne(user, valueSolutionScoreId2,"user");

        Property valueSolutionScoreId3 = valueSolutionScore.addLongProperty("solutionId").getProperty();
        valueSolutionScore.addToOne(solution, valueSolutionScoreId3,"solution");

        // ** Versus **//

        Property versusId = versus.addLongProperty("solution1Id").getProperty();
        versus.addToOne(solution, versusId,"solution1");

        Property versusId2 = versus.addLongProperty("solution2Id").getProperty();
        versus.addToOne(solution, versusId2,"solution2");

        // ** VersusResponse **//

        Property versusResponseId = versusResponse.addLongProperty("versusId").getProperty();
        versusResponse.addToOne(versus, versusResponseId,"versus");

        Property versusResponseId2 = versusResponse.addLongProperty("userId").getProperty();
        versusResponse.addToOne(user, versusResponseId2,"user");


        new DaoGenerator().generateAll(schema, args[0]);
    }
}
