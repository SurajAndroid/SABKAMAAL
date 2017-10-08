
package com.smt.sabkamaal.holder;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class ProductCategoryData {

    @SerializedName("category_name")
    private String mCategoryName;
    @SerializedName("id")
    private String mId;
    @SerializedName("status")
    private String mStatus;

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
