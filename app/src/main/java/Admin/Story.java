package Admin;

public class Story {
    private String description;
    private String imgURL;
    private int noInStory;

    public Story(String description, String imgURL, int noInStory) {
        this.description = description;
        this.imgURL = imgURL;
        this.noInStory = noInStory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getNoInStory() {
        return noInStory;
    }

    public void setNoInStory(int noInStory) {
        this.noInStory = noInStory;
    }
}
