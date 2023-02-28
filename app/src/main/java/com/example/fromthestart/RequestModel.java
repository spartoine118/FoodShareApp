package com.example.fromthestart;

public class RequestModel {

    private String requestID;
    private String requester;
    private String requesterName;
    private String poster;
    private String createDate;
    private String postID;
    private int quantity;
    private String detail;
    private String pickupDate;
    private boolean approveStatus;
    private String posterID;
    private String postName;

    public RequestModel(String detail) {
        this.detail = detail;
    }

    public RequestModel(String requester, String requesterName,String poster, String postID, int quantity, String detail, String pickupDate, String createDate, String postName) {
        this.requester = requester;
        this.poster = poster;
        this.postID = postID;
        this.quantity = quantity;
        this.detail = detail;
        this.pickupDate = pickupDate;
        this.createDate = createDate;
        this.postName = postName;
        this.requesterName = requesterName;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public boolean isApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(boolean approveStatus) {
        this.approveStatus = approveStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
