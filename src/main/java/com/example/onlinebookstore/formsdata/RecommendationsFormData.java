package com.example.onlinebookstore.formsdata;


public class RecommendationsFormData {
    private UserProfileFormData userProfileFormData;
    private String recommendationsStrategy;

    public UserProfileFormData getUserProfileFormData() {
        return userProfileFormData;
    }

    public void setUserProfileFormData(UserProfileFormData userProfileFormData) {
        this.userProfileFormData = userProfileFormData;
    }

    public String getRecommendationsStrategy() {
        return recommendationsStrategy;
    }

    public void setRecommendationsStrategy(String recommendationsStrategy) {
        this.recommendationsStrategy = recommendationsStrategy;
    }
}
