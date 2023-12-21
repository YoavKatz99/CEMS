package common;

import java.io.Serializable;
import java.util.Date;


/**
 * The TestData class represents the data for a test.
 * It contains information such as the test ID, course, date, time allocated, number of students started,
 * number of submissions, number of not submitted, average score, median score, score distribution, and author.
 * The class implements the Serializable interface to support object serialization.
 */
@SuppressWarnings("serial")
public class TestData implements Serializable {

    private String test_id;
    private String course;
    private Date date;
    private String time_allocated;
    private int students_started;
    private int submitted;
    private int notSubmitted;
    private double average;
    private int medain;
    private int[] distribution = new int[10];
    private String author;

    /**
     * Constructs a TestData object with the specified test ID, course, date, time allocated,
     * number of students started, number of submissions, number of not submitted, average score,
     * median score, score distribution, and author.
     *
     * @param test_id          the test ID
     * @param course           the course associated with the test
     * @param date             the date of the test
     * @param time_allocated   the time allocated for the test
     * @param students_started the number of students who started the test
     * @param submitted        the number of submissions for the test
     * @param notSubmitted     the number of students who did not submit the test
     * @param average          the average score for the test
     * @param medain           the median score for the test
     * @param distribution     the score distribution for the test
     * @param author           the author of the test
     */
    public TestData(String test_id, String course, Date date, String time_allocated, int students_started,
                    int submitted, int notSubmitted, double average, int medain, int[] distribution, String author) {
        this.test_id = test_id;
        this.course = course;
        this.date = date;
        this.time_allocated = time_allocated;
        this.students_started = students_started;
        this.submitted = submitted;
        this.notSubmitted = notSubmitted;
        this.average = average;
        this.medain = medain;
        this.distribution = distribution;
        this.author = author;
    }

    /**
     * Constructs a TestData object with the specified date, time allocated,
     * number of students started, number of submissions, and number of not submitted.
     *
     * @param date             the date of the test
     * @param time_allocated   the time allocated for the test
     * @param students_started the number of students who started the test
     * @param submitted        the number of submissions for the test
     * @param notSubmitted     the number of students who did not submit the test
     */
    public TestData(Date date, String time_allocated, int students_started, int submitted, int notSubmitted) {
        this.date = date;
        this.time_allocated = time_allocated;
        this.students_started = students_started;
        this.submitted = submitted;
        this.notSubmitted = notSubmitted;
    }

    /**
     * Returns the test ID.
     *
     * @return the test ID
     */
    public String getTest_id() {
        return test_id;
    }

    /**
     * Sets the test ID.
     *
     * @param test_id the test ID
     */
    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    /**
     * Returns the course associated with the test.
     *
     * @return the course associated with the test
     */
    public String getCourse() {
        return course;
    }

    /**
     * Sets the course associated with the test.
     *
     * @param course the course associated with the test
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Returns the date of the test.
     *
     * @return the date of the test
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the test.
     *
     * @param date the date of the test
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the time allocated for the test.
     *
     * @return the time allocated for the test
     */
    public String getTime_allocated() {
        return time_allocated;
    }

    /**
     * Sets the time allocated for the test.
     *
     * @param time_allocated the time allocated for the test
     */
    public void setTime_allocated(String time_allocated) {
        this.time_allocated = time_allocated;
    }

    /**
     * Returns the number of students who started the test.
     *
     * @return the number of students who started the test
     */
    public int getStudents_started() {
        return students_started;
    }

    /**
     * Sets the number of students who started the test.
     *
     * @param students_started the number of students who started the test
     */
    public void setStudents_started(int students_started) {
        this.students_started = students_started;
    }

    /**
     * Returns the number of submissions for the test.
     *
     * @return the number of submissions for the test
     */
    public int getSubmitted() {
        return submitted;
    }

    /**
     * Sets the number of submissions for the test.
     *
     * @param submitted the number of submissions for the test
     */
    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    /**
     * Returns the average score for the test.
     *
     * @return the average score for the test
     */
    public double getAverage() {
        return average;
    }

    /**
     * Sets the average score for the test.
     *
     * @param average the average score for the test
     */
    public void setAverage(double average) {
        this.average = average;
    }

    /**
     * Returns the median score for the test.
     *
     * @return the median score for the test
     */
    public int getMedain() {
        return medain;
    }

    /**
     * Sets the median score for the test.
     *
     * @param medain the median score for the test
     */
    public void setMedain(int medain) {
        this.medain = medain;
    }

    /**
     * Returns the score distribution for the test.
     *
     * @return the score distribution for the test
     */
    public int[] getDistribution() {
        return distribution;
    }

    /**
     * Sets the score distribution for the test.
     *
     * @param distribution the score distribution for the test
     */
    public void setDistribution(int[] distribution) {
        this.distribution = distribution;
    }

    /**
     * Returns the author of the test.
     *
     * @return the author of the test
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the test.
     *
     * @param author the author of the test
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the number of students who did not submit the test.
     *
     * @return the number of students who did not submit the test
     */
    public int getNotSubmitted() {
        return notSubmitted;
    }

    /**
     * Sets the number of students who did not submit the test.
     *
     * @param notSubmitted the number of students who did not submit the test
     */
    public void setNotSubmitted(int notSubmitted) {
        this.notSubmitted = notSubmitted;
    }
}

