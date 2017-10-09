
package com.smt.sabkamaal.holder;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class CategoryWiseProductData {

    @SerializedName("cat_id")
    private String mCatId;
    @SerializedName("discription")
    private String mDiscription;
    @SerializedName("id")
    private String mId;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("product_image")
    private String mProductImage;
    @SerializedName("product_name")
    private String mProductName;
    @SerializedName("quantity")
    private String mQuantity;

    public String getCatId() {
        return mCatId;
    }

    public void setCatId(String catId) {
        mCatId = catId;
    }

    public String getDiscription() {
        return mDiscription;
    }

    public void setDiscription(String discription) {
        mDiscription = discription;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String productImage) {
        mProductImage = productImage;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

}
