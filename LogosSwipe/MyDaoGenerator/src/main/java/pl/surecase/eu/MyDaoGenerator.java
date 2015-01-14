package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");

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

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
