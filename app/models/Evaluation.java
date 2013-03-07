package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;

/**
 * <p>
 * Class representing the evaluation of the application.
 * </p>
 *
 * @author billy
 *
 */
@Entity
public class Evaluation extends Model {
    /**
     * The evaluation's unique id.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;

    /**
     * Question one (1).
     */
    public String q1;

    /**
     * Question two (2).
     */
    public String q2;

    /**
     * Question three (3).
     */
    public String q3;

    /**
     * Constructor.
     *
     * @param q1    Question one (1).
     * @param q2    Question two (2).
     * @param q3    Question three (3).
     */
    public Evaluation(
            String q1,
            String q2,
            String q3) {
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }
}
