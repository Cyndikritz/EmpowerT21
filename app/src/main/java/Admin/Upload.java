package Admin;

public class Upload {
    private String mName;
    private String mImageUrl;
    private int mLevel;
    private String mLanguage;
    private String mkey;
    private String mPublisher;

    public Upload(){

    }
    public Upload(String name , String imageUrl, int level, String language, String key){
        if (name.trim().equals("")){
            name = "No Name";
        }
        mName=name;
        mImageUrl=imageUrl;
        mkey=key;
        mLevel = level;
        mLanguage = language;
    }

    public Upload(String name , String imageUrl, String language, String key){
        if (name.trim().equals("")){
            name = "No Name";
        }
        mName=name;
        mImageUrl=imageUrl;
        mkey=key;
        if(!language.equalsIgnoreCase("English") && !language.equalsIgnoreCase("Afrikaans")){
            mPublisher = language;
        }else{
            mLanguage = language;
        }

    }

    public Upload(String name , String imageUrl, String key){
        if (name.trim().equals("")){
            name = "No Name";
        }
        mName=name;
        mImageUrl=imageUrl;
        mkey=key;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public int getmLevel() {
        return mLevel;
    }

    public void setmLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }
}
