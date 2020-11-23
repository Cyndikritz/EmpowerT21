package Admin;

public class TestClass {
    private String mQuestion;
    private String mkey;
    private int mLevel;

    public TestClass() {
    }

    public TestClass(String mQuestion, String mkey, int mLevel) {
        this.mQuestion = mQuestion;
        this.mkey = mkey;
        this.mLevel = mLevel;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public int getmLevel() {
        return mLevel;
    }

    public void setmLevel(int mLevel) {
        this.mLevel = mLevel;
    }
}
