package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table VALUE_SOLUTION_SCORE.
 */
public class ValueSolutionScore {

    private Long id;
    private Double score;

    public ValueSolutionScore() {
    }

    public ValueSolutionScore(Long id) {
        this.id = id;
    }

    public ValueSolutionScore(Long id, Double score) {
        this.id = id;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}