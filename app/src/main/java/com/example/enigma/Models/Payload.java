package com.example.enigma.Models;

public class Payload {

    private String msg;
    private String errorMsg;
    private User user;
    private CurPlayer curPlayer;
    private CurPlayer[] leaderBoard;
    private String question;
    private int level;
    private String imgURL;
    private String hint;
    private String howClose;

    public Payload() {
    }

    public Payload(String msg, String errorMsg, User user, CurPlayer curPlayer, CurPlayer[] leaderBoard, String question, int level, String imgURL, String hint, String howClose) {
        this.msg = msg;
        this.errorMsg = errorMsg;
        this.user = user;
        this.curPlayer = curPlayer;
        this.leaderBoard = leaderBoard;
        this.question = question;
        this.level = level;
        this.imgURL = imgURL;
        this.hint = hint;
        this.howClose = howClose;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CurPlayer getCurPlayer() {
        return curPlayer;
    }

    public void setCurPlayer(CurPlayer curPlayer) {
        this.curPlayer = curPlayer;
    }

    public CurPlayer[] getLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(CurPlayer[] leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHowClose() {
        return howClose;
    }

    public void setHowClose(String howClose) {
        this.howClose = howClose;
    }
}

